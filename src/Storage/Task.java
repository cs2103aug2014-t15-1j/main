package Storage;

import java.util.List;
import java.util.ArrayList;

import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;

public class Task {
    private static int newId = 1;   // ID for each new task object
	
	// Task Object Attributes
    private final int ID; // ID only changes once, at each object construction
    private String name = "";
    private String due = "";   // TODO Change to Date type
    private String start = ""; // TODO Change to Date type
    private String end = "";   // TODO Change to Date type
    private List<String> tags = new ArrayList<String>();
    private boolean deleted = false;
    private boolean done = false;
    
    // Constructor
    public Task(String name, String due, String start, 
            String end, List<String> tags) {
        this.ID = newId;
        newId++;
        this.name = name;
        this.due = due;
        this.start = start;
        this.end = end;
        this.tags = tags;
    }
    
    // Constructor that clones a task object
    public Task(Task task) {
        this.ID = task.getId();
        this.name = task.getName();
        this.due = task.getDue();
        this.start = task.getStart();
        this.end = task.getEnd();
        this.tags.addAll(task.getTags());
        this.deleted = task.isDeleted();
        this.done = task.isDone();
    }
    
    // Stores all attributes in a single String
    // Meant to be written to system file
    public String getFullInfo() {
        String fullInfo = "Name: " + name + " ";
        fullInfo += "Due: " + due + " ";
        fullInfo += "Start: " + start + " ";
        fullInfo += "End: " + end + " ";
        fullInfo += concatanateTags();
        fullInfo += done ? "#done" : "#todo";
        
        return fullInfo;
    }
    
    // Stores attributes to be searched in a single String
    public String getSummary() {
        String summary = name + " ";
        summary += due + " ";
        summary += start + " ";
        summary += end + " ";
        summary += concatanateTags(); // TODO remove #'s?
        return summary;
    }
    
    // Converts list of tags into a single String
    private String concatanateTags() {
        String concatanatedTags = "";
        if (!tags.isEmpty()) { // TODO Necessary?
            for (String tempTag : tags) {
                concatanatedTags += tempTag + " ";
            }
        }
        return concatanatedTags;
    }
    
    public boolean wipeTask() {
        newId--;
        return true;
    }
    
    //===== Getters, setters, and resetters =====//
    
    // ID attribute functions
    @Getter
    public int getId() {
        return ID;
    }
    
    // Name attribute functions
    @Getter
    public String getName() {
        return name;
    }
    
    @Setter
    public void setName(String name) {
        this.name = name;
    }
    
    public void resetName() {
        name = "";
    }
    
    // Due attribute functions
    @Getter
    public String getDue() {
        return due;
    }
    
    @Setter
    public void setDue(String due) {
        this.due = due;
    }
    
    public void resetDue() {
        due = "";
    }
    
    // Start attribute functions
    @Getter
    public String getStart() {
        return start;
    }
    
    @Setter
    public void setStart(String start) {
        this.start = start;
    }
    
    public void resetStart() {
        start = "";
    }
    
    // End attribute functions
    @Getter
    public String getEnd() {
        return end;
    }
    
    @Setter
    public void setEnd(String end) {
        this.end = end;
    }
    
    public void resetEnd() {
        end = "";
    }
    
    // Tags attribute functions
    @Getter
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
        tags.clear();
    }
    
    // Deleted attribute functions
    @Getter
    public boolean isDeleted() {
        return deleted;
    }
    
    @Setter
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
    
    // Done attribute functions
    @Getter
    public boolean isDone() {
        return done;
    }
    
    @Setter
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
