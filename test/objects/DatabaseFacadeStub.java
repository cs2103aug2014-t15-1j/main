package objects;

import java.util.ArrayList;
import java.util.List;

import database.DatabaseFacade;
import database.DateTime;
import database.Task;
import database.TaskType;

public class DatabaseFacadeStub extends DatabaseFacade {

    List<Task> tasks;

    public DatabaseFacadeStub() {
        tasks = new ArrayList<Task>();
        tasks.add(new TaskStub("name", new DateTimeStub("", ""),
                new DateTimeStub("", ""), new DateTimeStub("", ""),
                new ArrayList<String>(), TaskType.TODO));
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
    public List<Task> getBlockTasks() {
        List<Task> blockTasks = new ArrayList<Task>();
        return blockTasks;
    }

    @Override
    public List<Task> getAllTasks() {
        return tasks;
    }

    @Override
    public Task getTask(int id) {
        if (id > 0) {
            return tasks.get(0);
        } else {
            return null;
        }
    }

    @Override
    public boolean add(Task task) {
        if (task != null) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean edit(Task task, String name, DateTime due, DateTime start,
                        List<String> tags) {
        if (task != null) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean edit(int id, String name, DateTime due, DateTime start,
                        List<String> tags) {
        if (id > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean delete(Task task) {
        if (task != null) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        if (id > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean restore(Task task) {
        if (task != null) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean restore(int id) {
        if (id > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean permanentlyDelete(Task task) {
        if (task != null) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean permanentlyDelete(int id) {
        if (id > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean resetData() {
        return true;
    }

    @Override
    public boolean markToDo(Task task) {
        if (task != null) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean markToDo(int id) {
        if (id > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean markDone(Task task) {
        if (task != null) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean markDone(int id) {
        if (id > 0) {
            return true;
        } else {
            return false;
        }
    }
}
