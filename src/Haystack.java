

import gui.Images;
import gui.MainInterface;
import gui.ResultGenerator;
import gui.SidePane;
import gui.TitleLabel;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;

public class HayStack {

    private static final int NUM_COLS_SCREEN = 2;
    private static final String PROGRAM_NAME = "HayStack";
    private static int SHELL_WIDTH = 1200;
    private static int SHELL_HEIGTH = 860;

    public static void main(String[] args) {
        Display display = new Display();
        Shell shell = new Shell(display);

        new Images(shell);
        configureShell(shell);
        createContents(shell);
        initialiseProgram();

        shell.pack();
        shell.open();

        while (!shell.isDisposed()) {
            if (!display.readAndDispatch())
                display.sleep();
        }

        display.dispose();
        Images.disposeAllImages();
    }

    private static void initialiseProgram() {
        ResultGenerator resultGen = ResultGenerator.getInstance();
        resultGen.start();
    }

    private static void configureShell(Shell shell) {
        shell.setSize(SHELL_WIDTH, SHELL_HEIGTH);
        setBackGround(shell);
        setPosition(shell);
        setLayout(shell);
    }

    private static void createContents(Shell shell) {
        new TitleLabel(shell, shell.getStyle());
        new MainInterface(shell, shell.getStyle());
        new SidePane(shell, shell.getStyle());
    }

    private static void setBackGround(Shell parent) {
        Display display = parent.getDisplay();

        Image background = new Image(display, HayStack.class.getClassLoader()
                .getResourceAsStream("resource/mainbg.png"));
        parent.setImage(new Image(display, HayStack.class.getClassLoader()
                .getResourceAsStream("resource/Icon.gif")));

        ImageData imageData = background.getImageData();
        imageData = imageData.scaledTo(parent.getSize().x, parent.getSize().y);
        parent.setBackgroundImage(background);

        parent.setBackgroundMode(SWT.INHERIT_FORCE);
    }

    /**
     * Sets the position of the shell to be in the center of the screen
     * 
     * @param shell
     *            shell window where the main application runs
     */
    private static void setPosition(Shell shell) {

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
