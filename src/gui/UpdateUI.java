package gui;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import database.Task;

/**
 * This classes calls all the 'update-able' user Interfaces ( Task Table,
 * TaskList, Calendar ) to update themselves.
 * 
 * @author Sharon
 *
 */
public class UpdateUI implements Observer {
    public UpdateUI(List<Task> tasks) {
        updateObservers(tasks);
    }

    private void updateObservers(List<Task> tasks) {
        new TaskTableUI(tasks);
        new TaskListUI(tasks);
    }

    /**
     * public void update(Observable observable, Object object) {
     * updateObservers() }
     **/

    @Override
    public void update(Observable arg0, Object arg1) {
        new TaskTableUI();
        new TaskListUI();
    }
}
