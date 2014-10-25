package gui;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * This classes calls all the 'update-able' user Interfaces ( Task Table,
 * TaskList, Calendar ) to update themselves.
 * 
 * @author Sharon
 *
 */
public class UpdateUI implements Observer {
    public UpdateUI(List tasks, boolean isDateType) {
        updateObservers(tasks, isDateType);
    }

    private void updateObservers(@SuppressWarnings("rawtypes") List list,
                                 boolean isDateType) {

        new TaskListUI(list);

        if (isDateType) {
            new DateTableUI(list);
            return;
        }
        new TaskTableUI(list);
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
