package Parser;

import java.util.ArrayList;

import Logic.CommandType;

public class CommandAdd extends Command {
    
    private String name;
    private String more;
    private String due;
    private String start;
    private String end;
    private String priority;
    private ArrayList<String> tags = new ArrayList<String>();
    
    private static final String PARAM_NAME = "name";
    private static final String PARAM_MORE = "more";
    private static final String PARAM_DUE = "due";
    private static final String PARAM_START = "start";
    private static final String PARAM_END = "end";
    private static final String PARAM_PRIO = "priority";
    private static final String PARAM_TAGS = "tags";

    public CommandAdd(ArrayList<TaskParam> content) {
        this.type = CommandType.ADD;
        
        for (TaskParam param : content) {
            switch (param.getName()){
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
                    
                default:
                    System.out.println("Houston, we have a problem.");    
            }
        }
    }
    
    public String get(String paramName){
        switch(paramName){
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
                
            default:
                System.out.println("Get's got a problem!");
                return null;
        }
    }
    
    public ArrayList<String> getTags(String tags){
        if (tags.equals(PARAM_TAGS)) {
            return this.tags;
        } else {
            return null;
        }
    }
    
    public String toString() {
        
        String result = "name: " + name;
        result = result.concat("\n" + "more: " + more);
        result = result.concat("\n" + "due: " + due);
        result = result.concat("\n" + "start: " + start);
        result = result.concat("\n" + "end: " + end);
        result = result.concat("\n" + "priority: " + priority);
        result = result.concat("\n" + "tags: " + tags);
        
        return result;
    }

}
