package commandLogic;

import java.util.List;
import java.util.ArrayList;

import commandLogic.Result.ResultType;
import database.Task;
import textParser.TaskParam;

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

    @Override
    public String get(String field) {
        switch (field) {
            case "date":
                return date;

            default:
                return null;
        }
    }

    @Override
    public List<String> getTags() {
        return this.tags;
    }

    // TODO: Add to Command
    @Override
    public List<String> getKeywords() {
        return this.keywords;
    }

    @Override
    public String toString() {
        String result = "\n[[ CMD-SEARCH: ]]";
        result = result.concat("\nCmdType: " + this.type);
        result = result.concat("\ndate: " + this.date);
        result = result.concat("\ntags: " + this.tags);
        result = result.concat("\nkeywords: " + this.keywords);

        return result;
    }

    /**
     * Executes "search" operation
     * Allows search <date>, search <key1> <key2> #tag1 #tag2
     * @return Result
     */
    @Override
    protected Result execute(boolean userInput) {
        Processor.getLogger().info("Executing 'Search' Command...");
        Processor.getInstance().initialiseNewSearchList();
        if (date != null) {
            searchUsingDate(date);
        } else if (keywords != null || tags != null) {
            searchUsingKeyOrTags(keywords, tags);
        }
        return new Result(Processor.getInstance().getLastSearch(), true, getType(), ResultType.TASK);
    }
    
    /* Performs search using date */
    private void searchUsingDate(String date) {
        List<Task> toDoTasks = Processor.getInstance().getFile().getToDoTasks();
        for (Task t: toDoTasks) {
            if (t.getDue().equals(date)) {
                Processor.getInstance().getLastSearch().add(t);
            }
        }
    }
    
    /**
     * Performs search using Keywords or Tags 
     * Tries to find if tags is present first before searching for keywords
     * 
     */
    private void searchUsingKeyOrTags(List<String> keywords, List<String> tags) {
        List<Task> toDoTasks = Processor.getInstance().getFile().getToDoTasks();
        for (Task task: toDoTasks) {       
            boolean found = isTagged(task, tags);
            if (!found) {
                found = containsKeyword(task, keywords);
            }
        }
    }
    
    /** Checks if a Task is tagged under a tag in a List of tags*/
    private boolean isTagged(Task task, List<String> tags) {
        for (String tag: tags) {
            if (task.getTags().contains(tag)) {
                Processor.getInstance().getLastSearch().add(task);
                return true;
            }
        }
        return false;
    }
    
    /** Checks if a Task contains a certain keyword in the List of keywords */
    private boolean containsKeyword(Task task, List<String> keywords) {
        for (String key: keywords) {
            if (task.getName().toLowerCase().contains(key.toLowerCase())) {
                Processor.getInstance().getLastSearch().add(task);
                return true;
            }
        }
        return false;
    }
}
