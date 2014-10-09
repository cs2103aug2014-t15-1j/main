package GUI;

import java.io.IOException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class MainScreen {
    // implement: task pane expansion, add upcoming tasks
	// increase size of commandLine if line does not fit into space
    // change font type, add scroll bar
    // add hayStack icon, ASCII picture
	// use mono-space font
	// one line display
	// gaps between number

	// Gets the new line character used by the user's system
	private static final String LINE_SEPARATOR = System.getProperty("line.separator");
	
	private static String CODE_CLEAR = " clear";
    private static String CODE_EXIT = " exit";

    public static void main(String[] args) {
        Display display = new Display();
        Shell shell = new Shell(display);
        
        SetUp setUpScreen = new SetUp(display, shell);
   
        readUserInput(setUpScreen);
   
        shell.pack();
        shell.open();
        
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch())
                display.sleep();
        }
        display.dispose();
    }

	private static void readUserInput(SetUp setUpScreen) {
		final Text displayScreen = setUpScreen.getDisplayScreen();
        final Text commandLine = setUpScreen.getCommandLine();
        
        commandLine.addListener(SWT.DefaultSelection, new Listener() {
            public void handleEvent(Event event) {
                String input = commandLine.getText();
                String output = "";
				try {
					output = ResultGenerator.sendInput(input);
				} catch (IOException error) {
					error.printStackTrace();
				}

                if (output.endsWith(CODE_CLEAR)) {
                    clearScreen(displayScreen);
                } else if (output.equals(CODE_EXIT)) {
                    Exit(output, displayScreen);
                } else
                    displayScreen.append(LINE_SEPARATOR + output);
                	commandLine.setText("");
            }
        });
	}

    private static void Exit(String output, Text displayScreen) {
        System.exit(0);
    }

    private static void clearScreen(Text displayScreen) {
        displayScreen.setText("");
    }

}
