package Storage;

import java.util.List;
import java.util.ArrayList;

public class Task {
    private static int newId = 1;   // ID for each new task object
	
	// Task Object Attributes
    private final int ID; // ID only changes once, at each object construction
    private String name = "";
    private String more = "";
    private String due = "";   // TODO Change to Date type
    private String start = ""; // TODO Change to Date type
    private String end = "";   // TODO Change to Date type
    private String priority = "";
    private List<String> tags = new ArrayList<String>();
    private boolean deleted = false;
    private boolean done = false;
    
    // Constructor
    public Task(String name, String more, String due, String start, 
            String end, String priority, List<String> tags) {
        this.ID = newId;
        newId++;
        this.name = name;
        this.more = more;
        this.due = due;
        this.start = start;
        this.end = end;
        this.priority = priority;
        this.tags = tags;
    }
    
    // Constructor that clones a task object
    public Task(Task task) {
        this.ID = task.getId();
        this.name = task.getName();
        this.more = task.getMore();
        this.due = task.getDue();
        this.start = task.getStart();
        this.end = task.getEnd();
        this.priority = task.getPriority();
        this.tags.addAll(task.getTags());
        this.deleted = task.isDeleted();
        this.done = task.isDone();
    }
    
    // Converts task into a single String
    // Meant to be written to system file
    public String stringify() {
        String stringifiedTask = "Name: " + name + " ";
        stringifiedTask += "More: " + more + " ";
        stringifiedTask += "Due: " + due + " ";
        stringifiedTask += "Start: " + start + " ";
        stringifiedTask += "End: " + end + " ";
        stringifiedTask += "Priority: " + priority + " ";
        stringifiedTask += stringifyTags();
        stringifiedTask += done ? "#done" : "#todo";
        
        return stringifiedTask;
    }
    
    // Converts list of tags into a single String
    private String stringifyTags() {
        String stringifiedTags = "";
        if (!this.tags.isEmpty()) {
            for (String tempTag : this.tags) {
                stringifiedTags += tempTag + " ";
            }
        }
        return stringifiedTags;
    }
    
    //===== Getters, setters, and resetters =====//
    
    // ID attribute functions
    public int getId() {
        return ID;
    }
    
    // Name attribute functions
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void resetName() {
        this.name = "";
    }
    
    // More attribute functions
    public String getMore() {
        return more;
    }
    
    public void setMore(String more) {
        this.more = more;
    }
    
    public void resetMore() {
        this.more = "";
    }
    
    // Due attribute functions
    public String getDue() {
        return due;
    }
    
    public void setDue(String due) {
        this.due = due;
    }
    
    public void resetDue() {
        this.more = "";
    }
    
    // Start attribute functions
    public String getStart() {
        return start;
    }
    
    public void setStart(String start) {
        this.start = start;
    }
    
    public void resetStart() {
        this.start = "";
    }
    
    // End attribute functions
    public String getEnd() {
        return end;
    }
    
    public void setEnd(String end) {
        this.end = end;
    }
    
    public void resetEnd() {
        this.end = "";
    }
    
    // Priority attribute functions
    public String getPriority() {
        return priority;
    }
    
    public void setPriority(String priority) {
        this.priority = priority;
    }
    
    public void resetPriority() {
        this.priority = "";
    }
    
    // Tags attribute functions
    public List<String> getTags() {
        return tags;
    }
    
    public void addTags(List<String> tags) {
        this.tags.addAll(tags);
    }
    
    public void removeTags(List<String> tags) {
        this.tags.removeAll(tags);
    }
    
    public void resetTags() {
        this.tags.clear();
    }
    
    // Deleted attribute functions
    public boolean isDeleted() {
        return deleted;
    }
    
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
    
    // Done attribute functions
    public boolean isDone() {
        return done;
    }
    
    public void setDone(boolean done) {
        this.done = done;
    }
    
    
    /* Legacy code from YX
	public Task(Command cmd) {
		 this.cmdType = cmd.getType();
		 this.details = ;
		 this.numTasks++;
	}
	
	public Command getCommand() {
		return this.cmd;
	}
	
	public boolean delete() {
		numTasks--;
		//Do some deleting
		return true;
	}
	
	public Map<String, String> getDetails() {
		return this.details;
	}
	
	@Override
	public String toString() {
		String output = "";
		for (Map.Entry<String, String> entry : details.entrySet()) {
		    output += entry.getKey() + ": " + entry.getValue() + "\n";
		}
		
		if (output.length() > 1) {
			output = output.substring(0, output.length() - 1);
		}
		
		return output;
	}*/
}
