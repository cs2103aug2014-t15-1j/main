package Storage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import Parser.Parser;

public class DataFile {
    private static ArrayList<Task> toDoTasks;
    private static ArrayList<Task> doneTasks;
    private static ArrayList<Task> deletedTasks;
    
    public void initialize() throws FileNotFoundException, IOException {
        final String filename = "Data";
        File file = new File(filename);
        if(file.exists()) {
            getTasksFromFile(file);
        } else {
            file.createNewFile();
        }
    }
    
    private void getTasksFromFile(File file) throws FileNotFoundException {
        Scanner scanner = new Scanner(file);
        while (scanner.hasNextLine()) {
            String unparsedText = scanner.nextLine();
            Task newTask = Parser.parseRawText(unparsedText);
            if (newTask.isDeleted()) {
                deletedTasks.add(newTask);
            } else if (newTask.isDone()) {
                doneTasks.add(newTask);
            } else {
                toDoTasks.add(newTask);
            }
        }
    }
    
    public Task read(int id) {
        for (int i = 0; i < toDoTasks.size(); i++) {
            if (toDoTasks.get(i).getId() == id) {
                return toDoTasks.get(i);
            }
        }
        for (int i = 0; i < doneTasks.size(); i++) {
            if (doneTasks.get(i).getId() == id) {
                return doneTasks.get(i);
            }
        }
        for (int i = 0; i < deletedTasks.size(); i++) {
            if (deletedTasks.get(i).getId() == id) {
                return deletedTasks.get(i);
            }
        }
        
        return null;    // If not found
    }
    
    public boolean write(Task task) throws IOException {
        toDoTasks.add(task);
        String newFileText = "";
        
        for (int i = 0; i < toDoTasks.size(); i++) {
            Task currentTask = toDoTasks.get(i);
            newFileText += changeToString(currentTask) + "\n";
        }
        
        final String filename = "Data";
        File file = new File(filename);
        if(!file.exists()) {
            file.createNewFile();
        }
        
        FileWriter newFile = new FileWriter(file, false);
        newFile.write(newFileText);
        newFile.close();
        return true;
    }
    
    // TODO
    public String changeToString(Task task) {
        return "";
    }
    
    public boolean deleteTask(int id) {
        for (int i = 0; i < toDoTasks.size(); i++) {
            Task currentTask = toDoTasks.get(i); 
            if (currentTask.getId() == id) {
                currentTask.setDeleted(true);
                return true;
            }
        }
        
        for (int i = 0; i < doneTasks.size(); i++) {
            Task currentTask = doneTasks.get(i); 
            if (currentTask.getId() == id) {
                currentTask.setDeleted(true);
                return true;
            }
        }
        return false;
    }
    
    // TODO
    public ArrayList<Task> getAllTasks() {
        
    }
}
