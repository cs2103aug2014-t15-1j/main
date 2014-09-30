package GUI;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class MainScreen {
    /**
     * private static final int TASK_PANE_WIDTH = 10; private static final int
     * TWO_COLUMNS = 2; private static final String LINE_SEPERATOR = System
     * .getProperty("line.separator"); private static final String
     * MESSAGE_WELCOME = "Welcome to Haystack!" + LINE_SEPERATOR +
     * "Enter “help” for more information."; private static final String
     * MESSAGE_TYPE_HERE = "Type commands here. Press Enter when done."; private
     * static final String MESSAGE_UPCOMING_TASKS = "Upcoming tasks:";
     **/
    protected static final String LINE_SEPARATOR = System
            .getProperty("line.separator");
    private static final String MESSAGE_WELCOME = "Welcome to Haystack!" +
                                                  LINE_SEPARATOR +
                                                  "Enter “help” for more information.";
    private static final String PROGRAM_NAME = "HayStack";
    private static final String TASK_PANE_LABEL = "Upcoming Tasks";
    private static final int MIN_WIDTH_SCREEN = 800;
    private static final int MIN_HEIGHT_SCREEN = 700;

    private static String CODE_CLEAR = " clear";
    private static String CODE_EXIT = " exit";

    public static void main(String[] args) {
        Display display = new Display();
        Shell shell = new Shell(display);
        shell.setText(PROGRAM_NAME);
        GridLayout layout = new GridLayout();
        layout.numColumns = 2;
        shell.setLayout(layout);

        final Text displayScreen = setUpDisplay(shell);
        final Text commandLine = setUp(shell);

        commandLine.addListener(SWT.DefaultSelection, new Listener() {
            public void handleEvent(Event e) {
                String input = commandLine.getText();
                String output = ResultGenerator.sendInput(input);

                if (output.endsWith(CODE_CLEAR)) {
                    clearScreen(displayScreen);
                } else if (output.endsWith(CODE_EXIT)) {
                    Exit(output, displayScreen);
                } else {
                    displayScreen.append(LINE_SEPARATOR + output);
                }
            }
        });

        // end
        shell.pack();
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch())
                display.sleep();
        }
        display.dispose();
    }

    private static void Exit(String output, Text displayScreen) {
        String toWrite = removeLastWord(output);
        displayScreen.append(toWrite);
        System.exit(0);
    }

    private static void clearScreen(Text displayScreen) {
        displayScreen.setText("");
    }

    private static String removeLastWord(String line) {
        int lastWord = line.lastIndexOf(" ");
        String lineWithoutLastWord = line.substring(0, lastWord);
        return lineWithoutLastWord;
    }

    private static Text setUp(Shell shell) {
        GridData gridData;

        Label taskPaneLabel = new Label(shell, SWT.NULL);
        taskPaneLabel.setText(TASK_PANE_LABEL);
        gridData = new GridData(GridData.HORIZONTAL_ALIGN_CENTER);
        taskPaneLabel.setLayoutData(gridData);

        Text upcomingTasks = new Text(shell, SWT.MULTI | SWT.READ_ONLY |
                                             SWT.BORDER);
        gridData = new GridData(GridData.FILL_BOTH);
        upcomingTasks.setLayoutData(gridData);
        upcomingTasks.setBounds(10, 10, 100, 100);

        Text commandLine = new Text(shell, SWT.SINGLE | SWT.BORDER_SOLID);
        gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
        gridData.horizontalSpan = 2;
        commandLine.setLayoutData(gridData);
        return commandLine;
    }

    private static Text setUpDisplay(Shell shell) {
        Text displayScreen = new Text(shell, SWT.MULTI | SWT.BORDER |
                                             SWT.READ_ONLY);
        GridData gridData = new GridData(GridData.FILL_BOTH);
        gridData.widthHint = MIN_WIDTH_SCREEN;
        gridData.heightHint = MIN_HEIGHT_SCREEN;
        gridData.verticalSpan = 2;
        displayScreen.setLayoutData(gridData);
        displayScreen.setText(MESSAGE_WELCOME);
        return displayScreen;
    }

}
