//@author A0118846W
package gui;

import logic.Log;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
/**
 *This class processes the input entered by the user into the commandLine interface. 
 *It calls other relevant classes to process the input by the user and displays the feedback
 */
public class ProcessUserInteraction {

    private static final String CONFIRM = "yes";
    private static final String NO_CONFIRM = "no";

    private static final String CODE_HELP = "Help";
    private static final String CODE_EXIT = "exit";

    // Feedback messages to be displayed to the user
    private static final String ASK_CONFIRM_DELETE = "This will erase all data, PERMANENTLY.  Key 'y' to continue or 'n' to abort";
    private static final String INVALID_INPUT = "Invalid Input.";
    private static final String SUCCESSFUL_DELETE_ALL = "Erased all data!";
    private static final String UNSUCCESSFUL_DELETE_ALL = "Did not delete anything";

    // command formats that displayed to the user while typing
    private static final String COMMAND_FORMAT_ADD = "add [name] due [DD/MM/YYYY hhmm] start [DD/MM/YYYY hhmm] #tag";
    private static final String COMMAND_FORMAT_BLOCK = "block [DD/MM/YYYY hhmm] to [DD/MM/YYYY hhmm]";
    private static final String COMMAND_FORMAT_DISPLAY = "display";
    private static final String COMMAND_FORMAT_DISPLAY_BLOCK = "%1$s block";
    private static final String COMMAND_FORMAT_DISPLAY_DONE = "%1$s done";
    private static final String COMMAND_FORMAT_DISPLAY_SOMEDAY = "%1$s someday";
    private static final String COMMAND_FORMAT_DISPLAY_TODAY = "%1$s today";
    private static final String COMMAND_FORMAT_DISPLAY_TOMORROW = "%1$s tomorrow";
    private static final String COMMAND_FORMAT_DISPLAY_UPCOMING = "%1$s upcoming";
    private static final String COMMAND_FORMAT_DONE = "done [id]";
    private static final String COMMAND_FORMAT_DELETE = "delete [id]";
    private static final String COMMAND_FORMAT_EXIT = "exit";
    private static final String COMMAND_FORMAT_EDIT = "edit [id] <parameter>  [value]";
    private static final String COMMAND_FORMAT_GOTTA = "gotta [name] by [date] tag";
    private static final String COMMAND_FORMAT_HELP = "help";
    private static final String COMMAND_FORMAT_REDO = "redo [id]";
    private static final String COMMAND_FORMAT_RESET = "reset";
    private static final String COMMAND_FORMAT_RESTORE = "restore [id]";
    private static final String COMMAND_FORMAT_SHOW = "show";
    private static final String COMMAND_FORMAT_SEARCH = "search [id]";
    private static final String COMMAND_FORMAT_TODO = "todo [id]";
    private static final String COMMAND_FORMAT_UNBLOCK = "unblock [DD/MM/YYYY hhmm] to [DD/MM/YYYY hhmm]";
    private static final String COMMAND_FORMAT_UNDO = "undo";

    // The interface where the user enters commands
    private Text commandLine;
    // The interface the displays feedback on the status of the user's commands
    private StyledText feedback;

    // display containing the program
    private Display display;

    // shell containing the program
    private Shell shell;

    private static ResultGenerator resultGenerator;
    private boolean isReplyToConfrimation = false;

    public ProcessUserInteraction() {
        commandLine = FeedbackAndInput.getCommandLine();
        feedback = FeedbackAndInput.getFeedback();
        display = commandLine.getDisplay();
        shell = commandLine.getShell();
        resultGenerator = ResultGenerator.getInstance();
        addListeners();
    }
    
    private void addListeners() {
        addShellListeners();
        addCommandLineListeners();
    }
    
    
    /**
     * adds listeners that are activated anywhere within the shell
     */
    private void addShellListeners() {
        addHelpListener();
    }
    
    /**
     * adds listener to open the help dialog when f1 is entered by the user
     */
    private void addHelpListener() {
        display.addFilter(SWT.KeyDown, new Listener() {
            public void handleEvent(Event event) {
                if (event.keyCode == SWT.F1) {
                    openHelpDialog();
                } else {
                    if (HelpDialog.getShell() != null && isDialogOpen()) {
                        Shell helpDialogShell = HelpDialog.getShell();
                        helpDialogShell.close();
                        helpDialogShell.dispose();
                    }
                }
            }
        });
    }
    
