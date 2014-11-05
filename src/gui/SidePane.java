package gui;

import logic.Processor;

import org.eclipse.jface.resource.FontRegistry;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Display;

/**
 * This Composite contains the user interfaces that show additional information about the tasks carried out. 
 * The children of this Composite include TimeUI, Calendar, UpcomingTaskList, FloatingTaskList
 */
//@author A0118846W
public class SidePane extends Composite {

    private static final int MIN_HEIGHT_SIDE_PANE = 500;
    private TimeUI timeLabel;
    
    /**
     * Creates the SidePane Composite and its children
     * @param parent Composite where SidePane composite is located in
     * @param style Style that SidePane should have
     */
    public SidePane(Composite parent, int style) {
        super(parent, style);
        setDimensions(parent);
        setLayout(parent);
        createChildren();
    }

    private void setDimensions(Composite parent) {
        int shellWidth = parent.getSize().x;
        this.setSize(shellWidth / 3, MIN_HEIGHT_SIDE_PANE);
    }

    private void setLayout(Composite parent) {
        GridLayout sidePaneLayout = new GridLayout();
        sidePaneLayout.numColumns = 1;
        this.setLayout(sidePaneLayout);
        this.setLayoutData(new GridData(GridData.FILL_VERTICAL));
    }

    private void createChildren() {
        TimeUI.getInstance(this);
        addCalendar();
        UpcomingTaskList.getInstance(this);
        FloatingTaskList.getInstance(this);
    }

    private void addCalendar() {
        new DateTime(this, SWT.CALENDAR);
    }
    

}
