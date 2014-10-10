package Parser;

import java.util.List;

import Logic.CommandType;

/**
 * This Command stores the delete type and if applicable, an id value.
 * 
 * Delete type: get("rangeType") [Values: "all", "search", "done", "id" ].
 * 
 * Delete id: get("id") [Value: string that can be parsed as int].
 * 
 * @author Nitsuj Portable
 *
 */
public class CommandDelete extends Command {

    // Delete types
    private String rangeType;

    // Delete type data [get("id")]
    private String id;

    public CommandDelete(List<TaskParam> content) {
        if (content.isEmpty()) {
            this.type = CommandType.ERROR;
            this.error = "No arguments for delete";
        } else {
            this.type = CommandType.DELETE;

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
                        this.error = "Delete constructor parameter error";
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
        String result = "\n[[ CMD-Delete: ]]";
        result = result.concat("\nrangeType: " + rangeType);
        result = result.concat("\nid: " + id);

        return result;
    }

}
