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
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import database.DateTime;
import database.Task;

public class TaskTableUI extends Composite {

    private static FontRegistry registry;
    private static final String HEADER_NAME_ID = "Id";
    private static final String HEADER_NAME_NAME = "Name";
    private static final String HEADER_NAME_DUE = "Due/End";
    private static final String HEADER_NAME_START = "Start";
    private static final String HEADER_NAME_TAGS = "Tags";
    private static final String HEADER_NAME_STATUS = "Status";

    private static final String CELL_EMPTY_DATE = "<no date>";

    private static final String PARA_STATUS_DELETED = "Deleted";
    private static final String PARA_STATUS_TODO = "To do";
    private static final String PARA_STATUS_DONE = "Done";

    // NOTE: 350 is able to fit up to 20 chars
    private static final int COL_WIDTH = 100;

    // NOTE: 50 is able to fit ID, two digit numbers, "." - XX.
    private static final int COL_WIDTH_ID = 30;

    // NOTE:250 is able to fit both date and time - DD/MM/YYYY HHMM
    private static final int COL_WIDTH_DATE = 120;

    // NOTE: 150 is just right for all statuses - To Do, Done, Deleted
    private static final int COL_WIDTH_STATUS = 135;

    private TableViewer tableViewer;
    private CTabFolder folder;
    private static TaskTableUI taskTableUI;

    private TaskTableUI(CTabFolder parent) {
        super(parent, SWT.NULL);
        folder = parent;
        buildControls(parent);
    }

    public static TaskTableUI getInstance() {
        assert (taskTableUI != null);
        return taskTableUI;
    }

    public static TaskTableUI getInstance(CTabFolder parent) {
        if (taskTableUI == null) {
            taskTableUI = new TaskTableUI(parent);
        }
        return taskTableUI;
    }

    public TableViewer getTableViewer() {
        return tableViewer;
    }

    public void update(List<Task> tasks) {
        setTasks(tasks);
    }

    private void setTasks(List<Task> tasks) {
        // table should have been instantiated before this method
        assert (tableViewer != null);
        tableViewer.setInput(tasks);
        folder.setSelection(0);

    }

    public void setTableSection(Task taskToSelect, List<Task> tasks) {
        int size = tasks.size();
        int indexToSelect = 0;
        for (int index = 0; index < size; index++) {
            Task currTask = tasks.get(index);
            if (currTask.equals(taskToSelect)) {
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
        FontData[] fontData = new FontData[] { new FontData("Tahoma", 10,
                SWT.NONE) };
        registry.put("table", fontData);
    }

    private void buildLabel(Composite parent) {
        StyledText tableTitle = new StyledText(parent, SWT.READ_ONLY);
        tableTitle.setText("Results:");
        tableTitle.setEnabled(false);
        tableTitle.setFont(registry.get("title"));
    }

    private void buildTable(Composite parent) {
        tableViewer = new TableViewer(parent, SWT.MULTI | SWT.BORDER |
                                              SWT.FULL_SELECTION);

        tableViewer.setContentProvider(new ArrayContentProvider());
        tableViewer.setLabelProvider(new LabelProvider());
        setUpTaskTableColumns(parent);

        Table table = tableViewer.getTable();
        table.setLinesVisible(true);
        table.setHeaderVisible(true);
        table.setEnabled(true);
        table.setFont(registry.get("table"));
    }

    private void setUpTaskTableColumns(Composite parent) {
        TableViewerColumn column = setColumnHeader(HEADER_NAME_ID, COL_WIDTH_ID);
        column.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                if (element instanceof Task) {
                    Task task = (Task) element;
                    assert (task != null);
                    String id = task.getId() + "";
                    return id + ".";
                }
                return "";
            }
        });
        column = setColumnHeader(HEADER_NAME_NAME, COL_WIDTH);

