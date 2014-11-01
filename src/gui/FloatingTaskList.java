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
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import database.Task;

public class FloatingTaskList implements Observer {

    private static final String DOT_AND_SPACE = ". ";
    private static final String LINE_SEPARATOR = System
            .getProperty("line.separator");

    private static FloatingTaskList floatingTaskList;
    private FontRegistry registry;
    private StyledText list;

    private FloatingTaskList(Composite parent, int style) {
        buildControls(parent);
    }

    public static FloatingTaskList getInstance(Composite parent, int style) {
        if (floatingTaskList == null) {
            floatingTaskList = new FloatingTaskList(parent, style);
        }
        return floatingTaskList;
    }

    public static FloatingTaskList getInstance() {
        assert (floatingTaskList != null);
        return floatingTaskList;
    }

    public void initialise() {
        populatesData();
    }

    @Override
    public void update(Observable o, Object arg) {
        populatesData();
    }

    public void populatesData() {
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

    private void buildControls(Composite parent) {
        formatRegistry(parent);
        buildList(parent);
    }

    private void formatRegistry(Composite parent) {

        registry = new FontRegistry(parent.getDisplay());
        FontData[] fontData = new FontData[] { new FontData("Times New Roman",
                11, SWT.NORMAL) };
        registry.put("list headers", fontData);
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
        list.setFont(registry.get("list headers"));
    }

}
