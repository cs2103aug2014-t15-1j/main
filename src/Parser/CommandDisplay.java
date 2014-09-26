package Parser;

import java.util.ArrayList;

import Logic.CommandType;

// TODO: MERGE with delete? VERY similar.

public class CommandDisplay extends Command {

    // Restore types [get("rangeType"); returns "all" | "id" | "done"]
    protected String rangeType;

    // Restore type data [get("id"); returns string]
    protected String id;

    public CommandDisplay(ArrayList<TaskParam> content) {
        if (content.isEmpty()) {
            this.type = CommandType.ERROR;
            this.error = "No arguments for display";
        } else {
            this.type = CommandType.DISPLAY;

            for (TaskParam param : content) {
                switch (param.getName()) {
                    case "rangeType":
                        this.rangeType = param.getField();
                        break;

                    case "id":
                        this.id = param.getField();
                        break;

                    default:
                        this.type = CommandType.ERROR;
                        this.error = "Restore constructor parameter error";
                }
            }
        }
    }

    public String get(String field) {
        switch (field) {
            case "rangeType":
                return this.rangeType;

            case "id":
                return this.id;

            default:
                return null;
        }
    }

    public String toString() {
        String result = "\n[[ CMD-DISPLAY: ]]";
        result = result.concat("\nrangeType: " + rangeType);
        result = result.concat("\nid: " + id);

        return result;
    }

}
