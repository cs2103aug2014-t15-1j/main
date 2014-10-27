package gui;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import logic.Processor;

import org.eclipse.swt.custom.StyledText;

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
                name = "";
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

    @Override
    public void update(Observable o, Object arg) {
        populatesData();
    }

    public void populatesData() {
        List<Task> upcomingTasks = getUpcomingTasks();
        List<Task> floatingTasks = getFloatingTasks();
        StyledText upcomingTasksList = getUpcomingList();
        StyledText floatingTasksList = getFloatingList();

        String upcomingList = getStringList(upcomingTasks, false);
        String floatingList = getStringList(floatingTasks, true);
        // to implement overdue tasks in red

        upcomingTasksList.setText(upcomingList);
        floatingTasksList.setText(floatingList);
    }
}
