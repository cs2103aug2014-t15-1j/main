package Logic;

import java.util.List;
import java.util.ArrayList;

import Parser.TaskParam;
import Storage.Task;

public class CommandEdit extends Command {
    
    private String id;
    private String name;
    private String due;
    private String start;
    private String end;
    private String delete;
    
    private List<String> tags = new ArrayList<String>();
    
    private static final String PARAM_ID = "id";
    private static final String PARAM_NAME = "name";
    private static final String PARAM_DUE = "due";
    private static final String PARAM_START = "start";
    private static final String PARAM_END = "end";
    private static final String PARAM_DEL = "delete";

    public CommandEdit(List<TaskParam> content) {
        this.type = CommandType.EDIT;
        
        for (TaskParam param : content) {
            switch (param.getName()){
                case "id":
                    this.id = param.getField();
                    break;
                    
                case "name":
                case "n":
                    this.name = param.getField();
                    break;
                    
                case "due":
                case "d":
                    this.due = param.getField();
                    break;
                    
                case "start":
                case "s":
                    this.start = param.getField();
                    break;
                    
                case "end":
                case "e":
                    this.end = param.getField();
                    break;
                    
                case "tag":
                    // NOTE: possible change to string (tags = tags.concat())
                    this.tags.add(param.getField());
                    break;
                    
                case "delete":
                    this.delete = param.getField();
                    break;
                    
                default:
                    System.out.println("EDITor, we have a problem.");    
            }
        }
    }
    
    public String get(String paramName){
        switch(paramName){
            case PARAM_ID:
                return this.id;
                
            case PARAM_NAME:
                return this.name;
            
            case PARAM_DUE:
                return this.due;
            
            case PARAM_START:
                return this.start;
            
            case PARAM_END:
                return this.end;
                
            case PARAM_DEL:
                return this.delete;
                
            default:
                System.out.println("Edit get's got a problem!");
                return null;
        }
    }
    
    public List<String> getTags(){
        return this.tags;
    }
    
    public String toString() {
        
        String result = "\n[[ CMD-EDIT: ]]";
        result = result.concat("\n" + "id: " + id);
        result = result.concat("\n" + "name: " + name);
        result = result.concat("\n" + "due: " + due);
        result = result.concat("\n" + "start: " + start);
        result = result.concat("\n" + "end: " + end);
        result = result.concat("\n" + "tags: " + tags);
        result = result.concat("\n" + "delete: " + delete);
        
        return result;
    }
    
    /**
     * Executes "edit" operation
     * Allow edit/deletion of parameters of a Task
     * @return Result
     */
    protected Result execute(boolean userInput) {
        Processor processor = Processor.getInstance();
        Processor.getLogger().info("Executing 'Edit' Command...");
        List<Task> list = new ArrayList<Task>();
        boolean success = false;
        
        int taskId = 0;
        try {
            taskId = Integer.parseInt(id);
        } catch (Exception e) {
            Processor.getLogger().warning("Invalid Task Id!");
        }
        
        if (taskId > 0) {
            Task existingTask = processor.getFile().getTask(taskId);
            Task oldTask = new Task(existingTask);
            success = processor.getFile().updateTaskInfo(existingTask, name, due, start, end, tags);
            if (success) {
                processor.getEditedTaskHistory().push(oldTask);
                list.add(existingTask);
            }
        }
        return new Result(list, success, getType());
    }

}