        column.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                if (element instanceof Task) {
                    // set Name: max 40 characters
                    Task task = (Task) element;
                    String name = task.getName();
                    assert (task != null);
                    if (name == null || name.isEmpty() || name.equals("null")) {
                        return "";
                    }
                    return task.getName();
                }
                return "";
            }
        });

        column = setColumnHeader(HEADER_NAME_START, COL_WIDTH_DATE);
        column.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                if (element instanceof Task) {
                    Task task = (Task) element;
                    assert (task != null);
                    DateTime start = task.getStart();
                    assert (start != null);
                    String startDate = start.toString();
                    if (startDate == null || startDate.isEmpty()) {
                        return CELL_EMPTY_DATE;
                    }

                    return startDate;
                }
                return "";
            }
        });

        column = setColumnHeader(HEADER_NAME_DUE, COL_WIDTH_DATE);

        column.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                if (element instanceof Task) {
                    Task task = (Task) element;
                    assert (task != null);
                    DateTime due = task.getDue();
                    assert (due != null);
                    String dueDate = due.toString();
                    if (dueDate == null || dueDate.isEmpty()) {
                        return CELL_EMPTY_DATE;
                    }
                    return dueDate;
                }
                return "";
            }
        });

        column = setColumnHeader(HEADER_NAME_TAGS, COL_WIDTH);
        column.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                if (element instanceof Task) {
                    Task task = (Task) element;
                    assert (task != null);
                    List<String> tags = task.getTags();
                    String tag = "";

                    if (tags == null || tags.isEmpty()) {
                        return "";
                    }
                    for (int index = 0; index < tags.size(); index++) {
                        tag = tag + tags.get(index);
                    }
                    return tag;
                }
                return "";
            }

        });

        column = setColumnHeader(HEADER_NAME_STATUS, COL_WIDTH_STATUS);
        column.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                if (element instanceof Task) {
                    Task task = (Task) element;
                    assert (task != null);
                    String Status;
                    if (task.isDeleted()) {
                        Status = PARA_STATUS_DELETED;
                    } else if (task.isDone()) {
                        Status = PARA_STATUS_DONE + " " +
                                 task.getCompletedOn().toString();
                    } else {
                        Status = PARA_STATUS_TODO;
                    }
                    return Status;
                }
                return "";
            }
        });

        Table table = tableViewer.getTable();
        table.setLayoutData(new GridData(GridData.FILL_BOTH));

        table.addListener(SWT.RESIZE, new Listener() {

            @Override
            public void handleEvent(Event event) {
                switch (event.type) {
                    case SWT.RESIZE:
                        resizeCol();
                        break;

                    default:
                        break;
                }
            }
        });

    }

    protected void resizeCol() {
        Table table = tableViewer.getTable();
        int totalColWidth = 0;

        for (int index = 0; index < table.getColumnCount() - 1; index++) {
            totalColWidth = table.getColumn(index).getWidth();
        }

        TableColumn nameCol = table.getColumn(1);
        TableColumn tagsCol = table.getColumn(4);

        TableColumn lastCol = table.getColumn(table.getColumnCount() - 1);
        lastCol.pack();
        nameCol.pack();
        tagsCol.pack();

        Rectangle tableArea = table.getClientArea();

        Point size = table.computeSize(SWT.DEFAULT, SWT.DEFAULT);
        int width = tableArea.width - 2 * table.getBorderWidth() -
                    folder.getBorderWidth();

        if (size.y > tableArea.height + table.getHeaderHeight()) {
            Point vertScrollWidth = table.getVerticalBar().getSize();
            width = width - vertScrollWidth.x;
        }

        if (totalColWidth < tableArea.width) {
            nameCol.setWidth((width - totalColWidth) / 4);
            tagsCol.setWidth((width - totalColWidth) / 4);
        }

    }

    private TableViewerColumn setColumnHeader(String headerName, int colWidth) {
        TableViewerColumn columnViewer;
        if (headerName.equals(HEADER_NAME_NAME)) {
            columnViewer = new TableViewerColumn(tableViewer, SWT.LEFT);
        } else {
            columnViewer = new TableViewerColumn(tableViewer, SWT.CENTER);
        }
        TableColumn column = columnViewer.getColumn();
        column.setText(headerName);
        column.setWidth(colWidth);
        column.setResizable(true);
        column.setMoveable(true);
        return columnViewer;
    }

}
