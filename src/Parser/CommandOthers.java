package Parser;

import Logic.CommandType;

public class CommandOthers extends Command {
    public CommandOthers(String type) {
        switch(type.toLowerCase()) {
            case "clear":
                this.type = CommandType.CLEAR;
                break;
            
            case "exit":
                this.type = CommandType.EXIT;
                break;
            
            case "joke":
                this.type = CommandType.JOKE;
                break;
            
            case "redo":
                this.type = CommandType.REDO;
                break;
                
            case "undo":
                this.type = CommandType.UNDO;
                break;
            
            default:
                this.type = CommandType.ERROR;
                this.error = "Error in Command constructor (command type)";
        }
    }
    
    public CommandOthers(String type, String msg) {
        this(type);
        if(type.equals("error")) {
            this.error = msg;
        }
    }
    
    public String toString() {
        String result = "\n[[ CMD-OTHERS ]]";
        result = result.concat("\ncmd-type: " + type);
        result = result.concat("\ncmd-info: " + error);
        
        return result;
    }

}
