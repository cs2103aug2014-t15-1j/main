package gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * Represents the main window of HayStack. Any user interaction with the program
 * will be processed here. Note: All methods in this class are static
 * 
 * @author Sharon
 *
 */
public class MainScreen {
    // implement: task pane expansion, add upcoming tasks
    // increase size of commandLine if line does not fit into space
    // change font type, add scroll bar
    // add hayStack icon, ASCII picture
    // use mono-space font
    // one line display
    private static final String ASK_CONFIRM_DELETE = "This will erase all data, PERMANENTLY.  Key 'y' to continue or 'n' to abort";
    private static final String INVALID_INPUT = "Invalid Input.";
    private static final String SUCCESSFUL_DELETE_ALL = "Erased all data!";
    private static final String UNSUCCESSFUL_DELETE_ALL = "Did not delete anything";
    private static final String MESSAGE_TYPE_HERE = "Type here";

    private static final String CONFIRM = "yes";
    private static final String NO_CONFIRM = "no";

    private static String CODE_EXIT = "exit";
    private static boolean askConfrim = false;
    private static boolean isFeedback = false;

    private static ResultGenerator resultGenerator = new ResultGenerator();

    /**
     * The entire HayStack program runs from this function. It sets up the
     * graphical user interface that the user interacts with any reads any input
     * that the user enters.
     * 
     * @param args
     */
    public static void main(String[] args) {
        Display display = new Display();

        Image background = new Image(display, MainScreen.class.getClassLoader()
                .getResourceAsStream("resource/mainbg.png"));

        final Shell shell = new Shell(display);

        shell.setImage(new Image(display, MainScreen.class.getClassLoader()
                .getResourceAsStream("resource/Icon.gif")));
        ImageData imageData = background.getImageData();
        imageData = imageData.scaledTo(shell.getSize().x, shell.getSize().y);
        shell.setBackgroundImage(background);
        shell.setBackgroundMode(SWT.INHERIT_FORCE);

        Monitor primary = display.getPrimaryMonitor();
        Rectangle bounds = primary.getBounds();
        Rectangle rect = shell.getBounds();

        int x = bounds.x + (bounds.width - rect.width) / 2;
        int y = bounds.y + (bounds.height - rect.height) / 2;

        shell.setLocation(x, y);

        final SetUp setUpScreen = SetUp.getInstance(shell);
        resultGenerator.start();

        removeText(setUpScreen);

        try {
            provideFeedback(setUpScreen);
            readUserInput(setUpScreen);
        } catch (Exception e) {
            String message = e.getMessage();
            setUpScreen.getFeedBack().setText(message);
            readUserInput(setUpScreen);
        }
        setUpScreen.getCommandLine().setFocus();

        display.addFilter(SWT.KeyDown, new Listener() {

            public void handleEvent(Event event) {
                if (event.keyCode == SWT.ALT) {
                    CTabFolder folder = setUpScreen.getTabFolder();
                    int index = folder.getSelectionIndex();
                    if (index == 0) {
                        folder.setSelection(1);
                    } else {
                        folder.setSelection(0);
                    }
                } else if (event.keyCode == SWT.F1) {
                    new HelpDialog(shell);

                }
            }
        });

        shell.pack();
        shell.open();

        while (!shell.isDisposed()) {
            if (!display.readAndDispatch())
                display.sleep();
        }

        background.dispose();
        display.dispose();
    }

    private static void removeText(SetUp screen) {

        final Text commandLine = screen.getCommandLine();
        final StyledText feedback = screen.getFeedBack();
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

    private static void provideFeedback(final SetUp screen) {
        final Text commandLine = screen.getCommandLine();
        final StyledText feedback = screen.getFeedBack();

        commandLine.addKeyListener(new KeyAdapter() {

            @Override
            public void keyReleased(KeyEvent e) {
                giveSuggestions(commandLine, feedback);
                if (commandLine.getText().isEmpty() && isFeedback) {
                    feedback.setText("");
                }

                isFeedback = false;
            }

        });
    }

    private static void readUserInput(final SetUp screen) {

        final Text commandLine = screen.getCommandLine();
        final StyledText feedback = screen.getFeedBack();

        commandLine.addListener(SWT.DefaultSelection, new Listener() {
            @Override
            public void handleEvent(Event event) {

                String input = commandLine.getText();
                String output = "";

                commandLine.setText("");
                if (askConfrim) {
                    if (!isValidDeleteAll(input)) {
                        feedback.setText(INVALID_INPUT);

                    }
                    if (resultGenerator.processDeleteAll(input)) {
                        feedback.setText(SUCCESSFUL_DELETE_ALL);
                    } else {
                        feedback.setText(UNSUCCESSFUL_DELETE_ALL);
                    }

                    askConfrim = false;

                } else if (input.trim().isEmpty()) {
                    // do nothing;
                } else {
                    try {
                        output = resultGenerator.sendInput(input);

                        // output = commandLine.getText();
                        if (output.equals(CODE_EXIT)) {
                            exitProgram();
                        } else if (output.equals(ASK_CONFIRM_DELETE)) {
                            feedback.setText(ASK_CONFIRM_DELETE);
                            askConfrim = true;

                        } else if (output.equals("Help")) {

                            SetUp setUp = SetUp.getInstance();
                            new HelpDialog(setUp.getShell());
                        } else {

                            feedback.setText(output);
                        }

                    } catch (Exception error) {

                        String message = error.getMessage();
                        if (message == null) {
                            message = "An internal error occurred.";
                        }
                        feedback.setText(message);
                        // remove this line before final launch
                        error.printStackTrace();
                    }
                }
            }

            private boolean isValidDeleteAll(String input) {
                String inputLowerCase = input.toLowerCase();
                if (inputLowerCase.equals(CONFIRM) ||
                    inputLowerCase.equals("y")) {
                    return true;
                }

                if (inputLowerCase.equals(NO_CONFIRM) ||
                    inputLowerCase.equals("n")) {
                    return true;
                }

                return false;
            }
        });
    }

    private static void exitProgram() {
        System.exit(0);
    }

    protected static void giveSuggestions(final Text commandLine,
                                          final StyledText feedback) {
        String input = commandLine.getText();

        if (input.isEmpty()) {
            return;
        }

        Character firstLetter = input.charAt(0);
        switch (Character.toLowerCase(firstLetter)) {
            case 'a':
                feedback.setText("add [name] start: [DD/MM/YYYY hhmm] #tag");
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
                    feedback.setText("done");
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
                feedback.setText("edit [id] <parameter>:  [value]");
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

    protected static boolean isOutOfBounds(String input, int index) {
        if (input.length() - 1 < index) {
            return true;
        }
        return false;
    }

}
