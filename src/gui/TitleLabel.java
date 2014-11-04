package gui;

import org.eclipse.jface.resource.FontRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

/**
 * This Composite contains the title of the program.
 * @author Sharon
 *
 */
public class TitleLabel extends Composite {

    private static final String LABEL_TEXT = "Haystack";
    private static final int NUM_COLS_SCREEN = 2;
    private FontRegistry registry;

    /**
     * Creates an instance of titleLabel
     * @param parent Composite where TitleLabel is located
     * @param style  Style that TitleLabel has
     */
    public TitleLabel(Composite parent, int style) {
        super(parent, style);
        setLayout(parent);
        createContents(parent);
    }

    private void setLayout(Composite parent) {
        GridLayout layout = new GridLayout();
        layout.numColumns = NUM_COLS_SCREEN;
        this.setLayout(layout);
        GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
        gridData.horizontalSpan = NUM_COLS_SCREEN;
        this.setLayoutData(gridData);
    }

    private void createContents(Composite parent) {
        getFontRegistry();
        Label title = new Label(this, SWT.SINGLE | SWT.CENTER);
        title.setText(LABEL_TEXT);
        title.setFont(registry.get("haystack title"));
        Color color = parent.getDisplay().getSystemColor(SWT.COLOR_BLACK);
        title.setForeground(color);

    }

    private void getFontRegistry() {
        registry = Fonts.getRegistry();
    }

}
