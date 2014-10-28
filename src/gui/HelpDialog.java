package gui;

import org.eclipse.jface.resource.FontRegistry;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.FontData;
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
        Shell shell = new Shell(getParent(), SWT.CLOSE);

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

        FontRegistry fontRegistry = new FontRegistry(parent.getDisplay());
        FontData font = new FontData("Arial", 11, SWT.NORMAL | SWT.BOLD);
        FontData[] fontData = new FontData[] { font };
        fontRegistry.put("label", fontData);

        Label label = new Label(parent, SWT.NONE);
        label.setText("To close press ESC");
        label.setFont(fontRegistry.get("label"));

        GridData data = new GridData(GridData.FILL_BOTH);
        label.setData(data);
        ImageRegistry imageRegistry = new ImageRegistry(parent.getDisplay());

        ImageDescriptor id = ImageDescriptor
                .createFromFile(HelpDialog.class, "/resource/Helpsheet.jpg");
        imageRegistry.put("HelpSheet", id);
        Label aLabel = new Label(parent, SWT.NONE);
        aLabel.setImage(imageRegistry.get("HelpSheet"));
        aLabel.setLayoutData(new GridData(GridData.FILL_BOTH));
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
