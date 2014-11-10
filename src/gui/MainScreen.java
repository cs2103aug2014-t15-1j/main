package gui;


import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;

/**
 * This class creates and setUps the window for the Haystack application
 */
public class MainScreen {

    private static final int NUM_COLS_SCREEN = 2;
    private static final String PROGRAM_NAME = "Haystack";

    /**
     * Runs and initializes the application
     */
 // @author A0118846W
    public static void run() {
        Display display = new Display();
        Shell shell = new Shell(display, SWT.NO_TRIM);

        runProgram(shell);

        shell.open();

        while (!shell.isDisposed()) {
            if (!display.readAndDispatch())
                display.sleep();
        }
        
        disposeResources(display);
    }
    
    /**
     * Starts the application
     * @param shell window containing the application
     */
 // @author A0118846W
    public static void runProgram(Shell shell) {
        initialiseResources(shell);
        createDragControls(shell);
        configureShell(shell);
        createContents(shell);
        initialiseProgram();
        new ProcessUserInteraction();
    }
    
    /** 
     * Disposes all the resources used by the application
     */
 // @author A0118846W
    public static void disposeResources(Display display) {
        display.dispose();
        Images.disposeAllImages();
        TableColours.disposeAllColours();
    }
    
 // @author A0118846W
    private static void initialiseResources(Shell shell) {
        new Images(shell);
        new Fonts(shell);
        new TableColours(shell.getDisplay());
    }

    /**
     * Create drag controls along the shell's borders.
     * 
     * Code taken from <a href=
     * "http://stackoverflow.com/questions/23126313/removing-window-border-in-swt-ui-disables-re-positioning"
     * >Stack Overflow</a>
     */
    private static void createDragControls(final Shell shell) {
        Listener l = new Listener() {
            Point origin;

            public void handleEvent(Event e) {
                switch (e.type) {
                    case SWT.MouseDown:
                        origin = new Point(e.x, e.y);
                        break;
                    case SWT.MouseUp:
                        origin = null;
                        break;
                    case SWT.MouseMove:
                        if (origin != null) {
                            Point p = shell.getDisplay().map(shell, null, e.x,
                                                             e.y);
                            shell.setLocation(p.x - origin.x, p.y - origin.y);
                        }
                        break;
                }
            }
        };

        shell.addListener(SWT.MouseDown, l);
        shell.addListener(SWT.MouseUp, l);
        shell.addListener(SWT.MouseMove, l);
    }
    
 // @author A0118846W
    private static void initialiseProgram() {
        ResultGenerator resultGen = ResultGenerator.getInstance();
        resultGen.start();
    }

    private static void configureShell(Shell shell) {
        Display display = shell.getDisplay();
        Rectangle bounds = display.getBounds();
        int shellWidth = (int) (bounds.width * 0.8);
        int shellHeight = (int) (bounds.height * 0.8);

        shell.setSize(shellWidth, shellHeight);
        setBackGround(shell);
        setPositionToCenterOfScreen(shell);
        setLayout(shell);
    }
    
 // @author A0118846W
    private static void createContents(Shell shell) {
        new TitleLabel(shell, shell.getStyle());
        new SidePane(shell, shell.getStyle());
        new MainInterface(shell, shell.getStyle());
    }
    
    // @author A0118846W
    private static void setBackGround(Shell parent) {

        parent.setImage(Images.getRegistry().get("icon"));

        Image background = Images.getRegistry().get("main");
        ImageData imageData = background.getImageData();
        imageData = imageData.scaledTo(parent.getSize().x, parent.getSize().y);
        parent.setBackgroundImage(background);

        parent.setBackgroundMode(SWT.INHERIT_FORCE);
    }

    /**
     * Sets the position of the application in the center of the device's screen
     * 
     * @param shell
     *            the shell that is to be positioned
     */
    private static void setPositionToCenterOfScreen(Shell shell) {

        Display display = shell.getDisplay();
        Monitor primary = display.getPrimaryMonitor();
        Rectangle bounds = primary.getBounds();
        Rectangle rect = shell.getBounds();

        int x = bounds.x + (bounds.width - rect.width) / 2;
        int y = bounds.y + (bounds.height - rect.height) / 2;

        shell.setLocation(x, y);
    }

    /**
     * Sets they layout of the shell window. Its children are arranged based on
     * this layout
     * 
     * @param shell
     *            window where the main application runs
     */
    private static void setLayout(Shell shell) {

        GridLayout layout = new GridLayout();
        layout.numColumns = NUM_COLS_SCREEN;
        shell.setLayout(layout);
        shell.setText(PROGRAM_NAME);
    }

}
