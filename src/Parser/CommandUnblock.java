package Parser;

import java.util.List;

import Logic.CommandType;

public class CommandUnblock extends Command {

    // Only one type of unblock search: "id"
    // Use get("id"); returns string
    protected String id;

    public CommandUnblock(List<TaskParam> content) {
        if (content.isEmpty()) {
            this.type = CommandType.ERROR;
            this.error = "No arguments for unblock";
        } else {
            this.type = CommandType.UNBLOCK;

            for (TaskParam param : content) {
                switch (param.getName()) {
                    case "id":
                        this.id = param.getField();
                        break;

                    default:
                        this.type = CommandType.ERROR;
                        this.error = "Unblock constructor parameter error";
                }
            }
        }
    }

    // TODO: Change to empty get()?
    public String get(String field) {
        switch (field) {
            case "id":
                return this.id;

            default:
                return null;
        }
    }

    public String toString() {
        String result = "\n[[ CMD-Unblock: ]]";
        result = result.concat("\nid: " + id);

        return result;
    }

}
