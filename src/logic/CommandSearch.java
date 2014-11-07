package logic;

import java.util.List;
import java.util.ArrayList;

import parser.TaskParam;
import database.Task;

public class CommandSearch extends Command {

    private List<String> tags = new ArrayList<String>();
    
    private List<String> keywords = new ArrayList<String>();
    
    private String date = "";
    
    private String status = "todo";

    public CommandSearch(List<TaskParam> content) {
        assert (!content.isEmpty());
        this.type = CommandType.SEARCH;

        for (TaskParam param : content) {
            constructUsingParam(param);
        }
    }

    private void constructUsingParam(TaskParam param) {
        switch (param.getName()) {
            case PARAM_STATUS:
                this.status = param.getField();
                break;

            case PARAM_DATE:
                this.date = param.getField();
                break;

            case PARAM_TAG:
                this.tags.add(param.getField());
                break;

            case PARAM_WORD:
                this.keywords.add(param.getField());
                break;

            default:
                assert false : "Invalid input - Received: " + param.getName();
        }
    }

    @Override
    public String get(String field) {
        switch (field) {
            case PARAM_DATE:
                return date;

            case PARAM_STATUS:
                return status;

            default:
                return null;
        }
    }

    @Override
    public List<String> getTags() {
        return this.tags;
    }
    
    @Override
    public List<String> getKeywords() {
        return this.keywords;
    }

    /**
     * Executes "search" operation Allows search <date> <key1> <key2> #tag1
     * #tag2
     * 
     * @return Result
     */
    @Override
    protected Result execute(boolean userInput) {
        if (Processor.LOGGING_ENABLED) {
            Processor.getLogger().info("Executing 'Search' Command...");
        }
        
        Processor.getInstance().initialiseNewSearchList();
        searchUsingDateAndKeyAndTags(date, keywords, tags);
        return new Result(Processor.getInstance().fetchLastSearch(), true,
                getType(), DISPLAY_TAB_RESULT);
    }

    private void searchUsingDateAndKeyAndTags(String date,
                                              List<String> keywords,
                                              List<String> tags) {
        int criteriaCount = getCriteriaCount(date, keywords, tags);
        if (criteriaCount >= 0) {
            List<Task> searchRange = getSearchBoundary(status);

            for (Task task : searchRange) {
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
                    Processor.getInstance().fetchLastSearch().add(task);
                }
            }
        }
    }

    private int getCriteriaCount(String date, List<String> keywords,
                                 List<String> tags) {
        int count = 0;
        if (date != "") {
            count++;
        }

        if (!keywords.isEmpty()) {
            count++;
        }

        if (!tags.isEmpty()) {
            count++;
        }

        return count;
    }

    private List<Task> getSearchBoundary(String status) {
        List<Task> searchRange = Processor.getInstance().getFile()
                .getToDoTasks();
        switch (status) {
            case RANGE_TYPE_ALL:
                searchRange = Processor.getInstance().getFile().getAllTasks();
                break;

            case RANGE_TYPE_DONE:
                searchRange = Processor.getInstance().getFile().getDoneTasks();
                break;

            case RANGE_TYPE_DELETED:
                searchRange = Processor.getInstance().getFile()
                        .getDeletedTasks();
                break;

            default:
                break;
        }
        return searchRange;
    }

    @Override
    public String toString() {
        return "cmdsearch status: " + this.status + " date: " + this.date +
               " tags: " + this.tags + " keywords: " + this.keywords;
    }

    /*
     * Obsolete Codes below, due to changing of conditions for Search Command
     * 
     * /* Performs search using date private void searchUsingDate(String date) {
     * List<Task> toDoTasks = Processor.getInstance().getFile().getToDoTasks();
     * for (Task t: toDoTasks) { if (t.getDue().equals(date)) {
     * Processor.getInstance().getLastSearch().add(t); } } }
     * 
     * /** Performs search using Keywords or Tags Tries to find if tags is
     * present first before searching for keywords
     * 
     * private void searchUsingKeyOrTags(List<String> keywords, List<String>
     * tags) { /*List<Task> toDoTasks =
     * Processor.getInstance().getFile().getToDoTasks(); for (Task task:
     * toDoTasks) { boolean found = isTagged(task, tags); if (!found) { found =
     * containsKeyword(task, keywords); } } }
     * 
     * /** Checks if a Task is tagged under a tag in a List of tags private
     * boolean isTagged(Task task, List<String> tags) { for (String tag: tags) {
     * for (String taskTag : task.getTags()) { if (taskTag.contains(tag)) {
     * Processor.getInstance().getLastSearch().add(task); return true; } } }
     * return false; }
     * 
     * /** Checks if a Task contains a certain keyword in the List of keywords
     * private boolean containsKeyword(Task task, List<String> keywords) { for
     * (String key: keywords) { if
     * (task.getName().toLowerCase().contains(key.toLowerCase())) {
     * Processor.getInstance().getLastSearch().add(task); return true; } }
     * return false; }
     */

}
