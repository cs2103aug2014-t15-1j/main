package gui;

import org.eclipse.jface.resource.FontRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;

/**
 * TableComposite contains a tabFolder containing all the tables used in the application.
 */
//@author A0118846W
public class TableComposite extends Composite {

    private static final String TASK_TABLE_TAB_LABEL = "Tasks";
    private static final String DATE_TABLE_TAB_LABEL = "Blocked Dates";
    
    private static CTabFolder tabFolder;
    
    /**
     * Creates the TableComposite and its children
     * @param parent Composite where the TableComposite is located in
     * @param style Style that the TableComposite should follow
     */
    public TableComposite(Composite parent, int style) {
        super(parent, style);
        setLayout();
        createContents(parent);
    }
    
    /**
     * Returns the tabFolder used to create the tables
     */
    public static CTabFolder  getFolder(){
        assert(tabFolder!=null);
        return tabFolder;
    }

    private void setLayout() {
        GridLayout layout = new GridLayout();
        this.setLayout(layout);
        this.setLayoutData(new GridData(GridData.FILL_BOTH));

        setColour(this);
    }

    private void setColour(Composite parent) {
        Color white = parent.getDisplay().getSystemColor(SWT.COLOR_WHITE);
        this.setBackground(white);
    }

    private void createContents(Composite parent) {
        
        tabFolder = new CTabFolder(this, SWT.BORDER);
        tabFolder.setLayoutData(new GridData(GridData.FILL_BOTH));

        createTaskTableTab(tabFolder);

        createDateTableTab(tabFolder);
    }

    private void createTaskTableTab(CTabFolder tabFolder) {
        CTabItem taskTable = new CTabItem(tabFolder, SWT.BORDER);
        taskTable.setText(TASK_TABLE_TAB_LABEL);
        Table taskTableUI = TaskTableUI.getInstance(tabFolder).getTableViewer()
                .getTable();
        taskTable.setControl(taskTableUI);
    }


    private void createDateTableTab(CTabFolder tabFolder) {
        CTabItem dateTable = new CTabItem(tabFolder, SWT.NONE);
        dateTable.setText(DATE_TABLE_TAB_LABEL);
        Table dateTableUI = DateTableUI.getInstance(tabFolder).getTableViewer()
                .getTable();
        dateTable.setControl(dateTableUI);
    }
}
