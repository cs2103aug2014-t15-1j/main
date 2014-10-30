package gui;

import org.eclipse.jface.resource.FontRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class TitleLabel extends Composite {

    private static final String LABEL_TEXT = "HayStack";
    private static final int NUM_COLS_SCREEN = 2;
    private FontRegistry registry;

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
        formatRegistry(parent);
        Label title = new Label(this, SWT.SINGLE | SWT.CENTER);
        title.setText(LABEL_TEXT);
        title.setFont(registry.get("haystack title"));
        Color color = parent.getDisplay().getSystemColor(SWT.COLOR_BLACK);
        title.setForeground(color);

    }

    private void formatRegistry(Composite parent) {
        registry = new FontRegistry(parent.getDisplay());

        FontData[] fontData = new FontData[] { new FontData("Impact", 13,
                SWT.NORMAL) };
        registry.put("haystack title", fontData);
    }

}
