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
        if(file.exists()) {
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
                    toDoTasks.add(newTask);
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
        for (Task tempTask : tasks) {
            if (tempTask.getId() == id) {
                return tempTask;
            }
        }
        return null;
    }
    
    // Used to add a new task
    public boolean addTask(Task task) {
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
        for (Task tempTask : tasks) {
            taskInfo += tempTask.getFullInfo() + "\n";
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
    
    // Given id, deletes task object
    // Task object is removed from to-do or done list,
    // and added to deleted list
    public boolean deleteTask(int id) {
        Task tempTask = searchTaskById(allTasks, id);
        if (tempTask == null) {
            return false; // Invalid ID
        } else if (tempTask.isDeleted()) {
            return false; // Already deleted
        }
        
        tempTask.setDeleted(true);
        deletedTasks.add(tempTask);
        if (tempTask.isDone()) {
            doneTasks.remove(tempTask);
        } else {
            toDoTasks.remove(tempTask);
        }
        updateFile();
        return true;
    }
    
    // Deletes all task objects
    // All tasks are removed from to-do and done
    // lists, and added to deleted list
    public boolean deleteAll() {
        for (Task tempTask : allTasks) {
            if (!tempTask.isDeleted()) {
                tempTask.setDeleted(true);
                deletedTasks.add(tempTask);
                if (tempTask.isDone()) {
                    doneTasks.remove(tempTask);
                } else {
                    toDoTasks.remove(tempTask);
                }
            }
        }
        updateFile();
        return true;
    }
    
    // Restores a deleted task given its id
    public boolean restore(int id) {
        Task task = searchTaskById(deletedTasks, id);
        
        if (task == null) {
            return false; // Invalid id
        } else {
            deletedTasks.remove(task);
            if (task.isDone()) {
                doneTasks.add(task);
            } else {
                toDoTasks.add(task);
            }
            return true;
        }
    }
}
// TODO fill in gaps and remove extraneous parts in Processor.java