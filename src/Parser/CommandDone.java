package Parser;

import java.util.List;

import Logic.CommandType;

//TODO: ADD DATES

public class CommandDone extends Command {

    // Done types [get("rangeType"); returns "all" | "id" | "dates"]
    private String rangeType;

    // Done data [get("id"), get("start"), get("end"); returns string]
    // If only 1 date, start = end;
    private String id;
    private String start;
    private String end;

    public CommandDone(List<TaskParam> content) {
        if (content.isEmpty()) {
            this.type = CommandType.ERROR;
            this.error = "No arguments for done";
        } else {
            this.type = CommandType.DONE;

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
                        this.error = "Todo constructor parameter error";
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
        String result = "\n[[ CMD-DONE: ]]";
        result = result.concat("\nrangeType: " + rangeType);
        result = result.concat("\nid: " + id);
        result = result.concat("\nstart: " + start);
        result = result.concat("\nend: " + end);

        return result;
    }

}
