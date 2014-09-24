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
    
    public String toString() {
        return "command:" + type;
    }

}
