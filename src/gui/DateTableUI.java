package gui;

import java.util.List;

import org.eclipse.jface.resource.FontRegistry;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import database.BlockDate;
/**
 * The DateTableUI is a table interface that is located in the a TabFolder. 
 * This user interface shows all the dates blocked by the user in a table.
 * The singleton pattern is used so that only one instance of the interface is used.
 */
//@author A0118846W
public class DateTableUI extends Composite {
    
    private static final String HEADER_NAME_ID = "Id";
    private static final String HEADER_DATE_START_DATE = "Start Date";
    private static final String HEADER_DATE_START_TIME = "Start Time";
    private static final String HEADER_DATE_END_DATE = "End Date";
    private static final String HEADER_DATE_END_TIME = "End Time";

    private static final int COL_WIDTH_DATE_TABLE = 175;
    private static final int COL_WIDTH_ID = 30;
    private static final int COL_WIDTH_DATE = 120;
    
    // Text to be shown when the element of a table is empty
    private static final String CELL_EMPTY = "<empty>";

    private static DateTableUI dateTableUI;
    private FontRegistry registry;
    private CTabFolder folder;
    private TableViewer tableViewer;

    /**
     * Creates an instance of the DateTableUI
     * @param parent TabFolder where the table is located in
     */
    private DateTableUI(CTabFolder parent) {
        super(parent, SWT.NULL);
        folder = parent;
        buildControls(parent);
    }
    
    /**
     * This method returns an instance of DateTableUI, it creates a new instance if it does not exist.
     * @param parent this is the TabFolder where the table is to be displayed in
     * @return DateTableUI instance
     */
    public static DateTableUI getInstance(CTabFolder parent) {
        if (dateTableUI == null) {
            dateTableUI = new DateTableUI(parent);
        }
        return dateTableUI;
    }
    
    /**
     * This method returns an instance of DateTableUI, this method should be called after DateTableUI has been created.
     * Otherwise, an assertion failure will occur
     * @return DateTableUI instance
     */
    public static DateTableUI getInstance() {
        assert (dateTableUI != null);
        return dateTableUI;
    }
    
    /**
     * Returns the TableViewer object used to create the table in DateTableUI.
     */
    public TableViewer getTableViewer() {
        return tableViewer;
    }
    
    /**
     * Updates the table interface shown to the user. This method should only be called after the class
     * has been initialized. Otherwise, an assertion failure will occur.
     * @param dates list of BlockDates objects which are to be shown to the user
     */
    public void update(List<BlockDate> dates) {
        assert(tableViewer!= null);
        assert(folder!=null);
        tableViewer.setInput(dates);
        folder.setSelection(1);
    }
    
    /**
     * Selects a specific BlockDate element in the table
     * @param dateToSelect the BlockDate object to be selected
     * @param dates the list of BlockDate elements that the table contains
     */
    public void setTableSection(BlockDate dateToSelect, List<BlockDate> dates) {
        int size = dates.size();
        int indexToSelect = 0;
        for (int index = 0; index < size; index++) {
            BlockDate currDate = dates.get(index);
            if (currDate.equals(dateToSelect)) {
                indexToSelect = index;
                break;
            }
        }

        tableViewer.getTable().setSelection(indexToSelect);
    }

    private void buildControls(Composite parent) {
        getFontRegistry();
        buildLabel(parent);
        buildTable(parent);
    }

    private void getFontRegistry() {
        registry = Fonts.getRegistry();
    }

    private void buildLabel(Composite parent) {
        StyledText tableTitle = new StyledText(parent, SWT.READ_ONLY);
        tableTitle.setText("Dates Blocked:");
        tableTitle.setEnabled(false);
        tableTitle.setFont(registry.get("title"));
    }

    private void buildTable(Composite parent) {
        tableViewer = new TableViewer(parent, SWT.MULTI | SWT.BORDER |
                                              SWT.FULL_SELECTION);

        tableViewer.setContentProvider(new ArrayContentProvider());
        tableViewer.setLabelProvider(new LabelProvider());
        setUpDateTableColumns();

        Table table = tableViewer.getTable();
        table.setLinesVisible(true);
        table.setHeaderVisible(true);
        table.setEnabled(true);
        table.setFont(registry.get("table"));
    }

    private void setUpDateTableColumns() {
        TableViewerColumn column = setColumnHeader(HEADER_NAME_ID, COL_WIDTH_ID);
        column.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {

                if (element instanceof BlockDate) {

                    BlockDate date = (BlockDate) element;
                    assert (date != null);
                    String id = date.getId() + "";
                    return id + ".";
                }
                return "";
            }
        });

        column = setColumnHeader(HEADER_DATE_START_DATE, COL_WIDTH_DATE);
        column.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {

                if (element instanceof BlockDate) {

                    BlockDate date = (BlockDate) element;
                    assert (date != null);
                    String startDate = date.getStartDate();
                    if (startDate == null) {
                        return CELL_EMPTY;
                    }
                    return startDate;
                }
                return "";
            }
        });

        column = setColumnHeader(HEADER_DATE_START_TIME, COL_WIDTH_DATE_TABLE);
        column.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                if (element instanceof BlockDate) {

                    BlockDate date = (BlockDate) element;
                    assert (date != null);
                    String startTime = date.getStartTime();
                    if (startTime == null) {
                        return CELL_EMPTY;
                    }
                    return startTime;
                }
                return "";
            }
        });

        column = setColumnHeader(HEADER_DATE_END_DATE, COL_WIDTH_DATE_TABLE);
        column.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {

                if (element instanceof BlockDate) {
                    BlockDate date = (BlockDate) element;
                    assert (date != null);
                    String endDate = date.getEndDate();
                    if (endDate == null) {
                        return CELL_EMPTY;
                    }
                    return endDate;
                }
                return "";
            }
        });

        column = setColumnHeader(HEADER_DATE_END_TIME, COL_WIDTH_DATE_TABLE);
        column.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                if (element instanceof BlockDate) {

                    BlockDate date = (BlockDate) element;
                    assert (date != null);
                    String endTime = date.getEndTime();
                    if (endTime == null) {
                        return CELL_EMPTY;
                    }
                    return endTime;
                }
                return "";
            }
        });

        Table table = tableViewer.getTable();
        table.setLayoutData(new GridData(GridData.FILL_BOTH));
    }

    private TableViewerColumn setColumnHeader(String headerName, int colWidth) {
        TableViewerColumn columnViewer = new TableViewerColumn(tableViewer,
                SWT.CENTER);

        TableColumn column = columnViewer.getColumn();
        column.setText(headerName);
        column.setWidth(colWidth);
        column.setResizable(true);
        column.setMoveable(true);
        return columnViewer;
    }

}
