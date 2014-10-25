package gui;

import java.util.ArrayList;
import java.util.List;

import logic.Processor;

import org.eclipse.swt.custom.StyledText;

import database.DateTime;
import database.Task;

/*
 * This class updates the TaskList Interface whenever a change is made. 
 */
public class TaskListUI {
    private static final String DOT_AND_SPACE = ". ";
    private static final String LINE_SEPARATOR = System
            .getProperty("line.separator");
    private static final String NO_NAME = "<no name>";

    public TaskListUI() {
        update();
    }

    public TaskListUI(List<Task> tasks) {
        update(tasks);
    }

    private void update(List<Task> tasks) {
        StyledText upcomingTasksList = getUpcomingList();
        StyledText floatingTasksList = getFloatingList();
        List<Task> upcomingTasks = getUpcomingTasks(tasks);
        List<Task> floatingTasks = getFloatingTasks(tasks);
        String upcomingList = getStringList(upcomingTasks, false); // to
        // implement:
        // overdue
        // tasks
        // in
        // red

        String floatingList = getStringList(floatingTasks, true);
        upcomingTasksList.setText(upcomingList);
        floatingTasksList.setText(floatingList);
    }

    private void update() {
        StyledText upcomingTasksList = getUpcomingList();
        StyledText floatingTasksList = getFloatingList();
        List<Task> upcomingTasks = getUpcomingTasks();
        List<Task> floatingTasks = getFloatingTasks();
        String upcomingList = getStringList(upcomingTasks, false); // to
        // implement:
        // overdue
        // tasks
        // in
        // red

        String floatingList = getStringList(floatingTasks, true);
        upcomingTasksList.setText(upcomingList);
        floatingTasksList.setText(floatingList);
    }

    private List<Task> getUpcomingTasks() {

        Processor processor = Processor.getInstance();
        List<Task> tasks = processor.fetchFloatingTasks();
        return tasks;
    }

    private List<Task> getUpcomingTasks(List<Task> tasks) {
        int size = tasks.size();
        List<Task> upcomingTasks = new ArrayList<Task>();
        for (int index = 0; index < size; index++) {
            Task task = tasks.get(index);
            if (!isEmpty(task.getDue()) || !isEmpty(task.getStart()) ||
                !isEmpty(task.getEnd())) {
                upcomingTasks.add(task);
            }
        }
        return upcomingTasks;
    }

    private List<Task> getFloatingTasks() {
        Processor processor = Processor.getInstance();
        List<Task> tasks = processor.fetchFloatingTasks();
        return tasks;
    }

    private List<Task> getFloatingTasks(List<Task> tasks) {
        int size = tasks.size();
        List<Task> floatingTasks = new ArrayList<Task>();
        for (int index = 0; index < size; index++) {
            Task task = tasks.get(index);
            if (isEmpty(task.getDue()) && isEmpty(task.getStart()) &&
                isEmpty(task.getEnd())) {
                floatingTasks.add(task);
            }
        }
        return floatingTasks;
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

    private String getStringList(List<Task> tasks, boolean isFloating) {
        if (!isValid(tasks)) {
            return "Nothing to display" + LINE_SEPARATOR;
        }
        String list = "";
        if (isFloating) {
            list = list + changeFloatingListToString(tasks);
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
            if (name == null || name.isEmpty() || name.equals("null")) {
                name = NO_NAME;
            }
            list = list + iD + DOT_AND_SPACE + name + LINE_SEPARATOR;
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
                name = NO_NAME;
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

    private boolean isEmpty(DateTime dateTime) {
        if (dateTime.getDate().equals("") && dateTime.getDay() == 0 &&
            dateTime.getMonth() == 0 && dateTime.getTime().equals("") &&
            dateTime.getYear() == 0) {
            return true;
        }

        return false;
    }
}
