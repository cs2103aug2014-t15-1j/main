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

public class TableComposite extends Composite {

    private static final String TODAY_TABLE_TAB_LABEL = "Tasks";
    private static final String UPCOMING_TABLE_TAB_LABEL = "Upcoming";
    private static final String TOMORROW_TABLE_TAB_LABEL = "Tomorrow";
    private static final String FLOATING_TABLE_TAB_LABEL = "Someday";
    
    private static CTabFolder tabFolder;
    private FontRegistry registry;
    private static List<TableViewer> tables = new ArrayList<TableViewer>();
    
    public TableComposite(Composite parent, int style) {
        super(parent, style);
        setLayout();
        createContents(parent);
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

        CTabItem todayTable = new CTabItem(tabFolder, SWT.BORDER);
        todayTable.setText(TODAY_TABLE_TAB_LABEL);
        TableViewer todayTableUI =  new TableUI(tabFolder).getTableViewer();
        tables.add(todayTableUI);
        todayTable.setControl(todayTableUI.getTable());
        
        CTabItem tomorrowTable = new CTabItem(tabFolder, SWT.BORDER);
        tomorrowTable.setText(TOMORROW_TABLE_TAB_LABEL);
        TableViewer tomorrowTableUI =  new TableUI(tabFolder).getTableViewer();
        tables.add(tomorrowTableUI);
        tomorrowTable.setControl(tomorrowTableUI.getTable());

        CTabItem upcomingTable = new CTabItem(tabFolder, SWT.BORDER);
        upcomingTable.setText(UPCOMING_TABLE_TAB_LABEL);
        TableViewer upcomingTableUI =  new TableUI(tabFolder).getTableViewer();
        tables.add(upcomingTableUI);
        upcomingTable.setControl(upcomingTableUI.getTable());
        
        CTabItem floatingTable = new CTabItem(tabFolder, SWT.BORDER);
        floatingTable.setText(FLOATING_TABLE_TAB_LABEL);
        TableViewer floatingTableUI =  new TableUI(tabFolder).getTableViewer();
        tables.add(floatingTableUI);
        floatingTable.setControl(floatingTableUI.getTable());
        
    }

    private void formatRegistry(Composite parent) {
        registry = new FontRegistry(parent.getDisplay());

        FontData[] fontData = new FontData[] { new FontData("Arial", 10,
                SWT.BOLD | SWT.UNDERLINE_SINGLE) };
        registry.put("title", fontData);
    }
}
