package logic;

import gui.TaskStub;

import java.util.List;
import java.util.ArrayList;

import logic.Result.ResultType;
import parser.DateParser;
import parser.TaskParam;
import database.DateTime;
import database.Task;

public class CommandEdit extends Command {
    
    private String id = "";
    private String name = "";
    private DateTime due = new DateTime();
    private DateTime start = new DateTime();
    private DateTime end = new DateTime();
    private String delete = "";
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
            constructUsingParam(param);
        }
    }

    private void constructUsingParam(TaskParam param) {
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
                
            case "delete":
                this.delete = param.getField();
                break;
                
            default:
                System.out.println("EDITor, we have a problem.");    
        }
    }
    
    @Override
    public String get(String paramName){
        switch(paramName){
            case PARAM_ID:
                return this.id;
                
            case PARAM_NAME:
                return this.name;
            
            case PARAM_DUE:
                return this.due.toString();
            
            case PARAM_START:
                return this.start.toString();
            
            case PARAM_END:
                return this.end.toString();
                
            case PARAM_DEL:
                return this.delete;
                
            default:
                System.out.println("Edit get's got a problem!");
                return null;
        }
    }
    
    @Override
    public List<String> getTags(){
        return this.tags;
    }
    
    @Override
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
    @Override
    protected Result execute(boolean userInput) {
        if (Processor.ENABLE_LOGGING) {
            Processor.getLogger().info("Executing 'Edit' Command...");
        }
        Processor processor = Processor.getInstance();
        List<Task> list = new ArrayList<Task>();
        boolean success = false;
        
        int taskId = getTaskId();
        
        if (taskId > 0) {
            Task existingTask = processor.getFile().getTask(taskId);
            Task oldTask = new Task(existingTask);
            if (delete != null) {
                switch (delete) {
                    case "name":
                        name = null;
                        break;
                    case "due":
                        due = null;
                        break;
                    case "start":
                        start = null;
                        break;
                    case "end":
                        end = null;
                        break;
                    case "tags":
                        tags = null;
                        break;
                }
            }
            success = processor.getFile().updateTaskInfo(existingTask, name, due, start, end, tags);
            if (success) {
                performUpdate(list, oldTask, existingTask);
            }
        }
        return new Result(list, success, getType(), ResultType.TASK);
    }
    
    private int getTaskId() {
        int taskId = 0;
        try {
            taskId = Integer.parseInt(id);
        } catch (Exception e) {
            if (Processor.ENABLE_LOGGING) {
                Processor.getLogger().warning("Invalid Task Id!");
            }
        }
        return taskId;
    }
    
    private void performUpdate(List<Task> list, Task oldTask, Task existingTask) {
        Processor processor = Processor.getInstance();
        processor.getEditedTaskHistory().push(oldTask);
        list.add(existingTask);
    }
    
    /** Undo the 'Edit' Command */
    @Override
    protected Result executeComplement() {
        Processor processor = Processor.getInstance();
        boolean success = false;
        List<Task> tasks = new ArrayList<Task>();
        
        Task prevTask = processor.getEditedTaskHistory().pop();
        
        String taskName = prevTask.getName();
        DateTime taskDue = prevTask.getDue();
        DateTime taskStart = prevTask.getStart();
        DateTime taskEnd = prevTask.getEnd();
        List<String> taskTags = prevTask.getTags();
        
        tasks.add(prevTask);
        success = processor.getFile().updateTaskInfo(prevTask.getId(), taskName, taskDue, taskStart, taskEnd, taskTags);
        return new Result(tasks, success, getType(), ResultType.TASK);
    }
}
