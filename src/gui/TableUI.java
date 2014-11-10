package gui;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import objects.DateTime;
import objects.Task;

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
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;


/**
 * TableUI table interface located that is located in a Tab Folder.
 * This class is shows the default format of building the table
 */
public class TableUI{
    
    private static final String HEADER_NAME_ID = "Id";
    private static final String HEADER_NAME_NAME = "Task Description";
    private static final String HEADER_NAME_DUE = "Due/End";
    private static final String HEADER_NAME_START = "Start";
    private static final String HEADER_NAME_TAGS = "Tags";
    private static final String HEADER_NAME_STATUS = "Status";
    private static final String PARA_STATUS_BLOCK = "Blocked Date";

    private static final String CELL_EMPTY_DATE = "-";

    private static final String PARA_STATUS_DELETED = "Deleted";
    private static final String PARA_STATUS_TODO = "To do";
    private static final String PARA_STATUS_DONE = "Done";

    private static int colourIndex = 1;
    private TableViewer tableViewer;
    private static FontRegistry registry;
    
    /**
     * Creates an instance of the TaskTableUI
     * @param parent tab folder where the TaskTableUI is located in
     */
    public TableUI(CTabFolder parent) {
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
        Color color = new Color(Display.getCurrent(), 255 - colourIndex*10, 255 - colourIndex, 255 - colourIndex*13);
        table.setBackground(color);
        colourIndex++;
        color.dispose();
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
                    DateTime due = task.getDue();
                    if(due == null){
                        return Display.getCurrent().getSystemColor(SWT.COLOR_BLACK);
                    }else if(isOverdue(due) && !task.isDone() ){
                        return TableColours.getOverdueColour();
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
                    DateTime due = task.getDue();
                    if(due == null){
                        return Display.getCurrent().getSystemColor(SWT.COLOR_BLACK);
                    }else if(isOverdue(due) && !task.isDone() ){
                        return TableColours.getOverdueColour();
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
                    DateTime due = task.getDue();
                    if(due == null){
                        return Display.getCurrent().getSystemColor(SWT.COLOR_BLACK);
                    }else if(isOverdue(due) && !task.isDone() ){

                        return TableColours.getOverdueColour();
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
                    DateTime due = task.getDue();
                    if(due == null){
                        return Display.getCurrent().getSystemColor(SWT.COLOR_BLACK);
                    }else if(isOverdue(due) && !task.isDone() ){
                        return TableColours.getOverdueColour();
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
                    DateTime due = task.getDue();
                    if(due == null){
                        return Display.getCurrent().getSystemColor(SWT.COLOR_BLACK);
                    }else if(isOverdue(due) && !task.isDone() ){
                        return TableColours.getOverdueColour();
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
                    if(task.isDeleted()){
                        return TableColours.getDeletedColor();
                    }else if(task.isBlock()){
                        return TableColours.getBlockDateColor();
                    }else if(task.isToDo()){
                        return TableColours.getToDoColor();
                    }else if(task.isDone()){
                        return TableColours.getDoneColor();
                    }
                }
                return Display.getCurrent().getSystemColor(SWT.COLOR_WHITE);
            }
            
        });

        table.setLayoutData(new GridData(GridData.FILL_BOTH));

    }

    protected boolean isOverdue(DateTime date) {
            DateTime now = getTodaysDate();
            if (date.isEarlierThan(now) || date.equals(now)) {
                return true;
            }
            return false;
    }

    private DateTime getTodaysDate() {
        Date date = new Date();
        String nowDate = new SimpleDateFormat("dd/MM/YYYY").format(date);
        String nowTime = new SimpleDateFormat("HHmm").format(date);
        DateTime today = new DateTime(nowDate, nowTime);
        return today;
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
