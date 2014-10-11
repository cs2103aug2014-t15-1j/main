package Parser;

import Logic.CommandType;

/**
 * This Command object encompasses the commands that need no other parameter
 * inputs.
 * 
 * The follow commands are included: "undo", "redo", "exit" and
 * "error".
 * 
 * The "error" Command can be constructed in two ways: an incorrect input, or
 * intentionally with an error message [CommandOthers("error", msg)]. This Command
 * cannot be created by a user, and works more like a thrown Exception.
 * 
 * @author Nitsuj Portable
 *
 */

public class CommandOthers extends Command {
    public CommandOthers(String type) {
        switch (type.toLowerCase()) {
            case "exit":
                this.type = CommandType.EXIT;
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
        assert(type.equals("error"));
        this.type = CommandType.ERROR;
        this.error = msg;
    }

    public String toString() {
        String result = "\n[[ CMD-OTHERS ]]";
        result = result.concat("\ncmd-type: " + type);
        result = result.concat("\ncmd-info: " + error);

        return result;
    }

}
