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
    
    private static ArrayList<Task> toDoTasks = new ArrayList<Task>();
    private static ArrayList<Task> doneTasks = new ArrayList<Task>();
    private static ArrayList<Task> deletedTasks = new ArrayList<Task>();
    
    public ArrayList<Task> getToDoTasks() {
        return toDoTasks;
    }
    public ArrayList<Task> getDoneTasks() {
        return doneTasks;
    }
    public ArrayList<Task> getDeletedTasks() {
        return deletedTasks;
    }
    
    public void initialize() {
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
    
    private void getTasksFromFile(File file) {
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String unparsedText = scanner.nextLine();
                Task newTask = Parser.parseRawText(unparsedText);
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
    
    public Task getTask(int id) {
        Task task = searchTaskById(toDoTasks, id);
        if (task != null) {
            return task;
        }
        task = searchTaskById(doneTasks, id);
        if (task != null) {
            return task;
        }
        task = searchTaskById(deletedTasks, id);
        if (task != null) {
            return task;
        }
        return null;    // If not found
    }
    
    private Task searchTaskById(ArrayList<Task> tasks, int id) {
        for (Task tempTask : tasks) {
            if (tempTask.getId() == id) {
                return tempTask;
            }
        }
        return null;
    }
    
    
    // Used when adding a new task
    public boolean write(Task task) {
        if (!task.isDone()) {
            toDoTasks.add(task);
        } else {
            doneTasks.add(task);
        }
        return updateFile();
    }

    private boolean updateFile() {
        // To write to file
        // Deleted tasks are not written to file
        String newFileText = stringifyToDoAndDoneTasks();
        return writeToFile(newFileText);
    }

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
    
    // TODO
    public boolean edit(int id) {
        return true;
    }
    
    public boolean deleteTask(int id) {
        for (int i = 0; i < toDoTasks.size(); i++) {
            Task currentTask = toDoTasks.get(i); 
            if (currentTask.getId() == id) {
                currentTask.setDeleted(true);
                toDoTasks.remove(i);
                deletedTasks.add(currentTask);
                updateFile();
                return true;
            }
        }
        
        for (int i = 0; i < doneTasks.size(); i++) {
            Task currentTask = doneTasks.get(i); 
            if (currentTask.getId() == id) {
                currentTask.setDeleted(true);
                doneTasks.remove(i);
                deletedTasks.add(currentTask);
                updateFile();
                return true;
            }
        }
        return false;
    }
}
// TODO change read() to getTask(), write() to saveTask()
// TODO fill in gaps and remove extraneous parts in Processor.java
// TODO adding existing element back into arraylist = ? [Processor.java using write(existingFile)]
// TODO use a hash table with id look up instead of arraylists