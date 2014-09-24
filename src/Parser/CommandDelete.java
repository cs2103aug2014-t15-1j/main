package Parser;

import java.util.ArrayList;

import Logic.CommandType;

public class CommandDelete extends Command {
    
    // Delete types [all, search, id, dates, done]
    protected String rangeType;
    
    // Delete type data
    protected String id;
    protected String start;
    protected String end;

    public CommandDelete(ArrayList<TaskParam> content) {
        this.type = CommandType.DELETE;
        
        for (TaskParam param : content) {
            switch (param.getName()){
                case "rangeType":
                    this.rangeType = param.getField();
                    break;
                    
                case "id":
                    this.id = param.getField();
                    break;
                    
                case "start":
                    this.start = param.getField();
                    break;
                    
                case "end":
                    this.end = param.getField();
                    break;
                    
                default:
                    this.type = CommandType.ERROR;
                    this.error = "Deleter, we have a problem.";    
            }
        }
    }
    
    public String get(String field){
        switch (field) {
            case "rangeType":
                return this.rangeType;
            
            case "id":
                return this.id;
                
            case "start":
                return this.start;
            
            case "end":
                return this.end;
                
            default:
                return null;
        }
    }
    
    public String toString() {
        String result = "[[ CMD-Delete: ]]\n";
        result = result.concat("rangeType: " + rangeType + "\n");
        result = result.concat("id: " + id + "\n");
        result = result.concat("start: " + start + "\n");
        result = result.concat("end: " + end + "\n");
        
        return result;
    }

}
