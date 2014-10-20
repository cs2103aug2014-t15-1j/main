package gui;

import java.util.List;

import logic.Processor;

import org.eclipse.jface.viewers.TableViewer;

import database.Task;

/*
 * This class updates the task table interface whenever a change is made
 */
public class TaskTableUI {

    public TaskTableUI() {
        update();
    }

    private void update() {
        TableViewer table = getTable();
        Processor processor = Processor.getInstance();
        List<Task> timedTasks = processor.fetchTimedTasks();
        List<Task> floatingTasks = processor.fetchFloatingTasks();
        if (isValid(timedTasks) && isValid(floatingTasks)) {

            List<Task> tasks = mergeTasks(timedTasks, floatingTasks);
            setTasks(table, tasks);

        } else if (isValid(floatingTasks)) {
            setTasks(table, floatingTasks);
        } else if (!isValid(timedTasks)) {
            setTasks(table, timedTasks);
        }
    }

    private void setTasks(TableViewer table, List<Task> tasks) {
        Object[] tasksArray = tasks.toArray();

        table.setInput(tasksArray);
        table.refresh();
    }

    private TableViewer getTable() {
        SetUp setUp = SetUp.getInstance();
        TableViewer table = setUp.getTableViewer();
        return table;
    }

    private boolean isValid(List<Task> tasks) {
        if (tasks == null || tasks.size() == 0 || tasks.isEmpty()) {
            return false;
        }

        return true;
    }

    private List<Task> mergeTasks(List<Task> timedTasks,
                                  List<Task> floatingTasks) {
        int sizeFloatingTasks = floatingTasks.size();

        for (int index = 0; index < sizeFloatingTasks; index++) {
            timedTasks.add(floatingTasks.get(index));
        }

        return timedTasks;
    }
}
