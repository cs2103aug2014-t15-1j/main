package logic;

import gui.DateTimeStub;
import gui.TaskStub;

import java.util.ArrayList;
import java.util.List;

import database.BlockDate;
import database.DataFile;
import database.DateTime;
import database.Task;

public class DataFileStub extends DataFile {

    List<Task> tasks;
    public DataFileStub() {
        tasks = new ArrayList<Task>();
        tasks.add(new TaskStub("name", new DateTimeStub("", ""),
                               new DateTimeStub("", ""), new DateTimeStub("", ""), new ArrayList<String>()));
    }

    @Override
    public List<Task> getToDoTasks() {
        return tasks;
    }

    @Override
    public List<Task> getDoneTasks() {
        return tasks;
    }

    @Override
    public List<Task> getDeletedTasks() {
        return tasks;
    }

    @Override
    public List<Task> getAllTasks() {
        return tasks;
    }

    @Override
    public Task getTask(int id) {
        return tasks.get(0);
    }

    @Override
    public boolean addNewTask(Task task) {
        return true;
    }

    @Override
    public boolean updateTaskInfo(Task task, String name, DateTime due,
                                  DateTime start, List<String> tags) {
        return true;
    }

    @Override
    public boolean updateTaskInfo(int id, String name, DateTime due,
                                  DateTime start, List<String> tags) {
        return true;
    }

    @Override
    public boolean deleteTask(Task task) {
        return true;
    }

    @Override
    public boolean deleteTask(int id) {
        return true;
    }

    @Override
    public boolean restoreTask(Task task) {
        return true;
    }

    @Override
    public boolean restoreTask(int id) {
        return true;
    }

    @Override
    public boolean wipeTask(Task task) {
        return true;
    }

    @Override
    public boolean wipeTask(int id) {
        return true;
    }

    @Override
    public boolean wipeFile() {
        return true;
    }

    @Override
    public boolean toDoTask(Task task) {
        return true;
    }

    @Override
    public boolean toDoTask(int id) {
        return true;
    }

    @Override
    public boolean doneTask(Task task) {
        return true;
    }

    @Override
    public boolean doneTask(int id) {
        return true;
    }

    @Override
    public boolean addNewBD(BlockDate bD) {
        return true;
    }

    @Override
    public boolean deleteBD(int id) {
        return true;
    }

    @Override
    public boolean deleteBD(BlockDate bD) {
        return true;
    }

    @Override
    public boolean restoreBD(int id) {
        return true;
    }

    @Override
    public boolean restoreBD(BlockDate bD) {
        return true;
    }

    @Override
    public boolean wipeBD(BlockDate bD) {
        return true;
    }

    @Override
    public boolean wipeBD(int id) {
        return true;
    }
}
