import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

public class MainScreen extends Composite {

    private static final int TASK_PANE_WIDTH = 10;
    private static final int TWO_COLUMNS = 2;
    private static final String MESSAGE_WELCOME = "Welcome to Haystack!%nEnter “help” for more information.";
    private static final String MESSAGE_TYPE_HERE = "Type commands here. Press Enter when done.";
    private static final String MESSAGE_UPCOMING_TASKS = "Upcoming tasks:";

    /**
     * Create the composite.
     * 
     * @param parent
     * @param style
     */
    public MainScreen(Composite parent, int style) {
        super(parent, style);

        // sets background of window as blue
        setBackground(SWTResourceManager.getColor(185, 209, 234));
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = TWO_COLUMNS * TASK_PANE_WIDTH;
        setLayout(gridLayout);

        Text displayScreen = new Text(this, SWT.READ_ONLY | SWT.MULTI
                | SWT.BORDER | SWT.V_SCROLL);

        GridData displayScreenLayout = new GridData();
        setUpTopScreen(displayScreenLayout, TWO_COLUMNS);

        displayScreen.setLayoutData(displayScreenLayout);
        displayScreen.setText(MESSAGE_WELCOME);

        Text taskPane = new Text(this, SWT.READ_ONLY | SWT.MULTI | SWT.BORDER
                | SWT.V_SCROLL);

        GridData taskPaneLayout = new GridData();
        setUpTopScreen(taskPaneLayout, TASK_PANE_WIDTH);

        taskPane.setLayoutData(taskPaneLayout);
        taskPane.setText(MESSAGE_UPCOMING_TASKS);

        Text userInputBox = new Text(this, SWT.BORDER);
        GridData inputBoxLayout = new GridData();
        setUpBottomScreen(displayScreenLayout, inputBoxLayout);

        userInputBox.setLayoutData(inputBoxLayout);
        userInputBox.setText(MESSAGE_TYPE_HERE);

        userInputBox.addListener(SWT.Traverse, new Listener() {

            @Override
            public void handleEvent(Event event) {
                if (event.detail == SWT.TRAVERSE_RETURN) {
                    // To get user input and call another class
                    // get output and print to screen
                }
            }
        });

    }

    private void setUpBottomScreen(GridData displayScreenLayout,
            GridData inputBoxLayout) {
        inputBoxLayout.horizontalSpan = TASK_PANE_WIDTH + TASK_PANE_WIDTH
                + TASK_PANE_WIDTH;
        inputBoxLayout.horizontalAlignment = GridData.FILL;
        displayScreenLayout.grabExcessHorizontalSpace = true;
    }

    private void setUpTopScreen(GridData screenLayout, int width) {
        screenLayout.horizontalSpan = width;
        screenLayout.horizontalAlignment = GridData.FILL;
        screenLayout.grabExcessHorizontalSpace = true;
        screenLayout.grabExcessVerticalSpace = true;
    }

    @Override
    protected void checkSubclass() {
        // Disable the check that prevents subclassing of SWT components
    }

    public static void main(String[] args) {
        Display display = new Display();

        Shell shell = new Shell(display);
        MainScreen screen = new MainScreen(shell, SWT.NONE);
        // the layout manager handle the layout
        // of the widgets in the container

        // start

        // end
        screen.pack();
        shell.pack();
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch())
                display.sleep();
        }
        display.dispose();
    }
}
