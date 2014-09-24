package Parser;

import java.util.ArrayList;

import Logic.CommandType;

public class CommandDelete extends Command {
    
    // Delete fields [all, search, id, date/date_range, done]
    protected String delete_type;
    protected String delete_field;

    public CommandDelete(ArrayList<TaskParam> content) {
        this.type = CommandType.DELETE;
    }
    
    public String get(String paramName){
        return "";
    }
    /*
    public ArrayList<String> getTags(String tags){
        if (tags.equals(PARAM_TAGS)) {
            return this.tags;
        } else {
            return null;
        }
    }
    
    public String toString() {
    }*/

}
