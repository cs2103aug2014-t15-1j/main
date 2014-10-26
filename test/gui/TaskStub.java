package gui;

import java.util.ArrayList;
import java.util.List;

import database.DateTime;
import database.Task;

/*
 * This class is to be used for testing ONLY.
 * TaskStub allows for dependency injection of Task objects.
 * 
 */

public class TaskStub extends Task {

    private static int newId = 1;
    // removed final modifier for testing purposes
    private int ID;
    private String name;
    private DateTime due = new DateTime();
    private DateTime start = new DateTime();
    private DateTime end = new DateTime();
    private List<String> tags = new ArrayList<String>();
    private boolean done;
    private boolean deleted = false;

    public TaskStub(String name, DateTime due, DateTime start, DateTime end,
            List<String> tags) {
        super(name, due, start, end, tags);
        setName(name);
        setDue(due);
        setStart(start);
        setEnd(end);
        setTags(tags);
    }

    public TaskStub(Task existingTask) {
        super(existingTask);
        this.ID = existingTask.getId();
        this.name = existingTask.getName();
        this.due = existingTask.getDue();
        this.start = existingTask.getStart();
        this.end = existingTask.getEnd();
        this.deleted = existingTask.isDeleted();
        this.done = existingTask.isDone();
    }
    /*** ACCESSORS ***/

    public static int getNewId() {
        return newId;
    }

    public int getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public DateTime getDue() {
        return due;
    }

    public DateTime getStart() {
        return start;
    }

    public DateTime getEnd() {
        return end;
    }

    public List<String> getTags() {
        return tags;
    }

    public boolean isDeleted() {
        return this.deleted;
    }

    public boolean isDone() {
        return this.done;
    }

    /*** MUTATORS ***/

    public static void setNewId(int newId) {
        TaskStub.newId = newId;
    }

    public void setId(int ID) {
        this.ID = ID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDue(DateTime due) {
        this.due = due;
    }

    public void setStart(DateTime start) {
        this.start = start;
    }

    public void setEnd(DateTime end) {
        this.end = end;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}
