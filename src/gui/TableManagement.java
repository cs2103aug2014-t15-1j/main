package gui;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.custom.CTabFolder;

import database.DateTime;
import database.Task;

public class TableManagement {
    private static final int INDEX_ALL = 0;
    private static final int INDEX_TODAY = 1;
    private static final int INDEX_TOMORROW = 2;
    private static final int INDEX_UPCOMING = 3;
    private static final int INDEX_SOMEDAY = 4;
    private static final int INDEX_RESULT = 5;

    private static final String TAB_NAME_ALL = "all";
    private static final String TAB_NAME_TODAY = "today";
    private static final String TAB_NAME_TOMORROW = "tomorrow";
    private static final String TAB_NAME_UPCOMING = "upcoming";
    private static final String TAB_NAME_SOMEDAY = "someday";
    private static final String TAB_NAME_RESULT = "result";

    private static CTabFolder folder;
    private static List<TableViewer> tables;

    public TableManagement() {
        folder = TableComposite.getTabFolder();
        tables = TableComposite.getTables();
    }
    
    /**
     * Updates all the information in all tables, except for the result table. 
     * It then sets the tab selection to show All table
     */
    public void refreshTables() {
        List<Task> all = ResultGenerator.getAllTasks();
        List<Task> todays = ResultGenerator.getTodayTasks();
        List<Task> tomorrow = ResultGenerator.getTomorrowsTasks();
        List<Task> upcoming = ResultGenerator.getUpcomingTasks();
        List<Task> floating = ResultGenerator.getFloatingTasks();

        updateTable(all, INDEX_ALL);
        updateTable(todays, INDEX_TODAY);
        updateTable(tomorrow, INDEX_TOMORROW);
        updateTable(upcoming, INDEX_UPCOMING);
        updateTable(floating, INDEX_SOMEDAY);
        
    }

    public void setDefaultSelection() {
        // default selection
        folder.setSelection(INDEX_ALL);
    }
    
    public void updateResultTable(List<Task> tasks){
        tables.get(INDEX_RESULT).setInput(tasks);
        folder.setSelection(INDEX_RESULT);
    }
    
    public void updateTableByName(String tabName, List<Task> tasks){
        int index = getTableIndex(tabName);
        tables.get(index).setInput(tasks);
        folder.setSelection(index);
    }
    
    public void setTableSelectionByIndex(int index) {
        if (folder == null) {
            return;
        }
        folder.setSelection(index);
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
        
        folder.setSelection(index);
    }
    
    /**
     * Selects a specific table element in the table
     * @param taskToSelect task that is to be selected
     * @param tabName tabName of the table where the task should be selected from
     */
    public void setTableSelectionByTask(Task taskToSelect, String tabName) {
        int index = getTableIndex(tabName);
        TableViewer table = tables.get(index);
        List<Task> tasks = getTableContent(taskToSelect);
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
        }

        // default
        return INDEX_ALL;
    }
    
    private List<Task> getTableContent(Task task) {
        List<Task> today = ResultGenerator.getTodayTasks();
        List<Task> tomorrow = ResultGenerator.getTomorrowsTasks();
        List<Task> upcoming = ResultGenerator.getUpcomingTasks();
        List<Task> floating = ResultGenerator.getFloatingTasks();
        List<Task> all = ResultGenerator.getAllTasks();

        if (task == null) {
            return null;
        }

        DateTime due = task.getDue();

        if (task.isFloating()) {
            return floating;
        } else if (isToday(due)) {
            return today;
        } else if (isTomorrow(due)) {
            return tomorrow;
        } else if (isUpcoming(due)) {
            return upcoming;
        } else {
            return all;
        }
    }

    private boolean isToday(DateTime date) {
        DateTime now = getNow();
        if (!isFloating(date) && isEqualDate(date, now)) {
            return true;
        }
        return false;
    }

    private boolean isTomorrow(DateTime date) {
        DateTime tomorrow = getTomorrowDate();
        if (!isFloating(date) && isEqualDate(date, tomorrow)) {
            return true;
        }
        return false;
    }

    private boolean isUpcoming(DateTime date) {
        if (isFloating(date) || isToday(date) || (isTomorrow(date))) {
            return false;
        }
        return true;
    }

    private boolean isFloating(DateTime date) {
        if (date == null || date.isEmpty()) {
            return true;
        }
        return false;
    }

    private boolean isEqualDate(DateTime date, DateTime dateToCompare) {
        return dateToCompare.getDate().equals(date.getDate());
    }

    private DateTime getNow() {
        Date date = new Date();
        String nowDate = new SimpleDateFormat("dd/MM/YYYY").format(date);
        String nowTime = new SimpleDateFormat("hhmm").format(date);
        DateTime now = new DateTime(nowDate, nowTime);
        return now;

    }

    private DateTime getTomorrowDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date tomorrow = calendar.getTime();
        String tomorrowDate = new SimpleDateFormat("dd/MM/YYYY")
                .format(tomorrow);
        String tomorrowTime = new SimpleDateFormat("hhmm").format(tomorrow);
        DateTime tomorrowsDate = new DateTime(tomorrowDate, tomorrowTime);
        return tomorrowsDate;
    }

}
