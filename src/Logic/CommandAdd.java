package Logic;

import java.util.List;
import java.util.ArrayList;

import Parser.DateParser;
import Parser.TaskParam;
import Storage.DateTime;
import Storage.Task;

public class CommandAdd extends Command {
    
    private String name;
    private DateTime due;
    private DateTime start;
    private DateTime end;
    
    private List<String> tags = new ArrayList<String>();
    
    private static final String PARAM_NAME = "name";
    private static final String PARAM_DUE = "due";
    private static final String PARAM_START = "start";
    private static final String PARAM_END = "end";

    public CommandAdd(List<TaskParam> content) {
        this.type = CommandType.ADD;
        
        for (TaskParam param : content) {
            switch (param.getName()){
                case "name":
                case "n":
                    this.name = param.getField();
                    break;
                    
                case "due":
                case "d":
                    this.due = DateParser.parseToDateTime(param.getField());
                    break;
                    
                case "start":
                case "s":
                    this.start = DateParser.parseToDateTime(param.getField());
                    break;
                    
                case "end":
                case "e":
                    this.end = DateParser.parseToDateTime(param.getField());
                    break;
                    
                case "tag":
                    // NOTE: possible change to string (tags = tags.concat())
                    this.tags.add(param.getField());
                    break;
                    
                default:
                    System.out.println("Error in adding Add parameters");    
            }
        }
    }
    
    public String get(String paramName){
        switch(paramName){
            case PARAM_NAME:
                return this.name;
            
            case PARAM_DUE:
                return this.due.toString();
            
            case PARAM_START:
                return this.start.toString();
            
            case PARAM_END:
                return this.end.toString();
                
            default:
                System.out.println("Add: Get's got a problem!");
                return null;
        }
    }
    
    public List<String> getTags(){
        return this.tags;
    }
    
    public String toString() {
        
        String result = "\n[[ CMD-ADD: ]]";
        result = result.concat("\n" + "name: " + name);
        result = result.concat("\n" + "due: " + due);
        result = result.concat("\n" + "start: " + start);
        result = result.concat("\n" + "end: " + end);
        result = result.concat("\n" + "tags: " + tags);
        
        return result;
    }


    
    /** 
     * Executes 'add' operation of a task 
     * Add a Task to 'todo' List
     * @return Result
     */
    protected Result execute(boolean userInput) {
        boolean success = false;
        List<Task> list = new ArrayList<Task>();
        Processor.getLogger().info("Executing 'Add' Command...");
        if (!isBlocked()) {
            Task newTask = new Task(name, due, start, end, tags);
            success = Processor.getInstance().getFile().addNewTask(newTask);
            list.add(newTask);
        }
        return new Result(list, success, getType());
    }
    
    /**
     * Check if the date is blocked
     * @param cmd
     * @return false/true depending on the validity of blocked dates
     */
    private boolean isBlocked() {
        return false;
    }
    
    /** Undo the 'Add' Command */
    protected Result executeComplement() {
        Processor processor = Processor.getInstance();
        List<Task> tasks = new ArrayList<Task>();
        boolean success = false;
        
        int taskId = processor.getFile().getToDoTasks().size() - 1;
        Task toDelete = processor.getFile().getToDoTasks().get(taskId);
        
        success = processor.getFile().wipeTask(toDelete);
        
        if (success) {
            tasks.add(toDelete);
        }
        
        return new Result(tasks, success, getType());
    }
    
}
