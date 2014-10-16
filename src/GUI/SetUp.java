package GUI;

import java.util.List;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

import Storage.Task;


public class SetUp {

    public static final String LINE_SEPARATOR = System
            .getProperty("line.separator");
    private static final String PROGRAM_NAME = "HayStack";
    private static final String MESSAGE_TYPE_HERE = "Type here";

    private static final int NUM_COLS_SCREEN = 2;
    private static final int NUM_COLS_COMPOSITE = 1;
    private static final int MIN_WIDTH_SCREEN = 1800;
    private static final int MIN_HEIGHT_SCREEN = 1500;
    private static final int MIN_WIDTH_SIDE_PANE = 1200;
    private static final int MIN_HEIGHT_SIDE_PANE = 1000;

    private static final String HEADER_NAME_ID = "Id";
    private static final String HEADER_NAME_NAME = "Name";
    private static final String HEADER_NAME_DUE = "Due";
    private static final String HEADER_NAME_START = "Start";
    private static final String HEADER_NAME_END = "End";
    private static final String HEADER_NAME_TAGS = "Tags";

    private static final int COL_WIDTH = 175;
    private static final int COL_WIDTH_ID = 50;

    private static SetUp setUp;
    private Shell shell;
    private Composite mainInterface;
    private Composite sidePane;

    private TableViewer tableViewer;

    private Label feedback;
    private Text commandLine;
    private DateTime calendar;
    private Label taskList;

    private SetUp(Shell shell) {
        this.shell = shell;
        initialise();
    }
    
    
    // Note: SetUp will not exist if MainScreen.java is destroyed, because only MainScreen.java creates a SetUp object.
    /**
     * This returns a SetUp object and creates a new instance of the SetUp object if it does not exist.
     * @param shell used to create a new SetUp object
     * @return instance of the SetUp object created
     */
    public static SetUp getInstance(Shell shell) {
        if (setUp == null) {
            setUp = new SetUp(shell);
        }
        return setUp;
    }
    
    /*
     * This returns the instance of a SetUp object created at the earlier point of the program
     */
    public static SetUp getInstance(){
        return setUp;
    }
    
    public Label getFeedBack(){
        return this.feedback;
    }
    
    public Text getCommandLine(){
        return this.commandLine;
    }
    
    public Label getTaskList(){
        return this.taskList;
    }
    
    public TableViewer getTableViewer(){
        return this.tableViewer;
    }
    
    public DateTime getCalendar(){
        return this.calendar;
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
        this.shell.setSize(MIN_WIDTH_SCREEN, MIN_HEIGHT_SCREEN);
        this.shell.setText(PROGRAM_NAME);
    }

    private void setUpComposites() {
        setUpMainInterface();
        setUpSidePane();
    }

    private void setUpMainInterface() {
        mainInterface = new Composite(this.shell, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.numColumns = NUM_COLS_COMPOSITE;
        GridData gridData = new GridData(GridData.FILL_BOTH);
        mainInterface.setLayoutData(gridData);
        mainInterface.setLayout(layout);
    }

    private void setUpSidePane() {
        sidePane = new Composite(this.shell, SWT.NONE);
        GridLayout sidePaneLayout = new GridLayout();
        sidePaneLayout.numColumns = 1;
        sidePane.setLayoutData(new GridData(GridData.FILL_BOTH));
        sidePane.setLayout(sidePaneLayout);
        // sidePane.setSize(MIN_WIDTH_SIDE_PANE, MIN_HEIGHT_SIDE_PANE);
    }

    private void setUpWidgets() {
        setUpCanvas();
        setUpTable();
        setUpFeedBack();
        setUpCommandLine();
        setUpCalendar();
        setUpTaskList();
    }

    private void setUpCanvas() {
        // TODO Auto-generated method stub
        new Canvas(mainInterface, SWT.NONE);
    }

    private void setUpTable() {
        tableViewer = new TableViewer(mainInterface, SWT.MULTI | SWT.BORDER);
        tableViewer.setContentProvider(new ArrayContentProvider() );
        tableViewer.setLabelProvider(new LabelProvider());
        setUpTableColumns();
        Table table = tableViewer.getTable();
        table.setLinesVisible(true);
        table.setHeaderVisible(true);
    }

    // to refactor method
    private void setUpTableColumns() {

        TableViewerColumn column = setColumnHeader(HEADER_NAME_ID, COL_WIDTH_ID);
        column.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                Task task = (Task) element;
                String id = task.getId() + "";
                return id;
            }
        });
        column = setColumnHeader(HEADER_NAME_NAME, COL_WIDTH);
        column.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                Task task = (Task) element;
                return task.getName();
            }
        });

        column = setColumnHeader(HEADER_NAME_DUE, COL_WIDTH);
        column.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                Task task = (Task) element;
                String Due =  task.getDue().toString();
                if(Due == null){
                    return "<empty>";
                }
                return Due;
            }
        });

        column = setColumnHeader(HEADER_NAME_START, COL_WIDTH);
        column.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                Task task = (Task) element;
                String Start = task.getStart().toString();
                if(Start == null){
                    return "<empty>";
                }
                
                return Start;
            }
        });

        column = setColumnHeader(HEADER_NAME_END, COL_WIDTH);
        column.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                Task task = (Task) element;
                String End = task.getEnd().toString();
                if(End == null){
                    return "<empty>";
                }
                return End;
            }
        });

        column = setColumnHeader(HEADER_NAME_TAGS, COL_WIDTH);
        column.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                Task task = (Task) element;
                List<String> tags = task.getTags();
                String tag = "";
                for (int index = 0; index < tags.size(); index++) {
                    tag = tag + tags.get(index);
                }
                return tag;
            }
        });

        Table table = tableViewer.getTable();
        table.setLayoutData(new GridData(GridData.FILL_BOTH));
    }

    private TableViewerColumn setColumnHeader(String headerName, int colWidth) {
        TableViewerColumn columnViewer = new TableViewerColumn(tableViewer,
                SWT.NONE);
        TableColumn column = columnViewer.getColumn();
        column.setText(headerName);
        column.setWidth(colWidth);
        column.setResizable(true);
        column.setMoveable(true);
        return columnViewer;
    }

    private void setUpFeedBack() {
        feedback = new Label(mainInterface, SWT.SINGLE);
        feedback.setText("Feedback");
        GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
        feedback.setLayoutData(gridData);

    }

    private void setUpCommandLine() {
        commandLine = new Text(mainInterface, SWT.SINGLE | SWT.BORDER_SOLID);
        GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
        commandLine.setLayoutData(gridData);
        commandLine.setText(MESSAGE_TYPE_HERE);
        }

    private void setUpCalendar() {
        // TODO Auto-generated method stub
        calendar = new DateTime(sidePane, SWT.CALENDAR);
    }

    private void setUpTaskList() {
        // TODO Auto-generated method stub
        taskList = new Label(sidePane, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL
                | SWT.READ_ONLY);
        GridData gridData = new GridData(GridData.FILL_BOTH);
        taskList.setLayoutData(gridData);
        // to change
       /** String textToSet = "UPCOMING TASK:" + LINE_SEPARATOR
                + "1. Implement task list" + LINE_SEPARATOR + "SOMEDAY:"
                + LINE_SEPARATOR + "1. End world hunger";
        taskList.setText(textToSet);
        **/
    }
}