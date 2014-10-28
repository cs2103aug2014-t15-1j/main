package gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
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
    private static final String INVALID_INPUT = "Invalid Input. Type yes or no.";
    private static final String SUCCESSFUL_DELETE_ALL = "Erased all data!";
    private static final String UNSUCCESSFUL_DELETE_ALL = "Did not delete anything";

    private static final String CONFIRM = "yes";
    private static final String NO_CONFIRM = "no";

    private static String CODE_EXIT = "exit";
    private static boolean askConfrim = false;

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

        Shell shell = new Shell(display);

        shell.setImage(new Image(display, MainScreen.class.getClassLoader()
                .getResourceAsStream("resource/Icon.gif")));
        ImageData imageData = background.getImageData();
        imageData = imageData.scaledTo(shell.getSize().x, shell.getSize().y);
        shell.setBackgroundImage(background);
        shell.setBackgroundMode(SWT.INHERIT_FORCE);

        SetUp setUpScreen = SetUp.getInstance(shell);
        addWindowListeners(shell, setUpScreen);
        resultGenerator.start();

        removeText(setUpScreen);

        try {
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

    private static void addWindowListeners(Shell shell, SetUp screen) {
        Control[] controls = shell.getChildren();

        for (int index = 0; index < controls.length; index++) {
            controls[index].addKeyListener(new KeyAdapter() {

                public void keyPressed(KeyEvent event) {

                    if ((event.stateMask & SWT.SHIFT) != 0) {
                        System.out.println("I am pressing tab");
                        CTabFolder folder = screen.getTabFolder();
                        int index = folder.getSelectionIndex();
                        if (index == 0) {
                            folder.setSelection(1);
                        } else if (index == 1) {
                            folder.setSelection(0);
                        }
                    }

                }
            });
        }
    }

    private static void removeText(SetUp screen) {

        final Text commandLine = screen.getCommandLine();
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
                    while (!isValidDeleteAll(input)) {
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
}
