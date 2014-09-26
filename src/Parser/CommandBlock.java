package Parser;

import java.util.ArrayList;

import Logic.CommandType;

public class CommandBlock extends Command {

    // Block dates range [get("start"), get("end"); returns date]
    // If it's only 1 day, start = end
    protected String start;
    protected String end;

    public CommandBlock(ArrayList<TaskParam> content) {
        if (content.isEmpty()) {
            this.type = CommandType.ERROR;
            this.error = "No arguments for block";
        } else {
            this.type = CommandType.BLOCK;

            for (TaskParam param : content) {
                switch (param.getName()) {
                    case "start":
                        this.start = param.getField();
                        break;
                        
                    case "end":
                        this.end = param.getField();
                        break;

                    default:
                        this.type = CommandType.ERROR;
                        this.error = "Block constructor parameter error";
                }
            }
        }
    }

    public String get(String field) {
        switch (field) {
            case "start":
                return this.start;

            case "end":
                return this.end;

            default:
                return null;
        }
    }

    public String toString() {
        String result = "\n[[ CMD-BLOCK: ]]";
        result = result.concat("\nstart: " + start);
        result = result.concat("\nend: " + end);

        return result;
    }

}
