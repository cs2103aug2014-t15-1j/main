package gui;

import logic.Processor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

//@author A0118846W
public class ProcessUserInteraction {
    
    private static final String ASK_CONFIRM_DELETE = "This will erase all data, PERMANENTLY.  Key 'y' to continue or 'n' to abort";
    private static final String INVALID_INPUT = "Invalid Input.";
    private static final String SUCCESSFUL_DELETE_ALL = "Erased all data!";
    private static final String UNSUCCESSFUL_DELETE_ALL = "Did not delete anything";
    
    

    private static final String CONFIRM = "yes";
    private static final String NO_CONFIRM = "no";

    private static String CODE_EXIT = "exit";
    
    private static ResultGenerator resultGenerator;
    private Text commandLine;
    private StyledText feedback;
    private boolean isReplyToConfrimation = false;
    private Display display;
    private Shell shell;
    
    public ProcessUserInteraction(){
        commandLine = FeedbackAndInput.getCommandLine();
        feedback =  FeedbackAndInput.getFeedback();
        display = commandLine.getDisplay();
        shell = commandLine.getShell();
        resultGenerator = ResultGenerator.getInstance();
        addListeners();
    }
    
    private void addListeners() {
        addShellListeners();
        addCommandLineListeners();
    }

    private void addShellListeners() {
        addHelpListener();
//        addChangeTabListener();
    }

    private void addHelpListener() {
        display.addFilter(SWT.KeyDown, new Listener(){
            public void handleEvent(Event event){
                if(event.keyCode == SWT.F1){
                   openHelpDialog();
                }else{
                    if(HelpDialog.getShell() != null && isDialogOpen()){
                        Shell helpDialogShell = HelpDialog.getShell();
                        helpDialogShell.close();
                        helpDialogShell.dispose();
                    }
                }
            }
        });
    }

//    // OBSOLETE
//    private void addChangeTabListener() {
//        display.addFilter(SWT.KeyDown, new Listener() {
//
//            public void handleEvent(Event event) {
//                if ( ( (event.stateMask & SWT.CTRL) == SWT.CTRL) &&
//                    (event.keyCode == 'd') ) {
//                    changeTabs();
//                }
//            }
//        });
//    }
//
//    private void changeTabs() {
//        CTabFolder folder = TableComposite.getFolder();
//        int index = folder.getSelectionIndex();
//        if (index == 0) {
//            folder.setSelection(1);
//        } else {
//            folder.setSelection(0);
//        }
//    }

    private void addCommandLineListeners() {
        addListenerRemoveText();

        addKeyBoardListener();

        addListenerProcessInput();
    }
    
    /**
     * Adds listeners to erases all text from commandLine when a single key is entered.
     * Only works at start of application
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
                            output = ResultGenerator.getUpKeyInput();
                            break;
                        case SWT.ARROW_DOWN:
                            output = ResultGenerator.getDownKeyInput();
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
     * Adds listener to process any text input entered by the user into commandLine, everytime ENTER is pressed
     */
    private void addListenerProcessInput() {
        commandLine.addListener(SWT.DefaultSelection, new Listener() {
            @Override
            public void handleEvent(Event event) {
                String input = commandLine.getText();
                commandLine.setText("");
                try{
                if (isReplyToConfrimation) {
                    processReply(input);
                } else if (input.trim().isEmpty()) {
                    // do nothing, if input is empty
                } else {
                    processInput(input);
                }
                }catch(Exception e){
                    e.printStackTrace();
                  feedback.setText(e.getMessage());
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

        } else if (output.equals("Help")) {
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
                feedback.setText("add [name] due [DD/MM/YYYY hhmm] start [DD/MM/YYYY hhmm] #tag");
                return;
            case 'b':
                feedback.setText("block [DD/MM/YYYY hhmm] to [DD/MM/YYYY hhmm]");
                return;
            case 'd':
                if (!isOutOfBounds(input, 1) &&
                    Character.toLowerCase(input.charAt(1)) == 'i') {
                    feedback.setText("display");
                    return;
                } else if (!isOutOfBounds(input, 1) &&
                           Character.toLowerCase(input.charAt(1)) == 'o') {
                    feedback.setText("done [id]");
                    return;
                }
                feedback.setText("delete [id] ");

                return;
            case 'e':
                if (!isOutOfBounds(input, 1) &&
                    Character.toLowerCase(input.charAt(1)) == 'x') {
                    feedback.setText("exit");
                    return;
                }
                feedback.setText("edit [id] <parameter>  [value]");
                return;
            case 'h':
                feedback.setText("help");
                return;
            case 'r':
                if (!isOutOfBounds(input, 2) &&
                    Character.toLowerCase(input.charAt(2)) == 'd') {
                    feedback.setText("redo [id]");
                    return;
                } else if (!isOutOfBounds(input, 3) &&
                           (Character.toLowerCase(input.charAt(3)) == 'e')) {
                    feedback.setText("reset");
                    return;
                }
                feedback.setText("restore [id]");

                return;
            case 's':
                if (!isOutOfBounds(input, 1) &&
                    Character.toLowerCase(input.charAt(1)) == 'h') {
                    feedback.setText("show");
                    return;
                }
                feedback.setText("search [id]");
                return;
            case 't':
                feedback.setText("todo [id]");
                return;
            case 'u':
                if (!isOutOfBounds(input, 2) &&
                    (Character.toLowerCase(input.charAt(2)) == 'b')) {
                    feedback.setText("unblock [DD/MM/YYYY hhmm] to [DD/MM/YYYY hhmm]");
                    return;
                }
                feedback.setText("undo");
                return;

        }
    }
    
    /**
     * Checks to see if index is out of bounds in the string
     * @param input String to check
     * @param index index to check
     * @return true if its out bounds, false otherwise
     */
    protected boolean isOutOfBounds(String input, int index) {
        if (input.length() - 1 < index) {
            return true;
        }
        return false;
    }
    

    private void openHelpDialog() {
        if(!isDialogOpen()){
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
        for(int index = 0; index < size; index++){
            String data = (String) shells[index].getData();
            if(data != null && data.equals("ID")){
                return true;
            }
        }
        return false;
    }

}