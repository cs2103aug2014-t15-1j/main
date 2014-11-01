package gui;

import java.io.InputStream;

import org.eclipse.jface.resource.FontRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class HelpDialog extends Dialog {

    Image image;

    public HelpDialog(Shell parent) {
        super(parent);
        open();
    }

    public void open() {
        Shell shell = new Shell(getParent(), SWT.CLOSE | SWT.APPLICATION_MODAL);

        shell.setLayout(new GridLayout());

        createContents(shell);
        addCloseListener(shell);

        Rectangle mainProgramSize = Display.getCurrent().getPrimaryMonitor()
                .getBounds();
        shell.setLocation((mainProgramSize.width - shell.getBounds().width) / 2,
                          (mainProgramSize.height - shell.getBounds().height) / 2);

        shell.pack();
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

        FontRegistry fontRegistry = new FontRegistry(parent.getDisplay());
        FontData font = new FontData("Arial", 11, SWT.NORMAL | SWT.BOLD);
        FontData[] fontData = new FontData[] { font };
        fontRegistry.put("label", fontData);

        Label label = new Label(parent, SWT.NONE);
        label.setText("To close press ESC");
        label.setFont(fontRegistry.get("label"));

        GridData data = new GridData(GridData.FILL_BOTH);
        label.setData(data);

        InputStream stream = getClass()
                .getResourceAsStream("/resource/Helpsheet.jpg");
        ImageData imageData = new ImageData(stream);
        image = new Image(parent.getDisplay(), imageData);

        Label aLabel = new Label(parent, SWT.NONE);
        aLabel.setImage(image);
        aLabel.setLayoutData(new GridData(GridData.FILL_BOTH));
    }

    private void addCloseListener(Shell shell) {
        Control[] controls = shell.getChildren();

        for (int index = 0; index < controls.length; index++) {
            controls[index].addKeyListener(new KeyAdapter() {

                public void keyReleased(KeyEvent event) {

                    if (event.character == SWT.ESC) {
                        shell.close();
                    }
                }
            });
        }
    }
}
