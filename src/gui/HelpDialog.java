package gui;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class HelpDialog extends Dialog {

    public HelpDialog(Shell parent) {
        super(parent);
        open();
    }

    public void open() {
        Shell shell = new Shell(getParent(), SWT.CLOSE | SWT.RESIZE);

        shell.setLayout(new GridLayout());
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

        GridData data = new GridData(GridData.FILL_BOTH);
        ImageRegistry imageRegistry = new ImageRegistry(parent.getDisplay());

        ImageDescriptor id = ImageDescriptor
                .createFromFile(HelpDialog.class, "/resource/Helpsheet.jpg");
        imageRegistry.put("HelpSheet", id);

        Image image = imageRegistry.get("help");

        Label helpImage = new Label(parent, SWT.NONE);
        helpImage.setImage(image);
        helpImage.setLayoutData(data);
    }

    private void addCloseListener(Shell shell) {
        Control[] controls = shell.getChildren();

        for (int index = 0; index < controls.length; index++) {
            controls[index].addKeyListener(new KeyAdapter() {

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
