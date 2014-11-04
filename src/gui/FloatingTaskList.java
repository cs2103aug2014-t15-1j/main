package gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import logic.Processor;

import org.eclipse.jface.resource.FontRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import database.Task;

/**
 * The FloatingTaskList is a user interface that shows all of the user's tasks that are without any dates
 * The Observer interface is implemented, FloatingTaskList observes logic.Processor
 * The singleton pattern is applied so that any instance of the class refers to the same instance
 * @author Sharon, Yao Xiang
 *
 */
public class FloatingTaskList implements Observer {

    private static final String DOT_AND_SPACE = ". ";
    private static final String LINE_SEPARATOR = System
            .getProperty("line.separator");

    private static FloatingTaskList floatingTaskList;
    private FontRegistry fontRegistry;
    private StyledText list;

    /**
     * Creates the FloatingTaskList and the text shown to the user on startup
     * @param parent Composite in which the interface is located
     */
    private FloatingTaskList(Composite parent) {
        buildControls(parent);
    }
    
    /**
     * Returns an instance of the FloatingTaskList. 
     * The method creates a new instance of FloatingTaskList, if it does not exist
     * @param parent Composite where the interface is located
     * @return an instance of FloatingTaskList 
     */
    public static FloatingTaskList getInstance(Composite parent) {
        if (floatingTaskList == null) {
            floatingTaskList = new FloatingTaskList(parent);
        }
        return floatingTaskList;
    }
    
    /**
     * Returns an instance of the FloatingTaskList. This method should be called after creating the FloatingTaskList 
     * interface. Otherwise, an assertion failure will occur
     * @return an instance of FloatingTaskList
     */
    public static FloatingTaskList getInstance() {
        assert (floatingTaskList != null);
        return floatingTaskList;
    }
    
    /**
     * Initializes the FloatingTaskList interface by setting the text to be shown at startUp. 
     * This method is to be called at application startup
     */
    public void initialise() {
        populatesData();
    }
    
    /**
     * Updates the list of floating tasks displayed in the FloatingTaskList interface
     */
    @Override
    public void update(Observable objectObserved, Object arg) {
        if (arg.equals("updateui") || arg.equals("sidepane")) {
            populatesData();
        }
    }
    
    private void buildControls(Composite parent) {
        getFontRegistry();
        buildLabel(parent);
        buildList(parent);
    }
    
    private void getFontRegistry() {
        fontRegistry = Fonts.getRegistry();
        }
    
    private void buildLabel(Composite parent) {
        StyledText floatingTask = new StyledText(parent, SWT.SINGLE |
                                                       SWT.READ_ONLY);
        floatingTask.setText("SOMEDAY");
        floatingTask.setFont(fontRegistry.get("list headers"));

        // floatingTask.setImage(imageRegistry.get("someday"));
        GridData centeredGridData = new GridData(SWT.CENTER, SWT.FILL, true,
                true);
        floatingTask.setLayoutData(centeredGridData);
    }

    private void buildList(Composite parent) {
        list = new StyledText(parent, SWT.MULTI | SWT.READ_ONLY |
                                      SWT.LEFT_TO_RIGHT | SWT.V_SCROLL);
        setLayout();
        format();
    }

    private void setLayout() {
        GridData gridData = new GridData(GridData.FILL_BOTH);
        gridData.heightHint = 150;
        list.setLayoutData(gridData);
        list.setWordWrap(true);
    }

    private void format() {
        Color white = list.getDisplay().getSystemColor(SWT.COLOR_WHITE);
        list.setBackground(white);
        list.setWordWrap(true);
        list.setEnabled(true);
        list.setFont(fontRegistry.get("list"));
    }
    
    private void populatesData() {
        List<Task> floatingTasks = getFloatingTasks();

        String floatingList = getStringList(floatingTasks);
        list.setText(floatingList);
    }

    private List<Task> getFloatingTasks() {
        Processor processor = Processor.getInstance();
        List<Task> tasks = processor.fetchFloatingTasks();
        tasks = getTopTen(tasks);
        return tasks;
    }

    private List<Task> getTopTen(List<Task> tasks) {
        int size = tasks.size();
        if (size <= 10) {
            return tasks;
        }
        return shortenList(tasks);
    }

    private List<Task> shortenList(List<Task> tasks) {
        List<Task> topTen = new ArrayList<Task>();
        for (int index = 0; index < 10; index++) {
            Task currTask = tasks.get(index);
            topTen.add(index, currTask);
        }
        return topTen;
    }

    private String getStringList(List<Task> tasks) {
        if (!isValid(tasks)) {
            return "Nothing to display" + LINE_SEPARATOR;
        }
        String list = "";

        list = list + changeFloatingListToString(tasks);
        return list;
    }

    private String changeFloatingListToString(List<Task> tasks) {
        int size = tasks.size();
        String list = "";
        for (int index = 0; index < size; index++) {
            Task currentTask = tasks.get(index);
            String iD = currentTask.getId() + "";
            String name = currentTask.getName();
            if (name == null || name.isEmpty() || name.equals("null")) {
                name = "";
            }
            list = list + iD + DOT_AND_SPACE + name + LINE_SEPARATOR;
        }

        return list;
    }

    private boolean isValid(List<Task> tasks) {
        if (tasks.isEmpty()) {
            return false;
        }
        return true;
    }

}
