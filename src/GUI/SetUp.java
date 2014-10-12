package GUI;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
/**
 * Represents the layout of the HayStack window. It creates a window and sets up the window of the Program when called.
 * @author Sharon
 */
public class SetUp {

	// Gets the new line character used by the user's system
	private static final String LINE_SEPARATOR = System
			.getProperty("line.separator");

	private static final String MESSAGE_WELCOME = "Welcome to Haystack!"
			+ LINE_SEPARATOR + "Enter 'help' for more information";
	private static final String PROGRAM_NAME = "Haystack Task Manager";
	private static final String TASK_PANE_LABEL = "Upcoming Tasks:";

	// default dimensions of the window
	private static final int NUM_COLS_PROGRAM = 2;
	private static final int MIN_WIDTH_SCREEN = 600;
	private static final int MIN_HEIGHT_SCREEN = 500;

	private static Text displayScreen;
	private static Text commandLine;
	private static Text upcomingTasks;
	private static Label taskPane;
	private static GridData gridData;
	private static Display display;
	private static Shell shell;

	/**
	 * Constructs a setUp object that creates the main shell for HayStack.
	 * @param displayToSet The Display object that the shell uses to create the window
	 * @param shellToSet The Shell object that the program needs to set.
	 */
	public SetUp(Display displayToSet, Shell shellToSet) {
	    // use Abstraction -- Occurrence
		display = displayToSet;
		shell = shellToSet;

		initialise();
	}

	/**
	 * Returns the text object of HayStack window where user feedback is displayed
	 * @return Text 
	 */
	public Text getDisplayScreen() {
		return displayScreen;
	}
	
	/**
	 * Returns the text object of the HayStack window where the user enters commands
	 * @return Text
	 */
	public Text getCommandLine() {
		return commandLine;
	}
	    
	/**
	 * Returns the text object of the HayStack window where the user's upcoming tasks are displayed
	 * @return Text
	 */
	public Text getUpcomingTasks() {
		return upcomingTasks;
	}


	private static void initialise() {
		setUpShell();
		setUpWidgets();
	}

	private static void setUpShell() {
		shell.setText(PROGRAM_NAME);
		GridLayout layout = new GridLayout();
		layout.numColumns = NUM_COLS_PROGRAM;
		shell.setLayout(layout);
	}

	private static void setUpWidgets() {
		setUpDisplayScreen();
		setUpTaskPaneLabel();
		setUpUpcomingTasks();
		setUpCommandLine();
	}

	private static void setUpDisplayScreen() {
		displayScreen = new Text(shell, SWT.MULTI | SWT.BORDER | SWT.READ_ONLY);
		gridData = new GridData(GridData.FILL_BOTH);
		gridData.widthHint = MIN_WIDTH_SCREEN;
		gridData.heightHint = MIN_HEIGHT_SCREEN;
		gridData.verticalSpan = NUM_COLS_PROGRAM;
		displayScreen.setLayoutData(gridData);
		displayScreen.setEditable(false);
		displayScreen.setText(MESSAGE_WELCOME);
		displayScreen.setBackground(display.getSystemColor(SWT.COLOR_BLACK));
		displayScreen.setForeground(display.getSystemColor(SWT.COLOR_GREEN));
		// displayScreen.setFont(font);
	}

	private static void setUpTaskPaneLabel() {

		taskPane = new Label(shell, SWT.NULL);
		taskPane.setText(TASK_PANE_LABEL);
		gridData = new GridData(GridData.HORIZONTAL_ALIGN_CENTER);
		taskPane.setLayoutData(gridData);
		// taskPane.setFont(font);
	}

	private static void setUpUpcomingTasks() {
		upcomingTasks = new Text(shell, SWT.MULTI | SWT.READ_ONLY | SWT.BORDER);
		gridData = new GridData(GridData.FILL_BOTH);
		upcomingTasks.setBackground(display.getSystemColor(SWT.COLOR_BLACK));
		upcomingTasks.setForeground(display.getSystemColor(SWT.COLOR_GREEN));
		upcomingTasks.setLayoutData(gridData);
		upcomingTasks.setEditable(false);
		// upcomingTasks.setFont(font);
	}

	private static void setUpCommandLine() {
		commandLine = new Text(shell, SWT.SINGLE | SWT.BORDER_SOLID);
		gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		gridData.horizontalSpan = 2;
		commandLine.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
		commandLine.setLayoutData(gridData);
		// commandLine.setFont(font);
	}

}
