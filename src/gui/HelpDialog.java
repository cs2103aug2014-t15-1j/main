package gui;

import java.io.InputStream;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

/**
 * HelpDialog is the Dialog which contains an image showing all the commands the user can input
 */
//@author A0118846W
public class HelpDialog extends Dialog {
    

    private Image image;
    private static Shell helpShell;
    
    /**
     * Creates the HelpDialoog and opens it
     * @param parent shell from which the Dialog is called
     */
    public HelpDialog(Shell parent) {
        super(parent);
    }
    
    /**
     * Returns the instance of shell object used to create the dialog
     * @return shell used to create dialog
     */
    public static Shell getShell() {
        return helpShell;
    }
    
    /**
     * Opens the help dialog
     */
    public void open() {
        Shell shell = new Shell(getParent(), SWT.NO_TRIM | SWT.APPLICATION_MODAL);

        shell.setLayout(new GridLayout());

        createContents(shell);
        addCloseListener(shell);
        
        shell.pack();
        centerDialogInScreen(shell);
        shell.open();
        
        Display display = getParent().getDisplay();

        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        
    }

   
    private void createContents(Composite parent) {
        drawHelpImage(parent);
    }

    private void drawHelpImage(Composite parent) {
        getHelpImage(parent);

        Label aLabel = new Label(parent, SWT.NONE);
        aLabel.setImage(image);
        aLabel.setLayoutData(new GridData(GridData.FILL_BOTH));
    }

    private void getHelpImage(Composite parent) {
        image = Images.getRegistry().get("help");
        
    }
    
    private void centerDialogInScreen(Shell shell) {
        Rectangle mainProgramSize = Display.getCurrent().getPrimaryMonitor()
                .getBounds();
        shell.setLocation((mainProgramSize.width - shell.getBounds().width) / 2,
                          (mainProgramSize.height - shell.getBounds().height) / 2);
    }
    
    private void addCloseListener(final Shell shell) {
        shell.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent event) {
                shell.close();
                shell.dispose();
            }
        });
        
    }
    
}
