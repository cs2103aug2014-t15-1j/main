package gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;

/**
 * This Composite contains the user interfaces that show additional information
 * about the tasks carried out. The controls include in this Composite include
 * TimeUI and the Calendar
 */
public class SidePane extends Composite {

    private static final int MIN_HEIGHT_SIDE_PANE = 500;

    /**
     * Creates the SidePane Composite and its children
     * 
     * @param parent
     *            Composite where SidePane composite is located in
     * @param style
     *            Style that SidePane should have
     */
    // @author A0118846W
    public SidePane(Composite parent, int style) {
        super(parent, style);
        setDimensions(parent);
        setLayout(parent);
        createChildren();
    }

    
    private void setDimensions(Composite parent) {
        int shellWidth = parent.getSize().x;
        this.setSize((int) (shellWidth * 0.8), MIN_HEIGHT_SIDE_PANE);
    }

    // @author A0118846W
    private void setLayout(Composite parent) {
        GridLayout sidePaneLayout = new GridLayout();
        sidePaneLayout.numColumns = 1;
        this.setLayout(sidePaneLayout);
        this.setLayoutData(new GridData(GridData.FILL_VERTICAL));
    }

    // @author A0118846W
    private void createChildren() {
        addCalendar();
        new TimeUI(this);
    }

    // @author A0118846W
    private void addCalendar() {
        new DateTime(this, SWT.CALENDAR);

    }

}
