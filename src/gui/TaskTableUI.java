package gui;

import java.util.List;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.custom.CTabFolder;

import database.Task;

/*
 * This class updates the task table interface whenever a change is made
 */
public class TaskTableUI {

    public void update(List<Task> tasks) {
        TableViewer table = getTable();
        setTasks(table, tasks);
    }

    /**
     * Adds the specified task list to table
     * 
     * @param table
     * @param tasks
     */
    private void setTasks(TableViewer table, List<Task> tasks) {
        if (table.equals(null)) {
            return;
        }
        table.setInput(tasks);
        setFocus();
    }

    /**
     * Gets the instance of table viewer for taskTable from SetUp
     * 
     * @return
     */
    private TableViewer getTable() {
        SetUp setUp = SetUp.getInstance();
        if (setUp.getTableViewer().equals(null)) {
            return null;
        }
        TableViewer table = setUp.getTableViewer();
        return table;
    }

    /**
     * Opens task table tab
     */
    private void setFocus() {
        SetUp setUp = SetUp.getInstance();
        CTabFolder tabs = setUp.getTabFolder();
        tabs.setSelection(0);
    }
}