    /**
     * adds all the listeners to the command line interface
     */
    private void addCommandLineListeners() {
        addListenerRemoveText();

        addKeyBoardListener();

        addListenerProcessInput();
    }

    /**
     * Adds listeners to erases all text from commandLine when a single key is
     * entered. Only works at start of application
     */
    private void addListenerRemoveText() {
        commandLine.addListener(SWT.KeyDown, new Listener() {
            @Override
            public void handleEvent(Event event) {

                switch (event.type) {
                    case SWT.KeyDown:
                        commandLine.setText("");
                        commandLine.removeListener(SWT.KeyDown, this);
                        
                        break;
                }
            }
        });
    }

    /**
     * Adds listeners to every key entered by user
     */
    private void addKeyBoardListener() {
        commandLine.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                String input = commandLine.getText();
                if (e.keyCode == SWT.ARROW_UP || e.keyCode == SWT.ARROW_DOWN) {
                    String output = "";
                    switch (e.keyCode) {
                        case SWT.ARROW_UP:
                            output = resultGenerator.getUpKeyInput();
                            break;
                        case SWT.ARROW_DOWN:
                            output = resultGenerator.getDownKeyInput();
                            break;
                        default:
                            // ignore
                    }
                    commandLine.setText(output);
                }
                giveCommandFormatHint(input);
            }
        });
    }

    /**
     * Adds listener to process any text input entered by the user into
     * commandLine, every time ENTER is pressed
     */
    private void addListenerProcessInput() {
        commandLine.addListener(SWT.DefaultSelection, new Listener() {
            @Override
            public void handleEvent(Event event) {
                String input = commandLine.getText();
                commandLine.setText("");
                if(Log.LOGGING_ENABLED){
                    Log.getLogger().info("reading user input" + input );
                }
                try {
                    if (isReplyToConfrimation) {
                        processReply(input);
                    } else if (input.trim().isEmpty()) {
                        // do nothing, if input is empty
                    } else {
                        processInput(input);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    feedback.setText(e.getMessage());
                    if(Log.LOGGING_ENABLED){
                        Log.getLogger().warning("error in processing input");
                    }
                }
            }
        });
    }

    protected void processReply(String input) {
        if (!isValidDeleteAll(input)) {
            feedback.setText(INVALID_INPUT);

        }
        if (resultGenerator.processDeleteAll(input)) {
            feedback.setText(SUCCESSFUL_DELETE_ALL);
        } else {
            feedback.setText(UNSUCCESSFUL_DELETE_ALL);
        }

        isReplyToConfrimation = false;
    }

    private boolean isValidDeleteAll(String input) {
        String inputLowerCase = input.toLowerCase();
        if (inputLowerCase.equals(CONFIRM) || inputLowerCase.equals("y")) {
            return true;
        }

        if (inputLowerCase.equals(NO_CONFIRM) || inputLowerCase.equals("n")) {
            return true;
        }

        return false;
    }

    protected void processInput(String input) {
        String output = resultGenerator.sendInput(input);

        if (output.equals(CODE_EXIT)) {
            exitProgram();
        } else if (output.equals(ASK_CONFIRM_DELETE)) {
            feedback.setText(ASK_CONFIRM_DELETE);
            isReplyToConfrimation = true;

        } else if (output.equals(CODE_HELP)) {
            openHelpDialog();

        } else {

            feedback.setText(output);
        }
    }

    private void exitProgram() {
        shell.dispose();
        display.dispose();
        Images.disposeAllImages();
        System.exit(0);
    }

    protected void giveCommandFormatHint(String input) {

        if (input.isEmpty()) {
            return;
        }

        Character firstLetter = input.charAt(0);
        switch (Character.toLowerCase(firstLetter)) {
            case 'a':
                feedback.setText(COMMAND_FORMAT_ADD);
                return;
            case 'b':
                feedback.setText(COMMAND_FORMAT_BLOCK);
                return;
            case 'd':
                if (!isOutOfBounds(input, 1) &&
                    Character.toLowerCase(input.charAt(1)) == 'i') {
                    feedback.setText(COMMAND_FORMAT_DISPLAY);
                    displayWhichTab(input);
                    return;
                } else if (!isOutOfBounds(input, 1) &&
                           Character.toLowerCase(input.charAt(1)) == 'o') {
                    feedback.setText(COMMAND_FORMAT_DONE);
                    return;
                }
                feedback.setText(COMMAND_FORMAT_DELETE);

                return;
            case 'e':
                if (!isOutOfBounds(input, 1) &&
                    Character.toLowerCase(input.charAt(1)) == 'x') {
                    feedback.setText(COMMAND_FORMAT_EXIT);
                    return;
                }
                feedback.setText(COMMAND_FORMAT_EDIT);
                return;
            case 'g':
                feedback.setText(COMMAND_FORMAT_GOTTA);
            case 'h':
                feedback.setText(COMMAND_FORMAT_HELP);
                return;
            case 'r':
                if (!isOutOfBounds(input, 2) &&
                    Character.toLowerCase(input.charAt(2)) == 'd') {
                    feedback.setText(COMMAND_FORMAT_REDO);
                    return;
                } else if (!isOutOfBounds(input, 3) &&
                           (Character.toLowerCase(input.charAt(3)) == 'e')) {
                    feedback.setText(COMMAND_FORMAT_RESET);
                    return;
                }
                feedback.setText(COMMAND_FORMAT_RESTORE);

                return;
            case 's':
                if (!isOutOfBounds(input, 1) &&
                    Character.toLowerCase(input.charAt(1)) == 'h') {
                    feedback.setText(COMMAND_FORMAT_SHOW);
                    displayWhichTab(input);
                    return;
                }
                feedback.setText(COMMAND_FORMAT_SEARCH);
                return;
            case 't':
                feedback.setText(COMMAND_FORMAT_TODO);
                return;
            case 'u':
                if (!isOutOfBounds(input, 2) &&
                    (Character.toLowerCase(input.charAt(2)) == 'b')) {
                    feedback.setText(COMMAND_FORMAT_UNBLOCK);
                    return;
                }
                feedback.setText(COMMAND_FORMAT_UNDO);
                return;

        }
    }

    private void displayWhichTab(String input) {
        String word = getFirstWord(input);
        input = removefirstWord(input);
        if (input.isEmpty() || word.isEmpty() ||
            !word.equalsIgnoreCase(COMMAND_FORMAT_DISPLAY) &&
            !word.equalsIgnoreCase(COMMAND_FORMAT_SHOW)) {
            return;
        }

        if (!isOutOfBounds(input, 0)) {
            switch (Character.toLowerCase(input.charAt(0))) {
                case 'b':
                    feedback.setText(String
                            .format(COMMAND_FORMAT_DISPLAY_BLOCK, word));
                    return;
                case 'd':
                    feedback.setText(String.format(COMMAND_FORMAT_DISPLAY_DONE,
                                                   word));
                    return;
                case 's':
                    feedback.setText(String
                            .format(COMMAND_FORMAT_DISPLAY_SOMEDAY, word));
                    return;
                case 't':
                    if (!isOutOfBounds(input, 2) &&
                        (Character.toLowerCase(input.charAt(2))) == 'd') {
                        feedback.setText(String
                                .format(COMMAND_FORMAT_DISPLAY_TODAY, word));
                    } else {
                        feedback.setText(String
                                .format(COMMAND_FORMAT_DISPLAY_TOMORROW, word));
                    }

                    return;
                case 'u':
                    feedback.setText(String
                            .format(COMMAND_FORMAT_DISPLAY_UPCOMING, word));
                    return;
                default:
                    return;
            }
        }

    }

    private String getFirstWord(String line) {
        int index = line.trim().indexOf(' ');
        if (index == -1) {
            return "";
        }
        return line.subSequence(0, index).toString();
    }

    private String removefirstWord(String line) {
        int index = line.indexOf(' ');
        if (index == -1) {
            return "";
        }

        return line.substring(index).trim();
    }

    /**
     * Checks to see if index is out of bounds in the string
     * 
     * @param input
     *            String to check
     * @param index
     *            index to check
     * @return true if its out bounds, false otherwise
     */
    protected boolean isOutOfBounds(String input, int index) {
        if (input.length() - 1 < index) {
            return true;
        }
        return false;
    }

    private void openHelpDialog() {
        if (!isDialogOpen()) {
            Shell dialogShell = new Shell(Display.getCurrent());
            dialogShell.setData("ID");
            HelpDialog dialog = new HelpDialog(dialogShell);
            dialog.open();
            dialogShell.setData("different");
        }

    }

    private boolean isDialogOpen() {
        Shell[] shells = Display.getCurrent().getShells();
        int size = shells.length;
        for (int index = 0; index < size; index++) {
            String data = (String) shells[index].getData();
            if (data != null && data.equals("ID")) {
                return true;
            }
        }
        return false;
    }

}