package graphics;

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
    private static final String ASK_CONFIRM_DELETE = "Are you sure you want to wipe file? This is irreversible";
    private static final String INVALID_INPUT = "Invalid Input. Type yes or no.";
    private static final String SUCCESSFUL_DELETE_ALL = "Erased all data!";
    private static final String UNSUCCESSFUL_DELETE_ALL = "Did not delete anything";
    

    private static final String CONFIRM = "yes";
    private static final String NO_CONFIRM = "no";
    
	private static String CODE_EXIT = "exit";
	private static boolean askConfrim = false;
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
            @Override
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
            @Override
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
			@Override
            public void handleEvent(Event event) {

				final Label feedback = screen.getFeedBack();
				String input = commandLine.getText();
				String output = "";
				if(askConfrim){
				    while(!isValidInput(input)){
				        feedback.setText(INVALID_INPUT);
				    }
				    if(ResultGenerator.processDelete(input)){
				        feedback.setText(SUCCESSFUL_DELETE_ALL);
				    }else{
				        feedback.setText(UNSUCCESSFUL_DELETE_ALL);
				    }
				    commandLine.setText("");
				    askConfrim = false;
				    
				    
				}else{
				//try{
					output = ResultGenerator.sendInput(input);
				
					//output = commandLine.getText();
					if (output.equals(CODE_EXIT)) {
					    exitProgram();
					} else if (output.equals(ASK_CONFIRM_DELETE)){
					    feedback.setText(ASK_CONFIRM_DELETE);
					    askConfrim = true;
					    commandLine.setText("");
					    
					}else {
					    feedback.setText(output);
                        commandLine.setText("");
					    
				//	}
				//}catch (Exception error) {
			//	error.getMessage();
				//System.out.println("I am an exception error in main screen.java");
			}
				}
			}

            private boolean isValidInput(String input) {
                String inputLowerCase = input.toLowerCase();
                if(inputLowerCase.equals(CONFIRM) || inputLowerCase.equals("y")){
                    return true;
                } 
                
                if(inputLowerCase.equals(NO_CONFIRM) || inputLowerCase.equals("n")){
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
