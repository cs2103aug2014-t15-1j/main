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

public class DateTableUI extends Composite {

    private static final String HEADER_NAME_ID = "Id";
    private static final String HEADER_DATE_START_DATE = "Start Date";
    private static final String HEADER_DATE_START_TIME = "Start Time";
    private static final String HEADER_DATE_END_DATE = "End Date";
    private static final String HEADER_DATE_END_TIME = "End Time";

    private static final int COL_WIDTH_DATE_TABLE = 175;
    // NOTE: 50 is able to fit ID, two digit numbers, "." - XX.
    private static final int COL_WIDTH_ID = 30;

    // NOTE:250 is able to fit both date and time - DD/MM/YYYY HHMM
    private static final int COL_WIDTH_DATE = 120;

    private static final String CELL_EMPTY = "<empty>";

    private static DateTableUI dateTableUI;
    FontRegistry registry;
    CTabFolder folder;
    TableViewer tableViewer;

    private DateTableUI(CTabFolder parent) {
        super(parent, SWT.NULL);
        folder = parent;
        buildControls(parent);
    }

    public static DateTableUI getInstance(CTabFolder parent) {
        if (dateTableUI == null) {
            dateTableUI = new DateTableUI(parent);
        }
        return dateTableUI;
    }

    public static DateTableUI getInstance() {
        assert (dateTableUI != null);
        return dateTableUI;
    }

    public TableViewer getTableViewer() {
        return tableViewer;
    }

    public void update(List<BlockDate> dates) {
        tableViewer.setInput(dates);
        folder.setSelection(1);
    }

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
        formatRegistry(parent);
        buildLabel(parent);
        buildTable(parent);
    }

    private void formatRegistry(Composite parent) {
        registry = new FontRegistry(parent.getDisplay());

        FontData[] fontData = new FontData[] { new FontData("Courier New", 10,
                SWT.BOLD | SWT.UNDERLINE_SINGLE) };
        registry.put("title", fontData);

        fontData = new FontData[] { new FontData("Courier New", 10, SWT.NONE) };
        registry.put("table", fontData);
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
