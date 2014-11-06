package gui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.resource.FontRegistry;
import org.eclipse.jface.viewers.TableViewer;
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

/**
 * TableComposite contains a tabFolder containing all the tables used in the application.
 */
//@author A0118846W
public class TableComposite extends Composite {
// Today, tmr, someday, upcoming, todo, block, done
    private static final int NUM_TAB_LABELS = 7;
    private static final String TODO_TABLE_TAB_LABEL = "To Do";
    private static final String TODAY_TABLE_TAB_LABEL = "Today";
    private static final String TOMORROW_TABLE_TAB_LABEL = "Tomorrow";
    private static final String UPCOMING_TABLE_TAB_LABEL = "Upcoming";
    private static final String FLOATING_TABLE_TAB_LABEL = "Someday";
    private static final String BLOCK_TABLE_TAB_LABEL = "Blocked";
    private static final String DONE_TABLE_TAB_LABEL = "Done";
    private static final String SEARCH_TABLE_TAB_LABEL = "Search";
    
    private static CTabFolder tabFolder;
    private FontRegistry registry;
    private static List<TableViewer> tables = new ArrayList<TableViewer>();

/**
 * Creates the TableComposite and its children
 * @param parent Composite where the TableComposite is located in
 * @param style Style that the TableComposite should follow
 */
    public TableComposite(Composite parent, int style) {
        super(parent, SWT.NONE);
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

    }
    
    public static CTabFolder getTabFolder(){
        return tabFolder;
    }
    
    public static List<TableViewer> getTables(){
        return tables;
    }
 
    private void createContents(Composite parent) {
        formatRegistry(parent);
        tabFolder = new CTabFolder(this, SWT.NONE);
        tabFolder.setLayoutData(new GridData(GridData.FILL_BOTH));
        tabFolder.setBackground(Colours.getTabFolderColour());

        createTable(tabFolder, TODO_TABLE_TAB_LABEL);
        createTable(tabFolder, TODAY_TABLE_TAB_LABEL);
        createTable(tabFolder, TOMORROW_TABLE_TAB_LABEL);
        createTable(tabFolder, UPCOMING_TABLE_TAB_LABEL);
        createTable(tabFolder, FLOATING_TABLE_TAB_LABEL);
        createTable(tabFolder, DONE_TABLE_TAB_LABEL);
        createTable(tabFolder, BLOCK_TABLE_TAB_LABEL);
        createTable(tabFolder, SEARCH_TABLE_TAB_LABEL);
        
        addListener(tabFolder);
        tabFolder.setSelection(0);
    }

    private void createTable(CTabFolder folder, String tabLabel) {
        CTabItem item = new CTabItem(tabFolder, SWT.NONE);
        item.setText(tabLabel);
        TableViewer tableUI =  new TableUI(tabFolder).getTableViewer();
        tables.add(tableUI);
        item.setControl(tableUI.getTable());
        
    }

    private void formatRegistry(Composite parent) {
        registry = new FontRegistry(parent.getDisplay());

        FontData[] fontData = new FontData[] { new FontData("Arial", 10,
                SWT.BOLD | SWT.UNDERLINE_SINGLE) };
        registry.put("title", fontData);
    }
    
    private void addListener(final CTabFolder folder) {
        Display display = folder.getDisplay();
        display.addFilter(SWT.KeyDown, new Listener() {

            public void handleEvent(Event event) {
                int index = folder.getSelectionIndex();
                if (event.keyCode == SWT.F3) {
                    folder.setSelection((index + 1 ) % NUM_TAB_LABELS);
                } else if (event.keyCode == SWT.F2) {
                    folder.setSelection((index + NUM_TAB_LABELS - 1 ) % NUM_TAB_LABELS);
                }
            }
        });
    }
}
