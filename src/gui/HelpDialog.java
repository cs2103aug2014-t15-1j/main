package gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
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
        Shell shell = new Shell(getParent(), getStyle());

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
        closeDialogLabel.setText("To close key ALT + SPACE");

        // get Image

    }

    private void addCloseListener(Shell shell) {
        shell.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent event) {
                if (((event.stateMask & SWT.ESC) == SWT.ESC)) {
                    shell.close();
                }
            }

        });

    }

}
