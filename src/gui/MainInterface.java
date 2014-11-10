//@author A0118846W
package gui;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

/**
 * The Composite that contains the programs main user interfaces. The children of this composite include 
 * the TableComposite and the FeedBackAndInput Composite
 */
public class MainInterface extends Composite {

    private static final int NUM_COLS_COMPOSITE = 1;

    /**
     * Creates the MainInterface Composite and its children composites
     * @param parent the Composite where this composite is to be located in
     * @param style  the style that the composite should follow
     */
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
