package Parser;

import java.util.List;

import Logic.CommandType;

public class CommandTodo extends Command {

    // Todo types [get("rangeType"); returns "last" | "id"]
    private String rangeType;

    // "id" data [get("id"); returns string]
    private String id;

    public CommandTodo(List<TaskParam> content) {
        if (content.isEmpty()) {
            this.type = CommandType.ERROR;
            this.error = "No arguments for todo";
        } else {
            this.type = CommandType.TODO;

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
        String result = "\n[[ CMD-TODO: ]]";
        result = result.concat("\nrangeType: " + rangeType);
        result = result.concat("\nid: " + id);

        return result;
    }

}
