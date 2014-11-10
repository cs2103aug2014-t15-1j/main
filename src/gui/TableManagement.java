//@author A0118846W
package gui;

import java.util.List;

import objects.Task;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.custom.CTabFolder;
/**
 *This class manages the selection and updating of the Tables used to display the users task information
 */
public class TableManagement {
    // The index numbers of the different tables 
    private static final int INDEX_ALL = 0;
    private static final int INDEX_TODAY = 1;
    private static final int INDEX_TOMORROW = 2;
    private static final int INDEX_UPCOMING = 3;
    private static final int INDEX_SOMEDAY = 4;
    private static final int INDEX_RESULT = 5;
    // This represents that the tab should not change when selected
    private static final int INDEX_NO_CHANGE = -1;
    
    // The tab names of the corresponding tables
    private static final String TAB_NAME_TODAY = "today";
    private static final String TAB_NAME_TOMORROW = "tomorrow";
    private static final String TAB_NAME_UPCOMING = "upcoming";
    private static final String TAB_NAME_SOMEDAY = "someday";
    private static final String TAB_NAME_RESULT = "result";
    private static final String TAB_NAME_NO_CHANGE = "nochange";

    // The Tab folder that contains all the tables
    private static CTabFolder folder;
    
    // A List containing the table viewers of all the tables
    private static List<TableViewer> tables;
    
    private static ResultGenerator resultGenerator = ResultGenerator.getInstance();
    
    /**
     * Creates an instance of the TableManagement class
     */
    public TableManagement() {
        folder= TableComposite.getTabFolder();
        tables = TableComposite.getTables();
    }
    
    /**
     * Updates all the information in all tables, except for the result table. 
     * It then sets the tab selection to show All table
     */
    public void refreshTables() {
        List<Task> all = resultGenerator.getAllTasks();
        List<Task> todays = resultGenerator.getTodayTasks();
        List<Task> tomorrow = resultGenerator.getTomorrowsTasks();
        List<Task> upcoming = resultGenerator.getUpcomingTasks();
        List<Task> floating = resultGenerator.getFloatingTasks();

        updateTable(all, INDEX_ALL);
        updateTable(todays, INDEX_TODAY);
        updateTable(tomorrow, INDEX_TOMORROW);
        updateTable(upcoming, INDEX_UPCOMING);
        updateTable(floating, INDEX_SOMEDAY);
        
    }
    
    /**
     * Updates the table in the result tab. The method selects a result table
     * @param tasks Tasks that the table should be updated with
     */
    public void updateResultTable(List<Task> tasks){
        assert(tasks!=null);
        tables.get(INDEX_RESULT).setInput(tasks);
        setSelection(INDEX_RESULT);
    }
    
    /**
     * Updates a specific table by its tab name. 
     * @param tabName Tab name of the table to be updates
     * @param tasks Tasks that tables should be updated with
     */
    public void updateTableByName(String tabName, List<Task> tasks){
        int index = getTableIndex(tabName);
        tables.get(index).setInput(tasks);
        if(folder == null){
            return;
        }
        setSelection(index);
    }
    
    /**
     * Selects a table tab by a specific name
     * @param tabName
     */
    public void setTableSelectionByName(String tabName) {
        int index = getTableIndex(tabName);
        if (folder == null) {
            return;
        }
        if (index == INDEX_NO_CHANGE){
            return;
        }
        setSelection(index);
    }
    
    /**
     * Selects a specific table element in the table
     * @param taskToSelect task that is to be selected
     * @param tabName tabName of the table where the task should be selected from
     */
    public void setTableSelectionByTask(Task taskToSelect, String tabName) {
        int index = getTableIndex(tabName);
        if(index== INDEX_NO_CHANGE){
            index = folder.getSelectionIndex();
        }
        setSelection(index);
        TableViewer table = tables.get(index);
        List<Task> tasks = getTableContent(index);
        if (tasks == null) {
            return;
        }
        setElementSelection(taskToSelect, tasks, table);
    }
    
    private void updateTable(List<Task> tasks, int index) {
        tables.get(index).setInput(tasks);
    }
    
    /**
     * Selects an element in the table. Default selection is the first element
     */
    private void setElementSelection(Task taskToSelect, List<Task> tasks,
                                     TableViewer tableViewer) {

        int size = tasks.size();
        int indexToSelect = 0;
        for (int index = 0; index < size; index++) {
            Task currTask = tasks.get(index);
            if (currTask.equals(taskToSelect)) {
                indexToSelect = index;
                break;
            }
        }

        tableViewer.getTable().setSelection(indexToSelect);
    }
    
    private int getTableIndex(String tabName){
        if (tabName.equals(TAB_NAME_TODAY)) {
            return INDEX_TODAY;
        } else if (tabName.equals(TAB_NAME_TOMORROW)) {
            return INDEX_TOMORROW;
        } else if (tabName.equals(TAB_NAME_UPCOMING)) {
            return INDEX_UPCOMING;
        } else if (tabName.equals(TAB_NAME_SOMEDAY)) {
            return INDEX_SOMEDAY;
        } else if (tabName.equals(TAB_NAME_RESULT)) {
            return INDEX_RESULT;
        }else if(tabName.equals(TAB_NAME_NO_CHANGE)){
            return INDEX_NO_CHANGE;
        }

        // default
        return INDEX_ALL;
    }
    
    private List<Task> getTableContent(int index){
        switch(index){
            case INDEX_TODAY:
                return resultGenerator.getTodayTasks();
            case INDEX_TOMORROW:
                return resultGenerator.getTomorrowsTasks();
            case INDEX_UPCOMING:
                return resultGenerator.getUpcomingTasks();
            case INDEX_SOMEDAY:
                return resultGenerator.getFloatingTasks();
            default:
                return resultGenerator.getAllTasks();
        }
    }
    
    private void setSelection(int index) {
        if(folder==null){
            return;
        }
        folder.setSelection(index);
    }
}
