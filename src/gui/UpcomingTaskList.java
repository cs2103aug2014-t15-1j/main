package gui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import logic.Processor;

import org.eclipse.jface.resource.FontRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import database.DateTime;
import database.Task;

public class UpcomingTaskList implements Observer {

    public static final String LINE_SEPARATOR = System
            .getProperty("line.separator");
    private static final String DOT_AND_SPACE = ". ";
    private FontRegistry registry;
    private StyledText list;
    private static UpcomingTaskList upcomingTaskList;

    public static UpcomingTaskList getInstance() {
        assert (upcomingTaskList != null);
        return upcomingTaskList;
    }

    public static UpcomingTaskList getInstance(Composite parent, int style) {
        if (upcomingTaskList == null) {
            upcomingTaskList = new UpcomingTaskList(parent, style);
        }

        return upcomingTaskList;
    }

    public void initialise() {
        populatesData();
    }

    private UpcomingTaskList(Composite parent, int style) {
        buildControls(parent);
    }

    private void buildControls(Composite parent) {
        formatRegistry(parent);
        buildList(parent);
    }

    private void buildList(Composite parent) {
        list = new StyledText(parent, SWT.MULTI | SWT.READ_ONLY |
                                      SWT.LEFT_TO_RIGHT | SWT.V_SCROLL);

        list.setFont(registry.get("list"));
        setListLayout(list);
        format(list);
    }

    private void format(StyledText list) {
        Display display = list.getShell().getDisplay();
        Color white = display.getSystemColor(SWT.COLOR_WHITE);
        list.setBackground(white);
        list.setEnabled(true);
    }

    private void formatRegistry(Composite parent) {
        registry = new FontRegistry(parent.getDisplay());
        FontData[] fontData = new FontData[] { new FontData("Courier New",
                11, SWT.NORMAL) };
        registry.put("list", fontData);
    }

    private String getIndentation(String iD) {
        int length = iD.length();
        int toIndent = length;
        String indent = " ";
        for (int index = 0; index < toIndent; index++) {
            indent = indent + indent;
        }
        return indent;
    }

    private DateTime getTodaysDate() {
        Date date = new Date();
        String nowDate = new SimpleDateFormat("dd/MM/YYYY").format(date);
        String nowTime = new SimpleDateFormat("hhmm").format(date);
        DateTime today = new DateTime(nowDate, nowTime);
        return today;
    }

    private List<Task> getTopTen(List<Task> tasks) {
        int size = tasks.size();
        if (size <= 10) {
            return tasks;
        }
        return shortenList(tasks);
    }

    private List<Task> getUpcomingTasks() {
        Processor processor = Processor.getInstance();
        List<Task> tasks = processor.fetchTimedTasks();
        tasks = getTopTen(tasks);
        return tasks;
    }

    private boolean isOverDue(DateTime date) {
        DateTime now = getTodaysDate();
        if (date.isEarlierThan(now)) {
            return true;
        }
        return false;
    }

    private boolean isValid(List<Task> tasks) {
        if (tasks.isEmpty()) {
            return false;
        }
        return true;
    }

    public void populatesData() {
        List<Task> upcomingTasks = getUpcomingTasks();
        addTasksToUpcomingList(upcomingTasks);
    }

    private void addTasksToUpcomingList(List<Task> tasks) {
        if (!isValid(tasks)) {
            list.setText("Nothing to display" + LINE_SEPARATOR);
            return;
        }
        list.setText("");
        int size = tasks.size();
        for (int index = 0; index < size; index++) {
            Task currTask = tasks.get(index);
            String name = currTask.getName();
            String iD = currTask.getId() + "";
            DateTime due = currTask.getDue();
            String indentation = getIndentation(iD);
            if (isNotValidName(name)) {
                name = "empty name";
            }
            list.append(iD + DOT_AND_SPACE + name + LINE_SEPARATOR);
            StyleRange range = new StyleRange();
            // due should never be empty, because timed tasks should have a due
            assert (!due.isEmpty());
            addDue(due, indentation, range);

        }
    }

    private void addDue(DateTime due, String indentation, StyleRange range) {
        String dueString = due.toString();
        if (isOverDue(due)) {
            list.append(indentation + "Due: " + dueString);
            range.start = list.getText().length() - dueString.length();
            range.length = dueString.length();
            range.foreground = list.getDisplay().getSystemColor(SWT.COLOR_RED);
            list.setStyleRange(range);
            list.append(LINE_SEPARATOR);
        } else {
            list.append(indentation + "Due: " + dueString + LINE_SEPARATOR);
        }
    }

    private boolean isNotValidName(String name) {
        return name == null || name.isEmpty() || name.equals("null");
    }

    private void setListLayout(StyledText list) {
        GridData gridData = new GridData(GridData.FILL_BOTH);
        gridData.heightHint = 500;
        list.setLayoutData(gridData);
        list.setWordWrap(true);
    }

    private List<Task> shortenList(List<Task> tasks) {
        List<Task> topTen = new ArrayList<Task>();
        for (int index = 0; index < 10; index++) {
            Task currTask = tasks.get(index);
            topTen.add(index, currTask);
        }
        return topTen;
    }

    @Override
    public void update(Observable o, Object arg) {
        populatesData();
    }

}
