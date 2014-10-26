package logic;

import java.util.List;
import java.util.ArrayList;

import logic.Result.ResultType;
import parser.TaskParam;
import database.Task;

public class CommandSearch extends Command {

    // Tags like #done, #deleted, #todo are stored in tags
    // But the parser ensures only 1 of the above is possible
    // TODO: ASSUME PARSER IS AN IDIOT
    private List<String> tags = new ArrayList<String>();
    private List<String> keywords = new ArrayList<String>();
    private String date = "";

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
     * Allows search <date> <key1> <key2> #tag1 #tag2
     * @return Result
     */
    @Override
    protected Result execute(boolean userInput) {
        Processor.getLogger().info("Executing 'Search' Command...");
        Processor.getInstance().initialiseNewSearchList();
        searchUsingDateAndKeyAndTags(date, keywords, tags);
        return new Result(Processor.getInstance().getLastSearch(), true, getType(), ResultType.TASK);
    }
    
    private void searchUsingDateAndKeyAndTags(String date, List<String> keywords, List<String> tags) {
        int criteriaCount = getCriteriaCount(date, keywords, tags);
        if (criteriaCount > 0) {
            List<Task> allTasks = Processor.getInstance().getFile().getToDoTasks();
            for (Task task : allTasks) {
                int found = criteriaCount;
                if (date != "") {
                    if (task.getSummary().contains(date)) {
                        found--;
                    }
                }
                if (!keywords.isEmpty()) {
                    for (String keyword : keywords) {
                        if (task.getSummary().toLowerCase().contains(keyword)) {
                            found--;
                            break;
                        }
                    }
                }
                if (!tags.isEmpty()) {
                    for (String tag : tags) {
                        if (task.getSummary().toLowerCase().contains(tag)) {
                            found--;
                            break;
                        }
                    }
                }
                if (found == 0) {
                    Processor.getInstance().getLastSearch().add(task);
                }
            }
        }
    }
    
    private int getCriteriaCount(String date, List<String> keywords, List<String> tags) {
        int count = 0;
        if (date != "")
            count++;
        if (!keywords.isEmpty()) {
            count++;
        }
        if (!tags.isEmpty()) {
            count++;
        }
        return count;
    }
    
    
    /*Obsolete Codes below, due to changing of conditions for Search Command
    
    /* Performs search using date
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
    private void searchUsingKeyOrTags(List<String> keywords, List<String> tags) {
        /*List<Task> toDoTasks = Processor.getInstance().getFile().getToDoTasks();
        for (Task task: toDoTasks) {       
            boolean found = isTagged(task, tags);
            if (!found) {
                found = containsKeyword(task, keywords);
            }
        }
    }
    
    /** Checks if a Task is tagged under a tag in a List of tags
    private boolean isTagged(Task task, List<String> tags) {
        for (String tag: tags) {
            for (String taskTag : task.getTags()) {
                if (taskTag.contains(tag)) {
                    Processor.getInstance().getLastSearch().add(task);
                    return true;
                }
            }
        }
        return false;
    }
    
    /** Checks if a Task contains a certain keyword in the List of keywords
    private boolean containsKeyword(Task task, List<String> keywords) {
        for (String key: keywords) {
            if (task.getName().toLowerCase().contains(key.toLowerCase())) {
                Processor.getInstance().getLastSearch().add(task);
                return true;
            }
        }
        return false;
    }*/
}
