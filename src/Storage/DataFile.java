package Storage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import Parser.Parser;

public class DataFile {
    
    final private static String FILENAME = "Data";
    
    // A task can only exist in one of the three 
    // (to-do/done/deleted) lists at any one time.
    // allTasks contains references to all tasks, sorted by id
    private static List<Task> allTasks = new ArrayList<Task>();
    private static List<Task> toDoTasks = new ArrayList<Task>();
    private static List<Task> doneTasks = new ArrayList<Task>();
    private static List<Task> deletedTasks = new ArrayList<Task>();
    
    // Task list getters
    // Aids in searching and display commands
    public List<Task> getAllTasks() {
        return Collections.unmodifiableList(allTasks);
    }
    public List<Task> getToDoTasks() {
        return Collections.unmodifiableList(toDoTasks);
    }
    public List<Task> getDoneTasks() {
        return Collections.unmodifiableList(doneTasks);
    }
    public List<Task> getDeletedTasks() {
        return Collections.unmodifiableList(deletedTasks);
    }
    
    // Constructor
    public DataFile() {
        File file = new File(FILENAME);
        if(file.exists() && allTasks.isEmpty()) {
            getTasksFromFile(file);
        } else {
            try {
                file.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    
    // Populate task lists with data from system file
    private void getTasksFromFile(File file) {
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String unparsedText = scanner.nextLine();
                Task newTask = Parser.parseToTask(unparsedText);
                allTasks.add(newTask);
                if (!newTask.isDone()) { // Branch predictor at work here
                    toDoTasks.add(newTask); // to-do tasks are stored in file first
                } else {
                    doneTasks.add(newTask);
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    // Given id, return task object
    // Includes to-do, done, and deleted tasks
    public Task getTask(int id) {
        Task task = searchTaskById(allTasks, id);
        return task;
    }
    
    // Given id, return task object from given list
    private Task searchTaskById(List<Task> tasks, int id) {
        for (Task task : tasks) {
            if (task.getId() == id) {
                return task;
            }
        }
        return null;
    }
    
    // Used to add a new task
    public boolean addNewTask(Task task) {
        toDoTasks.add(task);
        allTasks.add(task);
        return updateFile();
    }
    
    // Refreshes system file with current data in to-do and done lists
    // Deleted tasks are not written to file
    public boolean updateFile() {
        String updatedTaskInfo = getTaskInfo(toDoTasks);
        updatedTaskInfo += getTaskInfo(doneTasks);
        return writeToFile(updatedTaskInfo);
    }
    
    // Stores all Task attributes, of all Tasks in list, in single String 
    // For storage on file
    private String getTaskInfo(List<Task> tasks) {
        String taskInfo = ""; 
        for (Task task : tasks) {
            taskInfo += task.getFullInfo() + "\n";
        }
        return taskInfo;
    }
    
    // Writes to-do and done lists to system file
    private boolean writeToFile(String newFileText) {
        try {
            File file = new File(FILENAME);
            if(!file.exists()) {
                file.createNewFile();
            }
            
            FileWriter newFile = new FileWriter(file, false);
            newFile.write(newFileText);
            newFile.close();
            return true;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean editTask(int id, String name, String due, String start, String end, List<String> tags) {
        return editTaskById(id, name, due, start, end, tags);
    }
    
    public boolean editTask(Task task, String name, String due, String start, String end, List<String> tags) {
        return editTaskByObject(task, name, due, start, end, tags);
    }
    
    private boolean editTaskById(int id, String name, String due, String start, String end, List<String> tags) {
        Task task = searchTaskById(allTasks, id);
        return editTaskByObject(task, name, due, start, end, tags);
    }
    
    private boolean editTaskByObject(Task task, String name, String due, String start, String end, List<String> tags) {
        if (task == null) {
            return false; // Invalid ID
        }
        if (name != null) {
            task.setName(name);
        }
        if (due != null) {
            task.setDue(due);
        }
        if (start != null) {
            task.setStart(start);
        }
        if (end != null) {
            task.setEnd(end);
        }
        if (tags != null) {
            task.addTags(tags);
        }
        
        return updateFile();
    }
    
    // Given id or object, deletes task object
    // Task object is removed from to-do or done list,
    // and added to deleted list
    public boolean deleteTask(int id) {
        return deleteTaskById(id);
    }
    
    public boolean deleteTask(Task task) {
        return deleteTaskByObject(task);
    }
    
    private boolean deleteTaskById(int id) {
        Task task = searchTaskById(toDoTasks, id);
        if (task == null) {
            task = searchTaskById(doneTasks, id);
        }
        return deleteTaskByObject(task);
    }
    
    private boolean deleteTaskByObject(Task task) {
        if (task == null || task.isDeleted()) {
            return false; // Invalid ID or already deleted
        }
        
        task.setDeleted(true);
        deletedTasks.add(task);
        if (task.isDone()) {
            doneTasks.remove(task);
        } else {
            toDoTasks.remove(task);
        }
        return updateFile();
    }
    
    public boolean restoreTask(int id) {
        return restoreTaskById(id);
    }
    
    // Restores a deleted task given its id
    public boolean restoreTask(Task task) {
        return restoreTaskByObject(task);
    }
    private boolean restoreTaskById(int id) {
        Task task = searchTaskById(deletedTasks, id);
        return restoreTaskByObject(task);
    }
    
    private boolean restoreTaskByObject(Task task) {
        if (task == null || !task.isDeleted()) {
            return false; // Invalid ID or is not deleted task
        }
        
        task.setDeleted(false);
        deletedTasks.remove(task);
        if (task.isDone()) {
            doneTasks.add(task);
        } else {
            toDoTasks.add(task);
        }
        return updateFile();
    }
    
    
    
    public boolean wipeTask(int id) {
        return wipeTaskById(id);
    }
    
    public boolean wipeTask(Task task) {
        return wipeTaskByObject(task);
    }
    
    private boolean wipeTaskById(int id) {
        Task task = searchTaskById(allTasks, id);
        return wipeTaskByObject(task); 
    }
    
    private boolean wipeTaskByObject(Task task) {
        if (task == null) {
            return false; // Invalid ID
        }
        
        allTasks.remove(task);
        if (task.isDeleted()) {
            deletedTasks.remove(task);
        } else if (task.isDone()) {
            doneTasks.remove(task);
        } else {
            toDoTasks.remove(task);
        }
        task.wipeTask();
        return updateFile();
    }
    
    public boolean wipeFile() {
        allTasks.clear();
        toDoTasks.clear();
        doneTasks.clear();
        deletedTasks.clear();
        
        return updateFile();
    }
    
    public boolean toDoTask(int id) {
        return toDoTaskById(id);
    }
    
    public boolean toDoTask(Task task) {
        return toDoTaskByObject(task);
    }
    
    private boolean toDoTaskById(int id) {
        Task task = searchTaskById(allTasks, id);
        return toDoTaskByObject(task);
    }
    
    private boolean toDoTaskByObject(Task task) {
        if (task == null || (!task.isDeleted() && !task.isDone())) {
            return false; // Invalid ID or is undeleted to-do task
        }
        
        task.setDone(false);
        if (task.isDeleted()) {
            return restoreTask(task);
        }
        toDoTasks.add(task);
        doneTasks.remove(task);
        return updateFile();
    }
    
    public boolean doneTask(int id) {
        return doneTaskById(id);
    }
    
    public boolean doneTask(Task task) {
        return doneTaskByObject(task);
    }
    
    private boolean doneTaskById(int id) {
        Task task = searchTaskById(allTasks, id);
        return doneTaskByObject(task);
    }
    
    private boolean doneTaskByObject(Task task) {
        if (task == null || (!task.isDeleted() && task.isDone())) {
            return false; // Invalid ID or is undeleted done task
        }
        
        task.setDone(true);
        if (task.isDeleted()) {
            return restoreTask(task);
        }
        doneTasks.add(task);
        toDoTasks.remove(task);
        return updateFile();
    }
}
// TODO fill in gaps and remove extraneous parts in Processor.java