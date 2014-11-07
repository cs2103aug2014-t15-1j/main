package gui;

import java.io.InputStream;

import org.eclipse.jface.resource.FontRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

/**
 * HelpDialog is the Dialog which contains an image showing all the commands the user can input
 */
//@author A0118846W
public class HelpDialog extends Dialog {
    
    private static final String CLOSE_LABEL_TEXT = "To close press ESC";
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
     * Opens the help dialog
     */
    public void open() {
        Shell shell = new Shell(getParent(), SWT.NO_TRIM );

        shell.setLayout(new GridLayout());

        createContents(shell);
        addCloseListener(shell);
        
        shell.pack();
        centerDialogInScreen(shell);
        helpShell = shell;
        
        shell.open();
        
        Display display = getParent().getDisplay();

        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        
        image.dispose();
    }

   
    private void createContents(Composite parent) {
//        createCloseLabel(parent);
        drawHelpImage(parent);
    }
    
    
//    private void createCloseLabel(Composite parent) {
//        Label label = new Label(parent, SWT.NONE);
//        label.setText(CLOSE_LABEL_TEXT);
//        FontRegistry registry = Fonts.getRegistry();
//        label.setFont(registry.get("label"));
//
//        GridData data = new GridData(GridData.FILL_BOTH);
//        label.setData(data);
//    }

    private void drawHelpImage(Composite parent) {
        getHelpImage(parent);

        Label aLabel = new Label(parent, SWT.NONE);
        aLabel.setImage(image);
        aLabel.setLayoutData(new GridData(GridData.FILL_BOTH));
    }

    private void getHelpImage(Composite parent) {
        InputStream stream = getClass()
                .getResourceAsStream("/resource/Helpsheet.jpg");
        ImageData imageData = new ImageData(stream);
        image = new Image(parent.getDisplay(), imageData);
    }
    
    private void centerDialogInScreen(Shell shell) {
        Rectangle mainProgramSize = Display.getCurrent().getPrimaryMonitor()
                .getBounds();
        shell.setLocation((mainProgramSize.width - shell.getBounds().width) / 2,
                          (mainProgramSize.height - shell.getBounds().height) / 2);
    }
    
    private void addCloseListener(final Shell shell) {
//        Control[] controls = shell.getChildren();
//
//        for (int index = 0; index < controls.length; index++) {
//            controls[index].addKeyListener(new KeyAdapter() {
//
//                public void keyPressed(KeyEvent event) {
//                    if (event.character == SWT.ESC) {
//                        shell.close();
//                    }
//                }
//            });
//        }
        shell.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent event) {
                shell.dispose();
            }
        });
        
    }
    
    public static Shell getHelpDialog() {
        return helpShell;
    }
}
