package Parser;

import java.util.List;
import java.util.ArrayList;

import Logic.CommandType;

public class CommandAdd extends Command {
    
    private String name;
    private String due;
    private String start;
    private String end;
    
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
                return this.due;
            
            case PARAM_START:
                return this.start;
            
            case PARAM_END:
                return this.end;
                
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

}
