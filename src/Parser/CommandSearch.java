package Parser;

import java.util.List;
import java.util.ArrayList;

import Logic.CommandType;

public class CommandSearch extends Command {

    // Tags like #done, #deleted, #todo are stored in tags
    // But the parser ensures only 1 of the above is possible
    // TODO: ASSUME PARSER IS AN IDIOT
    private List<String> tags = new ArrayList<String>();
    private List<String> keywords = new ArrayList<String>();
    private String date;

    public CommandSearch(List<TaskParam> content) {
        assert (!content.isEmpty());
        this.type = CommandType.SEARCH;

        for (TaskParam param : content) {
            switch (param.getName()) {
                case "date":
                    this.date = param.getField();
                    break;
                    
                case "tag":
                    this.tags.add(param.getField());
                    break;

                case "word":
                    this.keywords.add(param.getField());
                    break;

                default:
                    this.type = CommandType.ERROR;
                    this.error = "Search constructor parameter error";
            }
        }
    }

    public String get(String field) {
        switch (field) {
            case "date":
                return date;

            default:
                return null;
        }
    }

    public List<String> getTags() {
        return this.tags;
    }

    // TODO: Add to Command
    public List<String> getKeywords() {
        return this.keywords;
    }

    public String toString() {
        String result = "\n[[ CMD-SEARCH: ]]";
        result = result.concat("\nCmdType: " + this.type);
        result = result.concat("\ndate: " + this.date);
        result = result.concat("\ntags: " + this.tags);
        result = result.concat("\nkeywords: " + this.keywords);

        return result;
    }

}
