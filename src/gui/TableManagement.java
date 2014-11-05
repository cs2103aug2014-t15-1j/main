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
    private static CTabFolder folder; 
    private static List<TableViewer> tables;
    
    public TableManagement(){
     folder = TableComposite.getTabFolder();
     tables = TableComposite.getTables();
     addTableListeners();
    }
    
    public void update(List<Task> tasks) {
        List<Task> todaysTasks = new ArrayList<Task>();
        List<Task> tomorrowsTasks = new ArrayList<Task>();
        List<Task> upcomingTasks = new ArrayList<Task>();
        List<Task> floatingTasks = new ArrayList<Task>();
        
        int size = tasks.size();
        for(int index = 0; index < size; index++){
            Task currTask = tasks.get(index);
            if(isToday(currTask.getDue())){
                todaysTasks.add(currTask);
            }else if(isTomorrow(currTask.getDue())){
                tomorrowsTasks.add(currTask);
            }else if(isUpcoming(currTask.getDue())){
                upcomingTasks.add(currTask);
            } else{
                floatingTasks.add(currTask);
            }
        }
        
        tables.get(0).setInput(todaysTasks);
        tables.get(1).setInput(tomorrowsTasks);
        tables.get(2).setInput(upcomingTasks);
        tables.get(3).setInput(floatingTasks);
        
    }
    
    public void setTableSelection(Task taskToSelect, List<Task> tasks){
        TableViewer table = getTable(taskToSelect);
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
        
        if(date == null){
            // floating
           return tables.get(3);
        }else if(isToday(date)){
            //today
            return tables.get(0);
        }else if(isTomorrow(date)){
            // tomorrow
            return tables.get(1);
        }else{
            // upcoming
            return tables.get(3);
        }
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
    
    private  void addTableListeners() {
        Display display = folder.getDisplay();
        display.addFilter(SWT.KeyDown, new Listener() {

            public void handleEvent(Event event) {
                if (((event.stateMask & SWT.CTRL) == SWT.CTRL) &&
                    (event.keyCode == 'd')) {
                    int index = folder.getSelectionIndex();
                    if(index < 4){
                        folder.setSelection(index+1);
                    }else{
                        folder.setSelection(0);
                    }
                } else if (event.keyCode == SWT.F1) {
                    new HelpDialog(folder.getShell());
                }
            }
        });
    }

}
