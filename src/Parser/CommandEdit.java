package Parser;

import java.util.ArrayList;

import Logic.CommandType;

public class CommandEdit extends Command {
    
    private String id;
    private String name;
    private String more;
    private String due;
    private String start;
    private String end;
    private String priority;
    private String delete;
    
    private static final String PARAM_ID = "id";
    private static final String PARAM_NAME = "name";
    private static final String PARAM_MORE = "more";
    private static final String PARAM_DUE = "due";
    private static final String PARAM_START = "start";
    private static final String PARAM_END = "end";
    private static final String PARAM_PRIO = "priority";
    private static final String PARAM_TAGS = "tags";
    private static final String PARAM_DEL = "delete";

    public CommandEdit(ArrayList<TaskParam> content) {
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
                    
                case "more":
                case "m":
                    this.more = param.getField();
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
                    
                case "priority":
                case "p":
                    this.priority = param.getField();
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
            
            case PARAM_MORE:
                return this.more;
            
            case PARAM_DUE:
                return this.due;
            
            case PARAM_START:
                return this.start;
            
            case PARAM_END:
                return this.end;
                
            case PARAM_PRIO:
                return this.priority;
                
            case PARAM_DEL:
                return this.delete;
                
            default:
                System.out.println("Edit get's got a problem!");
                return null;
        }
    }
    
    public ArrayList<String> getTags(){
        if (tags.equals(PARAM_TAGS)) {
            return this.tags;
        } else {
            return null;
        }
    }
    
    public String toString() {
        
        String result = "\n[[ CMD-EDIT: ]]";
        result = result.concat("\n" + "id: " + id);
        result = result.concat("\n" + "name: " + name);
        result = result.concat("\n" + "more: " + more);
        result = result.concat("\n" + "due: " + due);
        result = result.concat("\n" + "start: " + start);
        result = result.concat("\n" + "end: " + end);
        result = result.concat("\n" + "priority: " + priority);
        result = result.concat("\n" + "tags: " + tags);
        result = result.concat("\n" + "delete: " + delete);
        
        return result;
    }

}
