package GUI;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class SetUp {
	
	// Gets the new line character used by the user's system
	private static final String LINE_SEPARATOR = System.getProperty("line.separator");
	
	private static final String MESSAGE_WELCOME = "Welcome to Haystack!" + LINE_SEPARATOR + "Enter 'help' for more information";
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
	
	public SetUp(Display displayToSet, Shell shellToSet){
		
		display = displayToSet;
		shell = shellToSet;
		
		initialise();
    }
	
	public Text getDisplayScreen(){
		return displayScreen;
	}
	
	public Text getCommandLine(){
		return commandLine;
	}
	
	public Text getUpcomingTasks(){
		return upcomingTasks;
	}
	private static void initialise(){
		setUpShell();
		setUpWidgets();
	}
	
	private static void setUpShell(){
        shell.setText(PROGRAM_NAME);
        GridLayout layout = new GridLayout();
        layout.numColumns = NUM_COLS_PROGRAM;
        shell.setLayout(layout);
	}
	
	private static void setUpWidgets(){
		setUpDisplayScreen();
		setUpTaskPaneLabel();
		setUpUpcomingTasks();
		setUpCommandLine();
	}
	
	private static void setUpDisplayScreen(){
		displayScreen = new Text(shell, SWT.MULTI | SWT.BORDER
                | SWT.READ_ONLY);
        gridData = new GridData(GridData.FILL_BOTH);
        gridData.widthHint = MIN_WIDTH_SCREEN;
        gridData.heightHint = MIN_HEIGHT_SCREEN;
        gridData.verticalSpan = NUM_COLS_PROGRAM;
        displayScreen.setLayoutData(gridData);
        displayScreen.setEditable(false);
        displayScreen.setText(MESSAGE_WELCOME);
        displayScreen.setBackground(display.getSystemColor(SWT.COLOR_BLACK));
        displayScreen.setForeground(display.getSystemColor(SWT.COLOR_GREEN));
	}
	
	private static void setUpTaskPaneLabel(){

        taskPane = new Label(shell, SWT.NULL);
        taskPane.setText(TASK_PANE_LABEL);
        gridData = new GridData(GridData.HORIZONTAL_ALIGN_CENTER);
        taskPane.setLayoutData(gridData);
	}
	
	private static void setUpUpcomingTasks(){
        upcomingTasks = new Text(shell, SWT.MULTI | SWT.READ_ONLY
                | SWT.BORDER);
        gridData = new GridData(GridData.FILL_BOTH);
        upcomingTasks.setBackground(display.getSystemColor(SWT.COLOR_BLACK));
        upcomingTasks.setForeground(display.getSystemColor(SWT.COLOR_GREEN));
        upcomingTasks.setLayoutData(gridData);
        upcomingTasks.setEditable(false);
        
	}
	
	private static void setUpCommandLine(){
        commandLine = new Text(shell, SWT.SINGLE | SWT.BORDER_SOLID);
        gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
        gridData.horizontalSpan = 2;
        commandLine.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
        commandLine.setLayoutData(gridData);
	}


}
