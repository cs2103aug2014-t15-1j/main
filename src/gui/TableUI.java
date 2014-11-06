package gui;

import java.text.SimpleDateFormat;
import java.util.Date;
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
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import database.DateTime;
import database.Task;


/**
 * TableUI table interface located that is located in a Tab Folder.
 * This class is shows the default format of building the table
 * The singleton pattern is used so that only one interface of the table is used.
 */
//@author A0118846W
public class TableUI{
    
    private static final String HEADER_NAME_ID = "Id";
    private static final String HEADER_NAME_NAME = "Name";
    private static final String HEADER_NAME_DUE = "Due/End";
    private static final String HEADER_NAME_START = "Start";
    private static final String HEADER_NAME_TAGS = "Tags";
    private static final String HEADER_NAME_STATUS = "Status";
    private static final String PARA_STATUS_BLOCK = "Blocked Date";

    private static final String CELL_EMPTY_DATE = "<no date>";

    private static final String PARA_STATUS_DELETED = "Deleted";
    private static final String PARA_STATUS_TODO = "To do";
    private static final String PARA_STATUS_DONE = "Done";

    // NOTE: 350 is able to fit up to 20 chars
    private static final int COL_WIDTH = 175;

    // NOTE: 50 is able to fit ID, two digit numbers, "." - XX.
    private static final int COL_WIDTH_ID = 35;

    // NOTE:250 is able to fit both date and time - DD/MM/YYYY HHMM
    private static final int COL_WIDTH_DATE = 150;

    // NOTE: 150 is just right for all statuses - To Do, Done, Deleted
    private static final int COL_WIDTH_STATUS = 175;
    
    private static int colourIndex = 1;
    private static Color[] tableColours = new Color[7];

    private TableViewer tableViewer;
    private CTabFolder folder;
    private static FontRegistry registry;
    
    /**
     * Creates an instance of the TaskTableUI
     * @param parent tab folder where the TaskTableUI is located in
     */
    public TableUI(CTabFolder parent) {
        folder = parent;
        buildControls(parent);
    }

    public TableViewer getTableViewer() {
        return tableViewer;
    }
    
    private void buildControls(Composite parent) {
        getRegistry(parent);
        buildLabel(parent);
        buildTable(parent);
    }

    private void getRegistry(Composite parent) {
        registry = Fonts.getRegistry();
    }

    private void buildLabel(Composite parent) {
        StyledText tableTitle = new StyledText(parent, SWT.READ_ONLY);
        tableTitle.setText("Results:");
        tableTitle.setEnabled(false);
        tableTitle.setFont(registry.get("title"));
    }

    private void buildTable(Composite parent) {
        tableViewer = new TableViewer(parent, SWT.BORDER |
                                              SWT.FULL_SELECTION);

        tableViewer.setContentProvider(new ArrayContentProvider());
        tableViewer.setLabelProvider(new LabelProvider());
        setUpColumns(parent);

        Table table = tableViewer.getTable();
        table.setBackground(new Color(Display.getCurrent(), 255, 255, 255 - colourIndex*15));
        colourIndex++;
        table.setLinesVisible(true);
        table.setHeaderVisible(true);
        table.setEnabled(true);
        table.setFont(registry.get("table"));
    }

