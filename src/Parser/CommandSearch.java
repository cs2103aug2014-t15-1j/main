package Parser;

import java.util.ArrayList;

import Logic.CommandType;

public class CommandSearch extends Command {
    
    // Tags like #done, #deleted, #todo are stored in tags
    // But the parser ensures only 1 of the above is possible
    // TODO: ASSUME PARSER IS AN IDIOT
    private ArrayList<String> tags = new ArrayList<String>();

    private ArrayList<String> keywords = new ArrayList<String>();
    
    // TODO: Assign date stuff
    private String start;
    private String end;

    public CommandSearch(ArrayList<TaskParam> content) {
        assert(!content.isEmpty());
        this.type = CommandType.SEARCH;

            for (TaskParam param : content) {
                switch (param.getName()) {
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

            default:
                return null;
        }
    }

    public ArrayList<String> getTags() {
        return this.tags;
    }
    
    public String toString() {
        String result = "\n[[ CMD-SEARCH: ]]";
        result = result.concat("\nCmdType: " + this.type);
        result = result.concat("\ntags: " + this.tags);
        result = result.concat("\nkeywords: " + this.keywords);
        result = result.concat("\nstart: " + this.start);
        result = result.concat("\nend: " + this.end);

        return result;
    }

}
