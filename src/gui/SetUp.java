package gui;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import logic.Processor;

import org.eclipse.jface.resource.FontRegistry;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
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

    // NOTE: Limit below 1024x768
    private static final int MIN_WIDTH_SCREEN = 800;
    private static final int MIN_HEIGHT_SCREEN = 384;
    private static final int MIN_HEIGHT_SIDE_PANE = MIN_HEIGHT_SCREEN;

    private static final String HEADER_NAME_ID = "Id";
    private static final String HEADER_NAME_NAME = "Name";
    private static final String HEADER_NAME_DUE = "Due/End";
    private static final String HEADER_NAME_START = "Start";
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

    private static final String WELCOME_MESSAGE = "Welcome.";

    // NOTE: 350 is able to fit up to 20 chars
    private static final int COL_WIDTH = 100;

    // NOTE: 50 is able to fit ID, two digit numbers, "." - XX.
    private static final int COL_WIDTH_ID = 30;

    // NOTE:250 is able to fit both date and time - DD/MM/YYYY HHMM
    private static final int COL_WIDTH_DATE = 120;

    // NOTE: 150 is just right for all statuses - To Do, Done, Deleted
    private static final int COL_WIDTH_STATUS = 150;

    private static final int COL_WIDTH_DATE_TABLE = 175;

    protected static SetUp setUp;
    private Shell shell;
    private Composite mainInterface;
    private Composite sidePane;

    private CTabFolder tabFolder;
    private CTabItem taskTable;
    private CTabItem dateTable;

    private TableViewer tableViewer;
    private TableViewer dateViewer;

    private StyledText feedback;
    private Text commandLine;
    private StyledText upcomingTasksList;
    private StyledText floatingTasksList;
    private StyledText todaysDate;

    private FontRegistry fontRegistry;
    private ImageRegistry imageRegistry;

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

    public Shell getShell() {
        return shell;
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

    public CTabFolder getTabFolder() {
        return this.tabFolder;
    }

    private void initialise() {
        setUpShell();
        setUpComposites();
        setUpWidgets();
    }

    private void setUpShell() {
        GridLayout layout = new GridLayout();
        layout.numColumns = NUM_COLS_SCREEN;
        this.shell.setLayout(layout);
        this.shell.setText(PROGRAM_NAME);
    }

    private void setUpComposites() {
        formatFontRegistry();
        formatImageRegistry();
        setUpProgramLabel();
        setUpMainInterface();
        setUpTableComposite();
        setUpSidePane();
    }

    private void formatFontRegistry() {

        fontRegistry = new FontRegistry(shell.getDisplay());
        FontData font = new FontData("New Courier", 11, SWT.NORMAL);
        FontData[] fontData = new FontData[] { font };
        fontRegistry.put("type box", fontData);

        fontData = new FontData[] { new FontData("Impact", 13, SWT.NORMAL) };
        fontRegistry.put("haystack title", fontData);

        fontData = new FontData[] { new FontData("Arial", 11, SWT.NORMAL) };
        fontRegistry.put("feedback", fontData);

        fontData = new FontData[] { new FontData("Times New Roman", 11,
                SWT.NORMAL) };
        fontRegistry.put("list headers", fontData);

        fontData = new FontData[] { new FontData("Arial", 10,
                SWT.BOLD | SWT.UNDERLINE_SINGLE) };
        fontRegistry.put("title", fontData);

        fontData = new FontData[] { new FontData("Tahoma", 10, SWT.NONE) };
        fontRegistry.put("table", fontData);

    }

    private void formatImageRegistry() {

        imageRegistry = new ImageRegistry(shell.getDisplay());

        ImageDescriptor id = ImageDescriptor
                .createFromFile(SetUp.class, "/resource/mainbg.png");
        imageRegistry.put("main", id);

        id = ImageDescriptor.createFromFile(SetUp.class,
                                            "/resource/resultBg.png");
        imageRegistry.put("result", id);

        id = ImageDescriptor.createFromFile(SetUp.class,
                                            "/resource/sidepanelbg.png");
        imageRegistry.put("sidepane", id);

        id = ImageDescriptor.createFromFile(SetUp.class,
                                            "/resource/Someday.png");
        imageRegistry.put("someday", id);
        id = ImageDescriptor.createFromFile(SetUp.class,
                                            "/resource/UpcomingTask.png");
        imageRegistry.put("upcoming", id);

    }

    private void setUpProgramLabel() {
        Composite programLabel = new Composite(this.shell, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.numColumns = NUM_COLS_SCREEN;
        programLabel.setLayout(layout);
        GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
        gridData.horizontalSpan = 2;
        programLabel.setLayoutData(gridData);

        Label title = new Label(programLabel, SWT.SINGLE | SWT.CENTER);
        title.setText("Hastack");
        title.setFont(fontRegistry.get("haystack title"));
        Color color = shell.getDisplay().getSystemColor(SWT.COLOR_BLACK);
        title.setForeground(color);

    }

    private void setUpMainInterface() {
        mainInterface = new Composite(this.shell, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.numColumns = NUM_COLS_COMPOSITE;
        mainInterface.setLayout(layout);
        GridData gridData = new GridData(GridData.FILL_BOTH);
        mainInterface.setLayoutData(gridData);
    }

    private void setUpSidePane() {
        sidePane = new Composite(this.shell, SWT.NONE);
        GridLayout sidePaneLayout = new GridLayout();
        sidePaneLayout.numColumns = 1;
        sidePane.setLayout(sidePaneLayout);
        sidePane.setLayoutData(new GridData(GridData.FILL_VERTICAL));
        int shellWidth = shell.getSize().x;

        sidePane.setSize(shellWidth / 3, MIN_HEIGHT_SIDE_PANE);
    }

    private void setUpTableComposite() {
        Composite tableComp = new Composite(this.mainInterface, SWT.NONE);
        GridLayout layout = new GridLayout();
        tableComp.setLayout(layout);
        tableComp.setLayoutData(new GridData(GridData.FILL_BOTH));
        Color white = shell.getDisplay().getSystemColor(SWT.COLOR_WHITE);
        tableComp.setBackground(white);

        tabFolder = new CTabFolder(tableComp, SWT.BORDER);
        tabFolder.setLayoutData(new GridData(GridData.FILL_BOTH));

        taskTable = new CTabItem(tabFolder, SWT.BORDER);
        taskTable.setText("Tasks");
        setUpTaskTable();
        taskTable.setControl(tableViewer.getTable());

        dateTable = new CTabItem(tabFolder, SWT.NONE);
        dateTable.setText("Blocked Dates");
        setUpDateTable();
        dateTable.setControl(dateViewer.getTable());
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
        tableTitle.setFont(fontRegistry.get("title"));

        dateViewer = new TableViewer(tabFolder, SWT.MULTI | SWT.BORDER |
                                                SWT.FULL_SELECTION);

        dateViewer.setContentProvider(new ArrayContentProvider());
        dateViewer.setLabelProvider(new LabelProvider());
        setUpDateTableColumns();

        Table table = dateViewer.getTable();
        table.setLinesVisible(true);
        table.setHeaderVisible(true);
        table.setEnabled(true);
        table.setFont(fontRegistry.get("table"));
    }

    // to refactor method
    private void setUpDateTableColumns() {

        TableViewerColumn column = setColumnHeader(HEADER_NAME_ID,
                                                   COL_WIDTH_ID, dateViewer);
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

        column = setColumnHeader(HEADER_DATE_START_DATE, COL_WIDTH_DATE,
                                 dateViewer);
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

        column = setColumnHeader(HEADER_DATE_START_TIME, COL_WIDTH_DATE_TABLE,
                                 dateViewer);
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

        column = setColumnHeader(HEADER_DATE_END_DATE, COL_WIDTH_DATE_TABLE,
                                 dateViewer);
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

        column = setColumnHeader(HEADER_DATE_END_TIME, COL_WIDTH_DATE_TABLE,
                                 dateViewer);
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

        Table table = dateViewer.getTable();
        table.setLayoutData(new GridData(GridData.FILL_BOTH));
    }

    private void setUpTaskTable() {

        StyledText tableTitle = new StyledText(tabFolder, SWT.READ_ONLY);
        tableTitle.setText("Results:");
        tableTitle.setEnabled(false);
        tableTitle.setFont(fontRegistry.get("title"));

        tableViewer = new TableViewer(tabFolder, SWT.MULTI | SWT.BORDER |
                                                 SWT.FULL_SELECTION);

        tableViewer.setContentProvider(new ArrayContentProvider());
        tableViewer.setLabelProvider(new LabelProvider());
        setUpTaskTableColumns();

        Table table = tableViewer.getTable();
        table.setLinesVisible(true);
        table.setHeaderVisible(true);
        table.setEnabled(true);
        table.setFont(fontRegistry.get("table"));
    }

    private void setUpTaskTableColumns() {
        TableViewerColumn column = setColumnHeader(HEADER_NAME_ID,
                                                   COL_WIDTH_ID, tableViewer);
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
        column = setColumnHeader(HEADER_NAME_NAME, COL_WIDTH, tableViewer);

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

        column = setColumnHeader(HEADER_NAME_STATUS, COL_WIDTH_STATUS,
                                 tableViewer);
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
                        Status = PARA_STATUS_DONE;
                    } else {
                        Status = PARA_STATUS_TODO;
                    }
                    return Status;
                }
                return "";
            }
        });

        column = setColumnHeader(HEADER_NAME_START, COL_WIDTH_DATE, tableViewer);
        column.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                if (element instanceof Task) {
                    Task task = (Task) element;
                    assert (task != null);
                    DateTime Start = task.getStart();
                    if (Start == null) {
                        return CELL_EMPTY;
                    }

                    return Start.toString();
                }
                return "";
            }
        });

        column = setColumnHeader(HEADER_NAME_DUE, COL_WIDTH_DATE, tableViewer);

        column.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                if (element instanceof Task) {
                    Task task = (Task) element;
                    assert (task != null);
                    DateTime Due = task.getDue();

                    if (Due == null) {
                        return CELL_EMPTY;
                    }
                    return Due.toString();
                }
                return "";
            }
        });

        column = setColumnHeader(HEADER_NAME_TAGS, COL_WIDTH, tableViewer);
        column.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                if (element instanceof Task) {
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
                return "";
            }

        });

        Table table = tableViewer.getTable();
        table.setLayoutData(new GridData(GridData.FILL_BOTH));
        shell.addListener(SWT.RESIZE, new Listener() {

            @Override
            public void handleEvent(Event event) {
                switch (event.type) {
                    case SWT.RESIZE:
                        resizeCol(tableViewer);
                        break;

                    default:
                        break;
                }
            }

            private void resizeCol(TableViewer viewer) {
                Table table = viewer.getTable();
                int totalColWidth = 0;

                for (int index = 0; index < table.getColumnCount() - 1; index++) {
                    totalColWidth = table.getColumn(index).getWidth();
                }

                TableColumn nameCol = table.getColumn(1);
                TableColumn tagsCol = table.getColumn(5);

                TableColumn lastCol = table.getColumn(table.getColumnCount() - 1);
                lastCol.pack();
                nameCol.pack();
                tagsCol.pack();

                Rectangle tableArea = table.getClientArea();

                Point size = table.computeSize(SWT.DEFAULT, SWT.DEFAULT);
                int width = tableArea.width - 2 * table.getBorderWidth() -
                            tabFolder.getBorderWidth();

                if (size.y > tableArea.height + table.getHeaderHeight()) {
                    Point vertScrollWidth = table.getVerticalBar().getSize();
                    width = width - vertScrollWidth.x;
                }

                if (totalColWidth < tableArea.width) {
                    nameCol.setWidth((width - totalColWidth) / 4);
                    tagsCol.setWidth((width - totalColWidth) / 4);
                }

            }

        });

    }

    private TableViewerColumn setColumnHeader(String headerName, int colWidth,
                                              TableViewer viewer) {
        TableViewerColumn columnViewer = new TableViewerColumn(viewer,
                SWT.CENTER);
        TableColumn column = columnViewer.getColumn();
        column.setText(headerName);
        column.setWidth(colWidth);
        column.setResizable(true);
        column.setMoveable(true);
        return columnViewer;
    }

    private void setUpFeedBack() {
        feedback = new StyledText(mainInterface, SWT.MULTI);
        feedback.setText(WELCOME_MESSAGE);
        GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
        feedback.setLayoutData(gridData);
        feedback.setFont(fontRegistry.get("feedback"));
        Color white = shell.getDisplay().getSystemColor(SWT.COLOR_WHITE);
        this.feedback.setForeground(white);
        feedback.setEnabled(false);
    }

    private void setUpCommandLine() {
        commandLine = new Text(mainInterface, SWT.SINGLE | SWT.BORDER_DOT);
        GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
        commandLine.setLayoutData(gridData);
        commandLine.setText(MESSAGE_TYPE_HERE);
        Display display = shell.getDisplay();
        Color black = display.getSystemColor(SWT.COLOR_BLACK);
        Color white = display.getSystemColor(SWT.COLOR_WHITE);
        this.commandLine.setBackground(black);

        this.commandLine.setForeground(white);
        commandLine.setFont(fontRegistry.get("type box"));

        commandLine.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.keyCode == SWT.ARROW_UP || e.keyCode == SWT.ARROW_DOWN) {
                    String output = "";
                    switch (e.keyCode) {
                        case SWT.ARROW_UP:
                            output = Processor.getInstance().fetchInputUpKey();

                            break;
                        case SWT.ARROW_DOWN:
                            output = Processor.getInstance()
                                    .fetchInputDownKey();
                            break;
                    }
                    commandLine.setText(output);
                }
            }
        });
    }

    private void setUpUpcomingTaskList() {
        Display display = shell.getDisplay();
        Label upcomingTask = new Label(sidePane, SWT.NONE);

        upcomingTask.setImage(imageRegistry.get("upcoming"));
        GridData centeredGridData = new GridData(SWT.CENTER, SWT.FILL, true,
                true);
        upcomingTask.setLayoutData(centeredGridData);

        upcomingTasksList = new StyledText(sidePane, SWT.MULTI | SWT.READ_ONLY |
                                                     SWT.LEFT_TO_RIGHT);

        upcomingTasksList.setFont(fontRegistry.get("list headers"));
        GridData gridData = new GridData(GridData.FILL_BOTH);
        gridData.heightHint = 250;
        upcomingTasksList.setLayoutData(gridData);
        upcomingTasksList.setWordWrap(true);

        Color white = display.getSystemColor(SWT.COLOR_WHITE);
        upcomingTasksList.setBackground(white);
        upcomingTasksList.setEnabled(true);
        upcomingTasksList.setFont(fontRegistry.get("table"));
    }

    private void setUpFloatingTaskList() {
        Display display = shell.getDisplay();
        Label floatingTask = new Label(sidePane, SWT.SINGLE);
        floatingTask.setImage(imageRegistry.get("someday"));
        GridData centeredGridData = new GridData(SWT.CENTER, SWT.FILL, true,
                true);
        floatingTask.setLayoutData(centeredGridData);

        floatingTasksList = new StyledText(sidePane, SWT.MULTI | SWT.READ_ONLY |
                                                     SWT.LEFT_TO_RIGHT);
        floatingTasksList.setFont(fontRegistry.get("list headers"));
        GridData gridData = new GridData(GridData.FILL_BOTH);
        gridData.heightHint = 250;
        floatingTasksList.setLayoutData(gridData);
        floatingTasksList.setEnabled(false);
        floatingTasksList.setWordWrap(true);

        Color white = display.getSystemColor(SWT.COLOR_WHITE);
        floatingTasksList.setBackground(white);
        floatingTasksList.setEnabled(true);
        floatingTasksList.setFont(fontRegistry.get("table"));
    }

    private void setUpDate() {

        todaysDate = new StyledText(sidePane, SWT.READ_ONLY | SWT.SINGLE |
                                              SWT.RIGHT_TO_LEFT | SWT.BOLD);
        todaysDate.setFont(fontRegistry.get("list headers"));

        todaysDate.setSize(MIN_WIDTH_SCREEN, 1);
        Date date = new Date();
        String now = new SimpleDateFormat("EEE MMM d, yyyy").format(date);
        todaysDate.setText(now);
        Color white = shell.getDisplay().getSystemColor(SWT.COLOR_WHITE);
        this.todaysDate.setForeground(white);

    }
}