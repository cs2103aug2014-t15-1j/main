package gui;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

public class MainInterface extends Composite {

    private static final int NUM_COLS_COMPOSITE = 1;

    public MainInterface(Composite parent, int style) {
        super(parent, style);
        setLayout(parent);
        createContents(parent);
    }

    private void setLayout(Composite parent) {
        GridLayout layout = new GridLayout();
        layout.numColumns = NUM_COLS_COMPOSITE;
        this.setLayout(layout);
        GridData gridData = new GridData(GridData.FILL_BOTH);
        this.setLayoutData(gridData);

    }

    private void createContents(Composite parent) {
        new TableComposite(this, this.getStyle());
        new FeedbackAndInput(this, this.getStyle());
    }

}
