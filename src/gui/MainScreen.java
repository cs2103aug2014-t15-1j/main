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
//@author A0118846W
public class MainScreen {

    private static final int NUM_COLS_SCREEN = 2;
    private static final String PROGRAM_NAME = "Haystack";
    private static int SHELL_WIDTH = 1200;
    private static int SHELL_HEIGTH = 860;
    
/**
 * Runs and initializes the application
 */
    public static void run() {
        Display display = new Display();
        Shell shell = new Shell(display, SWT.NO_TRIM);
        createDragControls(shell);

        runProgram(shell);

        shell.open();

        while (!shell.isDisposed()) {
            if (!display.readAndDispatch())
                display.sleep();
        }

        display.dispose();
        Images.disposeAllImages();
    }
    
    /** 
     * Create drag controls along the shell's borders.
     * 
     * Code taken from
     * <a href="http://stackoverflow.com/questions/23126313/removing-window-border-in-swt-ui-disables-re-positioning">Stack Overflow</a>
     */
    private static void createDragControls(final Shell shell) {
        Listener l = new Listener()
        {
            Point origin;

            public void handleEvent(Event e)
            {
                switch (e.type)
                {
                    case SWT.MouseDown:
                        origin = new Point(e.x, e.y);
                        break;
                    case SWT.MouseUp:
                        origin = null;
                        break;
                    case SWT.MouseMove:
                        if (origin != null)
                        {
                            Point p = shell.getDisplay().map(shell, null, e.x, e.y);
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

public static void runProgram(Shell shell) {
    Images images = new Images(shell);
    new Fonts(shell);
    configureShell(shell, images);
    createContents(shell);
    initialiseProgram();
    new ProcessUserInteraction();
}

    private static void initialiseProgram() {
        ResultGenerator resultGen = ResultGenerator.getInstance();
        resultGen.start();
    }

    private static void configureShell(Shell shell, Images images) {
        Display display = shell.getDisplay();
        Rectangle bounds = display.getBounds();
        int shellWidth = (int) (bounds.width * 0.8);
        int shellHeight = (int) (bounds.height * 0.8);
        
        shell.setSize(shellWidth, shellHeight);
        setBackGround(shell, images);
        setPositionToCenterOfScreen(shell);
        setLayout(shell);
    }

    private static void createContents(Shell shell) {
        new TitleLabel(shell, shell.getStyle());
        new SidePane(shell, shell.getStyle());
        new MainInterface(shell, shell.getStyle());
    }

    private static void setBackGround(Shell parent, Images images) {
        Display display = parent.getDisplay();

        parent.setImage(new Image(display, MainScreen.class.getClassLoader()
                .getResourceAsStream("resource/Icon.gif")));

        Image background = Images.getRegistry().get("main");
       ImageData imageData = background.getImageData();
        imageData = imageData.scaledTo(parent.getSize().x, parent.getSize().y);
        parent.setBackgroundImage(background);

        parent.setBackgroundMode(SWT.INHERIT_FORCE);
    }

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
