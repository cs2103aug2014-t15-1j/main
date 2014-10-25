package gui;

import java.util.List;

import org.eclipse.jface.resource.FontRegistry;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

import database.BlockDate;
import database.DateTime;
import database.Task;

public class SetUp {

    public static final String LINE_SEPARATOR = System
            .getProperty("line.separator");
    private static final String PROGRAM_NAME = "HayStack";
    private static final String MESSAGE_TYPE_HERE = "Type here";

    private static final int NUM_COLS_SCREEN = 2;
    private static final int NUM_COLS_COMPOSITE = 1;
    private static final int MIN_WIDTH_SCREEN = 1800;
    private static final int MIN_HEIGHT_SCREEN = 1500;
    private static final int MIN_WIDTH_SIDE_PANE = 1500;
    private static final int MIN_HEIGHT_SIDE_PANE = 1300;

    private static final String HEADER_NAME_ID = "Id";
    private static final String HEADER_NAME_NAME = "Name";
    private static final String HEADER_NAME_DUE = "Due";
    private static final String HEADER_NAME_START = "Start";
    private static final String HEADER_NAME_END = "End";
    private static final String HEADER_NAME_TAGS = "Tags";
    private static final String HEADER_NAME_STATUS = "Status";

    private static final String HEADER_DATE_START_DATE = "Start Date";
    private static final String HEADER_DATE_START_TIME = "Start Time";
    private static final String HEADER_DATE_END_DATE = "End Date";
    private static final String HEADER_DATE_END_TIME = "End Time";

    private static final String PARA_STATUS_DELETED = "Deleted";
    private static final String PARA_STATUS_TODO = "To do";
    private static final String PARA_STATUS_DONE = "Done";

    private static final String CELL_EMPTY = "empty";

    private static final int COL_WIDTH = 175;
    private static final int COL_WIDTH_ID = 50;

    private static SetUp setUp;
    private Shell shell;
    private Composite mainInterface;
    private Composite sidePane;
    private Composite feedbackAndInput;

    private TabFolder tabFolder;
    private TabItem taskTable;
    private TabItem dateTable;

    private TableViewer tableViewer;
    private TableViewer dateViewer;

    private StyledText feedback;
    private Text commandLine;
    private StyledText upcomingTasksList;
    private StyledText floatingTasksList;
    private StyledText todaysDate;

    private FontRegistry registry;

    private SetUp(Shell shell) {
        this.shell = shell;
        initialise();
    }

    // Note: SetUp will not exist if MainScreen.java is destroyed, because only
    // MainScreen.java creates a SetUp object.
    /**
     * This returns a SetUp object and creates a new instance of the SetUp
     * object if it does not exist.
     * 
     * @param shell
     *            used to create a new SetUp object
     * @return instance of the SetUp object created
     */
    public static SetUp getInstance(Shell shell) {
        if (setUp == null) {
            setUp = new SetUp(shell);
        }
        return setUp;
    }

    /*
     * This returns the instance of a SetUp object created at the earlier point
     * of the program
     */
    public static SetUp getInstance() {
        return setUp;
    }

    public StyledText getFeedBack() {
        return this.feedback;
    }

    public Text getCommandLine() {
        return this.commandLine;
    }

    public StyledText getUpcomingTasksList() {
        return this.upcomingTasksList;
    }

    public StyledText getFloatingTasksList() {
        return this.floatingTasksList;
    }

    public TableViewer getTableViewer() {
        return this.tableViewer;
    }

    public TableViewer getDateViewer() {
        return this.dateViewer;
    }

    public TabFolder getTabFolder() {
        return this.tabFolder;
    }

    private void initialise() {
        setUpShell();
        setUpComposites();
        setUpWidgets();
    }

