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
    @SuppressWarnings("rawtypes")
    public UpdateUI(List<Task> tasks, boolean isDateType) {
        updateObservers(tasks, isDateType);
    }

    @SuppressWarnings("rawtypes")
    private void updateObservers(List<Task> list, boolean isDateType) {
        /**
         * if (isDateType) { new DateTableUI(list); return; }
         **/
        new TaskListUI();
        new TaskTableUI();
    }

    public void update(Observable observable, Object object) {
        updateObservers();
    }

    private void updateObservers() {
        new TaskListUI();
        new TaskTableUI();
    }
}
