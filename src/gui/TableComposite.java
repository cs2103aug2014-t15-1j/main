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
    
    private static CTabFolder tabFolder;
    private FontRegistry registry;
    private static List<TableViewer> tables = new ArrayList<TableViewer>();

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
    
    public static CTabFolder getTabFolder(){
        return tabFolder;
    }
    
    public static List<TableViewer> getTables(){
        return tables;
    }
    
    private void setColour(Composite parent) {
        Color white = parent.getDisplay().getSystemColor(SWT.COLOR_WHITE);
        this.setBackground(white);
    }

    private void createContents(Composite parent) {
        formatRegistry(parent);
        tabFolder = new CTabFolder(this, SWT.BORDER);
        tabFolder.setLayoutData(new GridData(GridData.FILL_BOTH));
        
        CTabItem toDoTable = new CTabItem(tabFolder, SWT.NONE);
        toDoTable.setText(TODO_TABLE_TAB_LABEL);
        TableViewer toDoTableUI =  new TableUI(tabFolder).getTableViewer();
        tables.add(toDoTableUI);
        toDoTable.setControl(toDoTableUI.getTable());
        
        CTabItem todayTable = new CTabItem(tabFolder, SWT.NONE);
        todayTable.setText(TODAY_TABLE_TAB_LABEL);
        TableViewer todayTableUI =  new TableUI(tabFolder).getTableViewer();
        tables.add(todayTableUI);
        todayTable.setControl(todayTableUI.getTable());
        
        CTabItem tomorrowTable = new CTabItem(tabFolder, SWT.NONE);
        tomorrowTable.setText(TOMORROW_TABLE_TAB_LABEL);
        TableViewer tomorrowTableUI =  new TableUI(tabFolder).getTableViewer();
        tables.add(tomorrowTableUI);
        tomorrowTable.setControl(tomorrowTableUI.getTable());

        CTabItem upcomingTable = new CTabItem(tabFolder, SWT.NONE);
        upcomingTable.setText(UPCOMING_TABLE_TAB_LABEL);
        TableViewer upcomingTableUI =  new TableUI(tabFolder).getTableViewer();
        tables.add(upcomingTableUI);
        upcomingTable.setControl(upcomingTableUI.getTable());
        
        CTabItem floatingTable = new CTabItem(tabFolder, SWT.NONE);
        floatingTable.setText(FLOATING_TABLE_TAB_LABEL);
        TableViewer floatingTableUI =  new TableUI(tabFolder).getTableViewer();
        tables.add(floatingTableUI);
        floatingTable.setControl(floatingTableUI.getTable());
        
        CTabItem doneTable = new CTabItem(tabFolder, SWT.NONE);
        doneTable.setText(DONE_TABLE_TAB_LABEL);
        TableViewer doneTableUI =  new TableUI(tabFolder).getTableViewer();
        tables.add(doneTableUI);
        floatingTable.setControl(doneTableUI.getTable());
        
        CTabItem blockTable = new CTabItem(tabFolder, SWT.NONE);
        blockTable.setText(BLOCK_TABLE_TAB_LABEL);
        TableViewer blockTableUI =  new TableUI(tabFolder).getTableViewer();
        tables.add(blockTableUI);
        blockTable.setControl(blockTableUI.getTable());
        
        addListener(tabFolder);
        tabFolder.setSelection(0);
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
