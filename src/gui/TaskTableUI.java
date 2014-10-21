package gui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.TableViewer;

import database.Task;

/*
 * This class updates the task table interface whenever a change is made
 */
public class TaskTableUI {

    public TaskTableUI(List<Task> tasks) {
        update(tasks);
    }

    private void update(List<Task> tasks) {
        TableViewer table = getTable();
        // Processor processor = Processor.getInstance();
        /**
         * List<Task> timedTasks = processor.fetchTimedTasks(); List<Task>
         * floatingTasks = processor.fetchFloatingTasks(); if
         * (isValid(timedTasks) && isValid(floatingTasks)) {
         * 
         * List<Task> tasks = mergeTasks(timedTasks, floatingTasks);
         * setTasks(table, tasks);
         * 
         * } else if (isValid(floatingTasks)) { setTasks(table, floatingTasks);
         * } else if (isValid(timedTasks)) { setTasks(table, timedTasks); }
         **/
        setTasks(table, tasks);
        // setTasks(table, processor.fetchFloatingTasks());
    }

    private void setTasks(TableViewer table, List<Task> tasks) {
        Object[] tasksArray = tasks.toArray();
        if (table.equals(null)) {
            return;
        }
        table.setInput(tasksArray);
        table.refresh();
    }

    private TableViewer getTable() {
        SetUp setUp = SetUp.getInstance();
        if (setUp.getTableViewer().equals(null)) {
            return null;
        }
        TableViewer table = setUp.getTableViewer();
        return table;
    }

    private boolean isValid(List<Task> tasks) {
        if (tasks.equals(null) || tasks.size() == 0 || tasks.isEmpty()) {
            return false;
        }

        return true;
    }

    private List<Task> mergeTasks(List<Task> timedTasks,
                                  List<Task> floatingTasks) {
        int sizeFloatingTasks = floatingTasks.size();
        int sizeTimedTasks = timedTasks.size();
        List<Task> list = new ArrayList<Task>();

        for (int index = 0; index < sizeTimedTasks; index++) {
            list.add(timedTasks.get(index));
        }

        for (int index = 0; index < sizeFloatingTasks; index++) {
            list.add(floatingTasks.get(index));
        }

        return list;
    }
}
