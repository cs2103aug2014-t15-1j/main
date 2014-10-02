package Storage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import Parser.Parser;

public class DataFile {
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
            if (!newTask.isDone()) { // Branch predictor at work here
                toDoTasks.add(newTask);
            } else {
                doneTasks.add(newTask);
            }
        }
        scanner.close();
    }
    
    // TODO refactor functions into one single function
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
        if (!task.isDone()) {
            toDoTasks.add(task);
        } else {
            doneTasks.add(task);
        }
        String newFileText = ""; // To write to file
        
        for (int i = 0; i < toDoTasks.size(); i++) {
            Task currentTask = toDoTasks.get(i);
            newFileText += changeToString(currentTask) + "\n";
        }
        
        for (int i = 0; i < doneTasks.size(); i++) {
            Task currentTask = doneTasks.get(i);
            newFileText += changeToString(currentTask) + "\n";
        }
        
        final String FILENAME = "Data";
        File file = new File(FILENAME);
        if(!file.exists()) {
            file.createNewFile();
        }
        
        FileWriter newFile = new FileWriter(file, false);
        newFile.write(newFileText);
        newFile.close();
        return true;
    }
    
    public String changeToString(Task task) {
        String stringifiedTask = "Name: " + task.getName() + " ";
        stringifiedTask += "More: " + task.getMore() + " ";
        stringifiedTask += "Due: " + task.getDue() + " ";
        stringifiedTask += "Start: " + task.getStart() + " ";
        stringifiedTask += "End: " + task.getEnd() + " ";
        stringifiedTask += "Priority: " + task.getPriority() + " ";
        stringifiedTask += task.getTags() + " ";
        stringifiedTask += task.isDone() ? "#Done" : "#ToDo";
        
        return stringifiedTask;
    }
    
    public boolean deleteTask(int id) {
        for (int i = 0; i < toDoTasks.size(); i++) {
            Task currentTask = toDoTasks.get(i); 
            if (currentTask.getId() == id) {
                currentTask.setDeleted(true);
                toDoTasks.remove(i);
                deletedTasks.add(currentTask);
                return true;
            }
        }
        
        for (int i = 0; i < doneTasks.size(); i++) {
            Task currentTask = doneTasks.get(i); 
            if (currentTask.getId() == id) {
                currentTask.setDeleted(true);
                doneTasks.remove(i);
                deletedTasks.add(currentTask);
                return true;
            }
        }
        return false;
    }
}
