//@author A0118846W
package gui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

/**
 * TableComposite contains a tabFolder containing all the tables used in the
 * application.
 */
public class TableComposite extends Composite {

    private static final int NUM_TAB_LABELS = 6; // Update this if adding new
                                                 // labels
    private static final String All_TABLE_TAB_LABEL = "All";
    private static final String TODAY_TABLE_TAB_LABEL = "Today";
    private static final String TOMORROW_TABLE_TAB_LABEL = "Tomorrow";
    private static final String UPCOMING_TABLE_TAB_LABEL = "Upcoming";
    private static final String FLOATING_TABLE_TAB_LABEL = "Someday";
    private static final String RESULT_TABLE_TAB_LABEL = "Result";

    private static CTabFolder tabFolder;
    private static List<TableViewer> tables = new ArrayList<TableViewer>();

    /**
     * Creates the TableComposite and its children
     * 
     * @param parent
     *            Composite where the TableComposite is located in
     * @param style
     *            Style that the TableComposite should follow
     */
    //@author A0118846W
    public TableComposite(Composite parent, int style) {
        super(parent, SWT.NONE);
        setLayout();
        createContents(parent);
    }

    /**
     * Returns the tab folder used to contain the tables
     */
    //@author A0118846W
    public static CTabFolder getTabFolder() {
        assert (tabFolder != null);
        return tabFolder;
    }

    /**
     * Returns the list of tableViewers that was used to create the tables
     */
    //@author A0118846W
    public static List<TableViewer> getTables() {
        return tables;
    }

    /**
     * Creates the layout of the composite which contains the table
     */
    //@author A0118846W
    private void setLayout() {
        GridLayout layout = new GridLayout();
        this.setLayout(layout);
        this.setLayoutData(new GridData(GridData.FILL_BOTH));

    }

    /**
     * Creates the tab folder containing all the tables and adds all the tables
     * to it
     */
    //@author A0118846W
    private void createContents(Composite parent) {
        tabFolder = new CTabFolder(this, SWT.NONE);
        tabFolder.setLayoutData(new GridData(GridData.FILL_BOTH));
        tabFolder.setBackground(TableColours.getTabFolderColour());

        createTabItem(tabFolder, All_TABLE_TAB_LABEL);
        createTabItem(tabFolder, TODAY_TABLE_TAB_LABEL);
        createTabItem(tabFolder, TOMORROW_TABLE_TAB_LABEL);
        createTabItem(tabFolder, UPCOMING_TABLE_TAB_LABEL);
        createTabItem(tabFolder, FLOATING_TABLE_TAB_LABEL);
        createTabItem(tabFolder, RESULT_TABLE_TAB_LABEL);

        addListener(tabFolder);
        tabFolder.setSelection(0);
    }
    
    /**
     * Creates a tab item within the tab folder.
     * @param folder folder containing all tabs
     * @param tabLabel String containing tab name
     */
    //@author A0118846
    private void createTabItem(CTabFolder folder, String tabLabel) {
        CTabItem item = new CTabItem(tabFolder, SWT.NONE);
        item.setText(tabLabel);
        TableViewer tableUI = createTable();
        item.setControl(tableUI.getTable());
        

    }
    
    /**
     * Creates  a Table
     */
    //@author A0118846W
    private TableViewer createTable() {
        TableViewer tableUI = new TableUI(tabFolder).getTableViewer();
        tables.add(tableUI);
        return tableUI;
    }

    //@author A116208N
    /**
     * Adds a listener to the tab folder containing the tables. Thus the user
     * can traverse between tabs by pressing F2 and F3
     */
    private void addListener(final CTabFolder folder) {
        Display display = folder.getDisplay();
        display.addFilter(SWT.KeyDown, new Listener() {

            public void handleEvent(Event event) {
                int index = folder.getSelectionIndex();
                if (event.keyCode == SWT.F3) {
                    folder.setSelection((index + 1) % NUM_TAB_LABELS);
                } else if (event.keyCode == SWT.F2) {
                    folder.setSelection((index + NUM_TAB_LABELS - 1) %
                                        NUM_TAB_LABELS);
                }
            }
        });
    }
}