    private void setUpShell() {
        // shell.setImage(icon);
        GridLayout layout = new GridLayout();
        layout.numColumns = NUM_COLS_SCREEN;
        this.shell.setLayout(layout);
        this.shell.setSize(MIN_WIDTH_SCREEN, MIN_HEIGHT_SCREEN);
        this.shell.setText(PROGRAM_NAME);
    }

    private void setUpComposites() {
        formatRegistry();
        setUpProgramLabel();
        setUpMainInterface();
        setUpSidePane();
        setUpFeedbackAndInput();
    }

    private void formatRegistry() {

        this.registry = new FontRegistry(shell.getDisplay());
        FontData font = new FontData("New Courier", 11, SWT.NORMAL);
        FontData[] fontData = new FontData[] { font };
        registry.put("type box", fontData);
        fontData = new FontData[] { new FontData("Arial", 11, SWT.NORMAL) };
        registry.put("feedback", fontData);
        fontData = new FontData[] { new FontData("Times New Roman", 11,
                SWT.NORMAL) };
        registry.put("list headers", fontData);
        fontData = new FontData[] { new FontData("Arial", 10,
                SWT.BOLD | SWT.UNDERLINE_SINGLE) };
        registry.put("title", fontData);
    }

    private void setUpProgramLabel() {
        Composite programLabel = new Composite(this.shell, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.numColumns = NUM_COLS_SCREEN;
        programLabel.setLayout(layout);
        GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
        gridData.horizontalSpan = 2;
        programLabel.setLayoutData(gridData);

        Label title = new Label(programLabel, SWT.SINGLE);
        title.setText("Haystack");

    }

    private void setUpMainInterface() {
        mainInterface = new Composite(this.shell, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.numColumns = NUM_COLS_COMPOSITE;
        mainInterface.setLayout(layout);
        GridData gridData = new GridData(GridData.FILL_BOTH);
        mainInterface.setLayoutData(gridData);

        tabFolder = new TabFolder(mainInterface, SWT.BORDER);
        tabFolder.setLayoutData(new GridData(GridData.FILL_BOTH));

        taskTable = new TabItem(tabFolder, SWT.BORDER);
        taskTable.setText("Tasks");
        setUpTaskTable();
        taskTable.setControl(tableViewer.getTable());

        dateTable = new TabItem(tabFolder, SWT.NONE);
        dateTable.setText("Blocked Dates");
        setUpDateTable();
        dateTable.setControl(dateViewer.getTable());

    }

    private void setUpSidePane() {
        sidePane = new Composite(this.shell, SWT.NONE);
        GridLayout sidePaneLayout = new GridLayout();
        sidePaneLayout.numColumns = 1;
        sidePane.setLayout(sidePaneLayout);
        sidePane.setLayoutData(new GridData(GridData.FILL_VERTICAL));
        sidePane.setSize(MIN_WIDTH_SIDE_PANE, MIN_HEIGHT_SIDE_PANE);
    }

    private void setUpFeedbackAndInput() {
        feedbackAndInput = new Composite(this.shell, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.numColumns = NUM_COLS_COMPOSITE;
        feedbackAndInput.setLayout(layout);
        GridData gridData = new GridData(GridData.FILL_BOTH);
        feedbackAndInput.setLayoutData(gridData);
    }

    private void setUpWidgets() {
        setUpCommandLine();
        setUpFeedBack();
        setUpUpcomingTaskList();
        setUpFloatingTaskList();
        setUpDate();
    }

    private void setUpDateTable() {
        StyledText tableTitle = new StyledText(tabFolder, SWT.READ_ONLY);
        tableTitle.setText("Dates Blocked:");
        tableTitle.setEnabled(false);
        tableTitle.setFont(registry.get("title"));

        dateViewer = new TableViewer(tabFolder, SWT.MULTI | SWT.BORDER);

        dateViewer.setContentProvider(new ArrayContentProvider());
        dateViewer.setLabelProvider(new LabelProvider());
        setUpDateTableColumns();

        Table table = tableViewer.getTable();
        table.setLinesVisible(true);
        table.setHeaderVisible(true);
        table.setEnabled(false);

    }

    // to refactor method
    private void setUpDateTableColumns() {

        TableViewerColumn column = setColumnHeader(HEADER_DATE_START_DATE,
                                                   COL_WIDTH);
        column.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {

                if (element instanceof Task) {
                    return "";
                }
                BlockDate date = (BlockDate) element;
                assert (date != null);
                String startDate = date.getStartDate();
                if (startDate == null) {
                    return CELL_EMPTY;
                }
                return startDate;
            }
        });
        column = setColumnHeader(HEADER_DATE_START_TIME, COL_WIDTH);
        column.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                if (element instanceof Task) {
                    return "";
                }

                BlockDate date = (BlockDate) element;
                assert (date != null);
                String startTime = date.getStartTime();
                if (startTime == null) {
                    return CELL_EMPTY;
                }
                return startTime;
            }
        });

        column = setColumnHeader(HEADER_DATE_END_DATE, COL_WIDTH);
        column.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                if (element instanceof Task) {
                    return "";
                }

                BlockDate date = (BlockDate) element;
                assert (date != null);
                String endDate = date.getEndDate();
                if (endDate == null) {
                    return CELL_EMPTY;
                }
                return endDate;
            }
        });

        column = setColumnHeader(HEADER_DATE_END_TIME, COL_WIDTH);
        column.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                if (element instanceof Task) {
                    return "";
                }

                BlockDate date = (BlockDate) element;
                assert (date != null);
                String endTime = date.getEndTime();
                if (endTime == null) {
                    return CELL_EMPTY;
                }
                return endTime;
            }
        });

        Table table = dateViewer.getTable();
        table.setLayoutData(new GridData(GridData.FILL_BOTH));
    }

    private void setUpTaskTable() {

        StyledText tableTitle = new StyledText(tabFolder, SWT.READ_ONLY);
        tableTitle.setText("Results:");
        tableTitle.setEnabled(false);
        tableTitle.setFont(registry.get("title"));

        tableViewer = new TableViewer(tabFolder, SWT.MULTI | SWT.BORDER);

        tableViewer.setContentProvider(new ArrayContentProvider());
        tableViewer.setLabelProvider(new LabelProvider());
        setUpTaskTableColumns();

        Table table = tableViewer.getTable();
        table.setLinesVisible(true);
        table.setHeaderVisible(true);
        table.setEnabled(false);
    }

    private void setUpTaskTableColumns() {
        TableViewerColumn column = setColumnHeader(HEADER_NAME_ID, COL_WIDTH_ID);
        column.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {

                Task task = (Task) element;
                assert (task != null);
                String id = task.getId() + "";
                return id;
            }
        });
        column = setColumnHeader(HEADER_NAME_NAME, COL_WIDTH);
        column.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                // set Name: max 40 characters
                Task task = (Task) element;
                String name = task.getName();
                assert (task != null);
                if (name == null || name.isEmpty() || name.equals("null")) {
                    return "";
                }
                return task.getName();
            }
        });

        column = setColumnHeader(HEADER_NAME_DUE, COL_WIDTH);
        column.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                Task task = (Task) element;
                assert (task != null);
                DateTime Due = task.getDue();

                if (Due == null) {
                    return CELL_EMPTY;
                }
                return Due.toString();
            }
        });

        column = setColumnHeader(HEADER_NAME_START, COL_WIDTH);
        column.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                Task task = (Task) element;
                assert (task != null);
                DateTime Start = task.getStart();
                if (Start == null) {
                    return CELL_EMPTY;
                }

                return Start.toString();
            }
        });

        column = setColumnHeader(HEADER_NAME_END, COL_WIDTH);
        column.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                Task task = (Task) element;
                assert (task != null);
                DateTime End = task.getEnd();
                if (End == null) {
                    return CELL_EMPTY;
                }
                return End.toString();
            }
        });

        column = setColumnHeader(HEADER_NAME_TAGS, COL_WIDTH);
        column.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                Task task = (Task) element;
                assert (task != null);
                List<String> tags = task.getTags();
                String tag = "";

                if (tags == null || tags.isEmpty()) {
                    return CELL_EMPTY;
                }
                for (int index = 0; index < tags.size(); index++) {
                    tag = tag + tags.get(index);
                }
                return tag;
            }
        });

        column = setColumnHeader(HEADER_NAME_STATUS, COL_WIDTH);
        column.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                Task task = (Task) element;
                assert (task != null);
                String Status;
                if (task.isDeleted()) {
                    Status = PARA_STATUS_DELETED;
                } else if (task.isDone()) {
                    Status = PARA_STATUS_DONE;
                } else {
                    Status = PARA_STATUS_TODO;
                }
                return Status;
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

    private void setUpFeedBack() {
        feedback = new StyledText(feedbackAndInput, SWT.MULTI);
        feedback.setText("");
        GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
        feedback.setLayoutData(gridData);
        feedback.setFont(registry.get("feedback"));
        Color white = shell.getDisplay().getSystemColor(SWT.COLOR_WHITE);
        this.feedback.setForeground(white);
        feedback.setEnabled(false);
    }

    private void setUpCommandLine() {
        commandLine = new Text(feedbackAndInput, SWT.SINGLE | SWT.BORDER_DOT);
        GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
        commandLine.setLayoutData(gridData);
        commandLine.setText(MESSAGE_TYPE_HERE);
        Display display = shell.getDisplay();
        Color black = display.getSystemColor(SWT.COLOR_BLACK);
        Color white = display.getSystemColor(SWT.COLOR_WHITE);
        this.commandLine.setBackground(black);
        this.commandLine.setForeground(white);
        commandLine.setFont(registry.get("type box"));
    }

    private void setUpUpcomingTaskList() {
        Label upcomingTask = new Label(sidePane, SWT.SINGLE);
        upcomingTask.setText("Upcoming Task");

        upcomingTasksList = new StyledText(sidePane, SWT.MULTI | SWT.READ_ONLY |
                                                     SWT.LEFT_TO_RIGHT);

        upcomingTasksList.setFont(registry.get("list headers"));
        GridData gridData = new GridData(GridData.FILL_BOTH);
        gridData.heightHint = 250;
        upcomingTasksList.setLayoutData(gridData);
        upcomingTasksList.setWordWrap(true);

        Display display = shell.getDisplay();
        Color white = display.getSystemColor(SWT.COLOR_WHITE);
        upcomingTasksList.setBackground(white);
        upcomingTasksList.setEnabled(true);
        upcomingTasksList.setFont(registry.get("list headers"));
    }

    private void setUpFloatingTaskList() {
        Label floatingTask = new Label(sidePane, SWT.SINGLE);
        floatingTask.setText("Someday");

        floatingTasksList = new StyledText(sidePane, SWT.MULTI | SWT.READ_ONLY |
                                                     SWT.LEFT_TO_RIGHT);
        floatingTasksList.setFont(registry.get("list headers"));
        GridData gridData = new GridData(GridData.FILL_BOTH);
        gridData.heightHint = 250;
        floatingTasksList.setLayoutData(gridData);
        floatingTasksList.setEnabled(false);
        floatingTasksList.setWordWrap(true);

        Display display = shell.getDisplay();
        Color white = display.getSystemColor(SWT.COLOR_WHITE);
        floatingTasksList.setBackground(white);
        floatingTasksList.setEnabled(true);
        floatingTasksList.setFont(registry.get("list headers"));
    }

    private void setUpDate() {

        todaysDate = new StyledText(sidePane, SWT.READ_ONLY | SWT.SINGLE |
                                              SWT.RIGHT_TO_LEFT);
        todaysDate.setFont(registry.get("list headers"));
        todaysDate.setText("today's date");

    }
}