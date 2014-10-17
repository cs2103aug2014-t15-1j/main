package GUI;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * Represents the main window of HayStack. Any user interaction with the program will be processed here.
 * Note: All methods in this class are static
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

	private static String CODE_EXIT = "exit";

	/**
	 * The entire HayStack program runs from this function. 
	 * It sets up the graphical user interface that the user interacts with any reads any input that the user enters.
	 * @param args
	 */
	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		
		// Things to do at startUp: Display all To do Tasks
		
		SetUp setUpScreen = SetUp.getInstance(shell);
		
		removeText(setUpScreen);
		readUserInput(setUpScreen);

		shell.pack();
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		
		display.dispose();
	}

	private static void removeText(SetUp screen) {
	    
	    final Text commandLine = screen.getCommandLine();
	    commandLine.addListener(SWT.KeyDown, new Listener() {
            public void handleEvent(Event event) {
                switch (event.type) {
                    case SWT.KeyDown :
                        commandLine.setText("");
                        commandLine.removeListener(SWT.KeyDown, this);
                        break;
                }
            }

        });
	    
	    commandLine.addListener(SWT.MouseDown, new Listener() {
            public void handleEvent(Event event) {
                switch (event.type) {
                    case SWT.MouseDown :
                        commandLine.setText("");
                        commandLine.removeListener(SWT.KeyDown, this);
                        break;
                }
            }

        });
    }

    private static void readUserInput(final SetUp screen) {

		final Text commandLine = screen.getCommandLine();

		commandLine.addListener(SWT.DefaultSelection, new Listener() {
			public void handleEvent(Event event) {

				final Label feedback;
				String input = commandLine.getText();
				String output = "";
				//try{
					output = ResultGenerator.sendInput(input);
					feedback = screen.getFeedBack();
					//output = commandLine.getText();
					if (!output.equals(CODE_EXIT)) {
	                       feedback.setText(output);
	                        commandLine.setText("");
					} else {
					    exitProgram();
				//	}
				//}catch (Exception error) {
			//	error.getMessage();
				//System.out.println("I am an exception error in main screen.java");
			}
			}
		});
	}

	private static void exitProgram() {
		System.exit(0);
	}
}
