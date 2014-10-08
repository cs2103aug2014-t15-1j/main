package Storage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import Parser.Parser;

public class DataFile {
    
    final private static String FILENAME = "Data";
    
    // A task can only exist in one of the three (todo, done, deleted)
    // arrayLists at any one time.
    // allTasks contains references to all tasks, sorted by id
    private static ArrayList<Task> allTasks = new ArrayList<Task>();
    private static ArrayList<Task> toDoTasks = new ArrayList<Task>();
    private static ArrayList<Task> doneTasks = new ArrayList<Task>();
    private static ArrayList<Task> deletedTasks = new ArrayList<Task>();
    
    // ArrayList getters
    // Aids in searching and display commands
    public ArrayList<Task> getAllTasks() {
        return allTasks;
    }
    public ArrayList<Task> getToDoTasks() {
        return toDoTasks;
    }
    public ArrayList<Task> getDoneTasks() {
        return doneTasks;
    }
    public ArrayList<Task> getDeletedTasks() {
        return deletedTasks;
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
    
    // Populate task arrayLists with data from system file
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
    // Includes todo, done, and deleted tasks
    public Task getTask(int id) {
        Task task = searchTaskById(allTasks, id);
        return task;
    }
    
    // Given id, return task object from given arrayList
    private Task searchTaskById(ArrayList<Task> tasks, int id) {
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
    
    // Refreshes system file with new data in arrayLists
    public boolean updateFile() {
        // To write to file
        // Deleted tasks are not written to file
        String newFileText = stringifyToDoAndDoneTasks();
        return writeToFile(newFileText);
    }
    
    // Stringifies tasks which have not been deleted,
    // in preparation for storage on file
    private String stringifyToDoAndDoneTasks() {
        String text = ""; 
        for (int i = 0; i < toDoTasks.size(); i++) {
            Task currentTask = toDoTasks.get(i);
            text += currentTask.stringify() + "\n";
        }
        for (int i = 0; i < doneTasks.size(); i++) {
            Task currentTask = doneTasks.get(i);
            text += currentTask.stringify() + "\n";
        }
        return text;
    }
    
    // Writes todo and done arrayLists to system file
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
    // Task object is removed from todo or done arrayList,
    // and added to deleted arrayList
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
    // All tasks are removed from todo and done
    // arrayLists, and added to deleted arrayList
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
        return true;
    }
}
// TODO fill in gaps and remove extraneous parts in Processor.java