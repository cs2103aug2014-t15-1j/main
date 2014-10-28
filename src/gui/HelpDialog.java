package gui;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class HelpDialog extends Dialog {

    private Image image;
    private static HelpDialog help;

    public HelpDialog(Shell parent) {
        super(parent);
        open();
    }

    public void open() {
        Shell shell = new Shell(getParent(), SWT.CLOSE | SWT.RESIZE);

        shell.setLayout(new RowLayout(SWT.VERTICAL));
        createContents(shell);
        addCloseListener(shell);

        shell.pack();
        shell.open();
        Display display = getParent().getDisplay();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();

            }
        }
    }

    private void createContents(Composite parent) {
        Label closeDialogLabel = new Label(parent, SWT.NONE);
        closeDialogLabel.setText("To close press ESC");

        ImageRegistry imageRegistry = new ImageRegistry(parent.getDisplay());

        ImageDescriptor id = ImageDescriptor
                .createFromFile(HelpDialog.class, "/resource/Helpsheet.jpg");
        imageRegistry.put("help", id);

        Image image = imageRegistry.get("help");
        Label helpImage = new Label(parent, SWT.NONE);
        helpImage.setImage(image);
    }

    private void addCloseListener(Shell shell) {
        Control[] widgets = shell.getChildren();

        for (int i = 0; i < widgets.length; i++) {
            widgets[i].addKeyListener(new KeyAdapter() {

                public void keyReleased(KeyEvent event) {

                    if (event.character == SWT.ESC) {
                        System.out.println("closing");
                        shell.close();
                    }

                }

            });

        }
    }

}
