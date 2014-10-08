package Parser;

import Logic.CommandType;

public class CommandHelp extends Command {
    
    // help fields: "all" or "<command>" or "invalid"
    private String field;

    public CommandHelp(String content) {
        this.type = CommandType.HELP;
        this.field = content;
    }
    
    public String get(){
        return this.field;
    }
    
    public String toString() {
        String result = "\n[[ CMD-HELP: ]]";
        result = result.concat("\nfield: " + field);
        
        return result;
    }

}
