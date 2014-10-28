package gui;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import logic.Processor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;

import database.DateTime;
import database.Task;

/*
 * This class updates the TaskList Interface whenever a change is made. 
 */
public class TaskListUI implements Observer {
    private static final String DOT_AND_SPACE = ". ";
    private static final String LINE_SEPARATOR = System
            .getProperty("line.separator");

    public TaskListUI() {
        populatesData();
    }

    @Override
    public void update(Observable o, Object arg) {
        populatesData();
    }

    public void populatesData() {
        List<Task> upcomingTasks = getUpcomingTasks();
        List<Task> floatingTasks = getFloatingTasks();

        StyledText upcomingTasksList = getUpcomingList();
        StyledText floatingTasksList = getFloatingList();

        addTasksToUpcomingList(upcomingTasksList, upcomingTasks);

        String floatingList = getStringList(floatingTasks, true);
        floatingTasksList.setText(floatingList);
    }

    private StyledText getUpcomingList() {
        SetUp setUp = SetUp.getInstance();
        StyledText taskList = setUp.getUpcomingTasksList();
        return taskList;
    }

    private StyledText getFloatingList() {
        SetUp setUp = SetUp.getInstance();
        StyledText taskList = setUp.getFloatingTasksList();
        return taskList;
    }

    private List<Task> getUpcomingTasks() {
        Processor processor = Processor.getInstance();
        List<Task> tasks = processor.fetchTimedTasks();
        return tasks;
    }

    private List<Task> getFloatingTasks() {
        Processor processor = Processor.getInstance();
        List<Task> tasks = processor.fetchFloatingTasks();
        return tasks;
    }

    private void addTasksToUpcomingList(StyledText upcomingTasks,
                                        List<Task> tasks) {
        if (!isValid(tasks)) {
            upcomingTasks.setText("Nothing to display" + LINE_SEPARATOR);
            return;
        }

        int size = tasks.size();
        for (int index = 0; index < size; index++) {
            Task currTask = tasks.get(index);
            String name = currTask.getName();
            String iD = currTask.getId() + "";
            DateTime due = currTask.getDue();
            if (name == null || name.isEmpty() || name.equals("null")) {
                name = "empty name";
            }

            upcomingTasks.append(iD + DOT_AND_SPACE + name + LINE_SEPARATOR);
            StyleRange range = new StyleRange();
            if (!due.isEmpty()) {
                String dueString = due.toString();
                if (isOverDue(due)) {
                    upcomingTasks.append("Due: " + dueString);
                    range.start = upcomingTasks.getText().length() -
                                  dueString.length();
                    range.length = dueString.length();
                    range.foreground = upcomingTasks.getDisplay()
                            .getSystemColor(SWT.COLOR_RED);
                    upcomingTasks.setStyleRange(range);
                    upcomingTasks.append(LINE_SEPARATOR);
                } else {
                    upcomingTasks.append("Due: " + dueString + LINE_SEPARATOR);
                }
            } else {
                DateTime start = currTask.getStart();
                String startString = start.toString();
                if (isOverDue(start)) {
                    upcomingTasks.append("Start: " + startString +
                                         LINE_SEPARATOR);
                    range.start = upcomingTasks.getText().length() -
                                  startString.length();
                    range.length = startString.length();
                    range.foreground = upcomingTasks.getDisplay()
                            .getSystemColor(SWT.COLOR_RED);
                    upcomingTasks.setStyleRange(range);
                    upcomingTasks.append(LINE_SEPARATOR);
                } else {
                    upcomingTasks.append("Start: " + startString +
                                         LINE_SEPARATOR);
                }
            }
        }
    }

    private boolean isOverDue(DateTime date) {
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

    private String getStringList(List<Task> tasks, boolean isFloating) {
        if (!isValid(tasks)) {
            return "Nothing to display" + LINE_SEPARATOR;
        }
        String list = "";
        if (isFloating) {
            list = list + changeFloatingListToString(tasks);
            return list;
        }
        list = list + changeTaskListToString(tasks);
        return list;
    }

    private String changeTaskListToString(List<Task> tasks) {
        int size = tasks.size();
        String list = "";
        for (int index = 0; index < size; index++) {
            Task currentTask = tasks.get(index);
            String iD = currentTask.getId() + "";
            String name = currentTask.getName();
            String due = currentTask.getDue().toString();
            String start = currentTask.getStart().toString();

            if (name == null || name.isEmpty() || name.equals("null")) {
                name = "empty name";
            }
            if (!due.isEmpty()) {
                list = list + iD + DOT_AND_SPACE + name + LINE_SEPARATOR +
                       "Due: " + due + LINE_SEPARATOR;
            } else {
                list = list + iD + DOT_AND_SPACE + name + LINE_SEPARATOR +
                       "Start: " + start + LINE_SEPARATOR;
            }

            list += LINE_SEPARATOR;
        }

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