    private void setUpColumns(Composite parent) {
        Table table = tableViewer.getTable();
        int scrollBarWidth = table.getVerticalBar().getSize().x;
        int screenWidth = parent.getDisplay().getBounds().width;
        int spaceLeft;
        int[] colWidths; // ID, start, due, tags, type/status
        if (screenWidth<1920) {
            int tableWidth = (int) (screenWidth * 0.8 * 0.70); //very rough approximation
            spaceLeft = tableWidth - 30 - 120 - 120 - 120 - (120 + 40) - scrollBarWidth;
            colWidths = new int[]{30, 120, 120, 120, 160};
        } else {
            int tableWidth = (int) (screenWidth * 0.8 * 0.78); //very rough approximation
            spaceLeft = tableWidth - 40 - 150 - 150 - 200 - (150 + 50) - scrollBarWidth;
            colWidths = new int[]{40, 150, 150, 200, 200};
        }
        
        TableViewerColumn column = setColumnHeader(HEADER_NAME_ID, colWidths[0]);
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
            
            @Override
            public Color getForeground(Object element){
                if(element instanceof Task){
                    Task task = (Task) element;
                    Display display = Display.getCurrent();
                    DateTime due = task.getDue();
                    if(due == null){
                        return Display.getCurrent().getSystemColor(SWT.COLOR_BLACK);
                    }else if(isOverdue(due) && !task.isDone() ){
                        return Colours.getDeletedColor();
                    }
                }
                return Display.getCurrent().getSystemColor(SWT.COLOR_BLACK);
            }
        });
        
        column = setColumnHeader(HEADER_NAME_NAME, spaceLeft);
        column.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                if (element instanceof Task) {
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
            
            @Override
            public Color getForeground(Object element){
                if(element instanceof Task){
                    Task task = (Task) element;
                    Display display = Display.getCurrent();
                    DateTime due = task.getDue();
                    if(due == null){
                        return Display.getCurrent().getSystemColor(SWT.COLOR_BLACK);
                    }else if(isOverdue(due) && !task.isDone() ){
                        return Colours.getDeletedColor();
                    }
                }
                return Display.getCurrent().getSystemColor(SWT.COLOR_BLACK);
            }
        });

        column = setColumnHeader(HEADER_NAME_START, colWidths[1]);
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
            
            @Override
            public Color getForeground(Object element){
                if(element instanceof Task){
                    Task task = (Task) element;
                    Display display = Display.getCurrent();
                    DateTime due = task.getDue();
                    if(due == null){
                        return Display.getCurrent().getSystemColor(SWT.COLOR_BLACK);
                    }else if(isOverdue(due) && !task.isDone() ){
                        return Colours.getDeletedColor();
                    }
                }
                return Display.getCurrent().getSystemColor(SWT.COLOR_BLACK);
            }
        });

        column = setColumnHeader(HEADER_NAME_DUE, colWidths[2]);

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
            
            @Override
            public Color getForeground(Object element){
                if(element instanceof Task){
                    Task task = (Task) element;
                    Display display = Display.getCurrent();
                    DateTime due = task.getDue();
                    if(due == null){
                        return Display.getCurrent().getSystemColor(SWT.COLOR_BLACK);
                    }else if(isOverdue(due) && !task.isDone() ){
                        return Colours.getDeletedColor();
                    }
                }
                return Display.getCurrent().getSystemColor(SWT.COLOR_BLACK);
            }
        });

        column = setColumnHeader(HEADER_NAME_TAGS, colWidths[3]);
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
                    for (String tempTag : tags) {
                        tag += tempTag + " ";
                    }
                    return tag;
                }
                return "";
            }
            
            @Override
            public Color getForeground(Object element){
                if(element instanceof Task){
                    Task task = (Task) element;
                    Display display = Display.getCurrent();
                    DateTime due = task.getDue();
                    if(due == null){
                        return Display.getCurrent().getSystemColor(SWT.COLOR_BLACK);
                    }else if(isOverdue(due) && !task.isDone() ){
                        return Colours.getDeletedColor();
                    }
                }
                return Display.getCurrent().getSystemColor(SWT.COLOR_BLACK);
            }
        });

        column = setColumnHeader(HEADER_NAME_STATUS, colWidths[4]);
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
                    } else if(task.isToDo()){
                        Status = PARA_STATUS_TODO;
                    }else{
                        Status = PARA_STATUS_BLOCK;
                    }
                    return Status;
                }
                return "";
            }
            

            @Override
            public Color getBackground(Object element){
                if(element instanceof Task){
                    Task task = (Task) element;
                    Display display = Display.getCurrent();
                    if(task.isDeleted()){
                        return Colours.getDeletedColor();
                    }else if(task.isBlock()){
                        return display.getSystemColor(SWT.COLOR_BLUE);
                    }else if(task.isToDo()){
                        return Colours.getToDoColor();
                    }else if(task.isDone()){
                        return display.getSystemColor(SWT.COLOR_GRAY);
                    }
                }
                return Display.getCurrent().getSystemColor(SWT.COLOR_WHITE);
            }
            
            @Override
            public Color getForeground(Object element){
                if(element instanceof Task){
                    Task task = (Task) element;
                    Display display = Display.getCurrent();
                    return Display.getCurrent().getSystemColor(SWT.COLOR_WHITE);
                }
                return Display.getCurrent().getSystemColor(SWT.COLOR_BLACK);
            }
            
            @Override
            public Font getFont(Object element){
                if(element instanceof Task){
                    Task task = (Task) element;
                    Display display = Display.getCurrent();
                    return registry.get("table element");
                }
                return null;
            }
        });

        table.setLayoutData(new GridData(GridData.FILL_BOTH));

        table.addListener(SWT.RESIZE, new Listener() {

            @Override
            public void handleEvent(Event event) {
                switch (event.type) {
                    case SWT.RESIZE:
                      //  resizeCol();
                        break;

                    default:
                        break;
                }
            }
        });

    }

    protected boolean isOverdue(DateTime date) {
            DateTime now = getTodaysDate();
            if (date.isEarlierThan(now)) {
                return true;
            }
            return false;
    }

    private DateTime getTodaysDate() {
        Date date = new Date();
        String nowDate = new SimpleDateFormat("dd/MM/YYYY").format(date);
        String nowTime = new SimpleDateFormat("hhmm").format(date);
        DateTime today = new DateTime(nowDate, nowTime);
        return today;
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
        TableViewerColumn columnViewer = new TableViewerColumn(tableViewer, SWT.LEFT);
        TableColumn column = columnViewer.getColumn();
        column.setText(headerName);
        column.setWidth(colWidth);
        column.setResizable(true);
        column.setMoveable(true);
        return columnViewer;
    }

}
