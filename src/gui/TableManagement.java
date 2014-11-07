package gui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import database.DateTime;
import database.Task;

public class TableManagement {
    private static final int INDEX_TODO = 0;
    private static final int INDEX_TODAY = 1;
    private static final int INDEX_TOMORROW = 2;
    private static final int INDEX_UPCOMING = 3;
    private static final int INDEX_SOMEDAY = 4;
    private static final int INDEX_DONE = 5;
    private static final int INDEX_BLOCK= 6;
    private static final int INDEX_SEARCH = 7;
    
    private static CTabFolder folder; 
    private static List<TableViewer> tables;
    
    public TableManagement(){
     folder = TableComposite.getTabFolder();
     tables = TableComposite.getTables();
    }
    
    public void updateTable(List<Task> tasks){
        Task task = tasks.get(0);
        getTable(task);
    }
    
    public void refreshTables(){
            List<Task> toDo = ResultGenerator.getToDoTasks();
            List<Task> floating = ResultGenerator.getFloatingTasks();
            List<Task> done = ResultGenerator.getDoneTasks();
            List<Task> blocked = ResultGenerator.getBlockTasks();
            List<Task> todays = ResultGenerator.getTodayTasks();
            List<Task> tomorrow = ResultGenerator.getTomorrowsTasks();
            List<Task> upcoming = ResultGenerator.getNextWeekTasks();
            List<Task> search = ResultGenerator.getSearchTasks();
            
            updateToDoTable(toDo);
            updateFloatingTaskTable(floating);
            updateDoneTable(done);
            updateBlockTable(blocked);
            updateTodayTable(todays);
            updateTomorrowTable(tomorrow);
            updateUpcomingTable(upcoming);
            updateSearchTable(search);
    }
    
    public void updateTable(List<Task> tasks, int index){
        tables.get(index).setInput(tasks);
    }
    
    public void updateFloatingTaskTable(List<Task> tasks){
        tables.get(INDEX_SOMEDAY).setInput(tasks);
    }
    
    public void updateToDoTable(List<Task> tasks){
        tables.get(INDEX_TODO).setInput(tasks);
    }
    
    public void updateDoneTable(List<Task> tasks){
        tables.get(INDEX_DONE).setInput(tasks);
    }

    public void updateBlockTable(List<Task> blockTasks){
       tables.get(INDEX_BLOCK).setInput(blockTasks);
    }
    
    public void updateTodayTable(List<Task> todaysTasks){
        tables.get(INDEX_TODAY).setInput(todaysTasks);
     }
    
    public void updateTomorrowTable(List<Task> tomorrowsTasks){
        tables.get(INDEX_TOMORROW).setInput(tomorrowsTasks);
    }
    
    public void updateUpcomingTable(List<Task> upcomingTasks){
        tables.get(INDEX_UPCOMING).setInput(upcomingTasks);
    }
    
    public void updateSomedayTable(List<Task> floatingTasks){
        tables.get(INDEX_SOMEDAY).setInput(floatingTasks);
    }
    
    public void updateSearchTable(List<Task> searchResults){
        tables.get(INDEX_SEARCH).setInput(searchResults);
    }
    
    public void updateTimedTable(List<Task> tasks) {
        List<Task> todaysTasks = new ArrayList<Task>();
        List<Task> tomorrowsTasks = new ArrayList<Task>();
        List<Task> upcomingTasks = new ArrayList<Task>();
        
        int size = tasks.size();
        for(int index = 0; index < size; index++){
            Task currTask = tasks.get(index);
            if(isToday(currTask.getDue())){
                todaysTasks.add(currTask);
            }else if(isTomorrow(currTask.getDue())){
                tomorrowsTasks.add(currTask);
            }else{
                upcomingTasks.add(currTask);
            }
        }
        
        tables.get(INDEX_TODAY).setInput(todaysTasks);
        tables.get(INDEX_TOMORROW).setInput(tomorrowsTasks);
        tables.get(INDEX_UPCOMING).setInput(upcomingTasks);
    }
    
    public void setTableSelectionByIndex(int index){
        folder.setSelection(index);
    }
    
    public void setTableSelectionByTask(Task taskToSelect){
        TableViewer table = getTable(taskToSelect);
        List<Task> tasks = getTableContent(taskToSelect);
        setElementSelection(taskToSelect, tasks, table);
    }

    /**
     * Selects an element in the table. Default selection is the first element
     */
    private void setElementSelection(Task taskToSelect, List<Task> tasks, TableViewer tableViewer) {
        
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
    
    private TableViewer getTable(Task task){
        DateTime date = task.getDue();
        
        if(task.isToDo()){
            setTableSelection(INDEX_TODO);
            return tables.get(INDEX_TODO);
        }else if(task.isDone()){
            setTableSelection(INDEX_DONE);
            return tables.get(INDEX_DONE);
        }else if(task.isBlock()){
            setTableSelection(INDEX_BLOCK);
            return tables.get(INDEX_BLOCK);
        }
        
        if(task.isFloating()){
           return tables.get(INDEX_SOMEDAY);
        }else if(isToday(date)){
            return tables.get(INDEX_TODAY);
        }else if(isTomorrow(date)){
            return tables.get(INDEX_TOMORROW);
        }else{
            return tables.get(INDEX_UPCOMING);
        }
    }

    private void setTableSelection(int index) {
        folder.setSelection(index);
    }
    

    private List<Task> getTableContent(Task task) {
        List<Task> floating = ResultGenerator.getFloatingTasks();
        List<Task> timed = ResultGenerator.getTimedTasks();
        List<Task> done = ResultGenerator.getDoneTasks();
        List<Task> blocked = ResultGenerator.getBlockTasks();
        List<Task> todo = ResultGenerator.getToDoTasks();
        
        if(task.isToDo()){
            return todo;
        }else if(task.isBlock()){
            return blocked;
        }else if(task.isDone()){
            return done;
        }else if(task.isFloating()){
            return floating;
        }

        return null;
    }
    
    private List<Task> getDoneList(List<Task> timed) {
        List<Task> timedList = new ArrayList<Task>();
        int size = timed.size();
        for(int index = 0; index < size; index++){
            Task currTask = timed.get(index);
            if(currTask.isDone()){
                timedList.add(currTask);
            }
        }
        return timedList;
    }

    private boolean isToday(DateTime date) {
        DateTime now =  getNow();
        if(!isFloating(date) && isEqualDate(date, now)){
            return true;          
     }
        return false;
    }

    private boolean isTomorrow(DateTime date) {     
           DateTime tomorrow =  getTomorrow();
           if(!isFloating(date) && isEqualDate(date, tomorrow)){
               return true;          
        }
        return false;
    }
    
    private boolean isUpcoming(DateTime date) {     
     if(isFloating(date) || isToday(date) || (isTomorrow(date))){
            return false;          
     }
     return true;
 }


    private boolean isFloating(DateTime date) {
        if(date == null){
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
    
    private DateTime getTomorrow() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date tomorrow = calendar.getTime();
        String tomorrowDate = new SimpleDateFormat("dd/MM/YYYY").format(tomorrow);
        String tomorrowTime = new SimpleDateFormat("hhmm").format(tomorrow);
        DateTime tomorrowsDate = new DateTime(tomorrowDate, tomorrowTime);
        return tomorrowsDate;
    }
}
