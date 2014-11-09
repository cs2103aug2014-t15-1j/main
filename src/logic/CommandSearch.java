package logic;

import java.util.List;
import java.util.ArrayList;

import parser.objects.TaskParam;
import database.Task;

/**
 * This class extends abstract class Command. CommandSearch performs search operations.
 * Allows searching of Tasks through the use of Keywords and Dates and Tags.
 * 
 * @author A0110751W
 *
 */
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
     * Executes "search" operation.
     * <p>
     * It will perform search operation based on the criteria of the search.
     * Criteria of search can include <u>Keywords</u> and <u>Date</u> and
     * <u>Tags</u><br>
     * <code>Task</code> will have to meet all the specified criterias.<br>
     * 
     * @return {@link logic.Result#Result(List, boolean, CommandType, boolean)
     *         Result}
     */
    @Override
    protected Result execute(boolean userInput) {
        Processor.log("Executing 'Search' Command...");

        Processor.getInstance().initialiseNewSearchList();
        searchUsingDateAndKeyAndTags(date, keywords, tags);
        return new Result(Processor.getInstance().fetchLastSearch(), true,
                getType(), DISPLAY_TAB_RESULT);
    }

    /**
     * This method adds Tasks that matches the search criteria into the search
     * list
     * 
     * @param date
     * @param keywords
     * @param tags
     */
    private void searchUsingDateAndKeyAndTags(String date,
                                              List<String> keywords,
                                              List<String> tags) {
        int criteriaCount = getCriteriaCount(date, keywords, tags);
        if (criteriaCount >= 0) {
            List<Task> searchRange = getSearchBoundary(status);

            for (Task task : searchRange) {
                addTaskMatchingCriteria(date, keywords, tags, criteriaCount,
                                        task);
            }
        }
    }

    private void addTaskMatchingCriteria(String date, List<String> keywords,
                                         List<String> tags, int criteriaCount,
                                         Task task) {
        int found = criteriaCount;
        found = getMatchingDateCount(date, task, found);
        found = getMatchingCount(keywords, task, found);
        found = getMatchingCount(tags, task, found);
        // Meets all criteria
        if (found == 0) {
            Processor.getInstance().fetchLastSearch().add(task);
        }
    }

    private int getMatchingDateCount(String date, Task task, int found) {
        if (date != "") {
            if (task.getSummary().contains(date)) {
                found--;
            }
        }
        return found;
    }

    private int getMatchingCount(List<String> list, Task task, int found) {
        if (!list.isEmpty()) {
            for (String key : list) {
                if (task.getSummary().toLowerCase().contains(key.toLowerCase())) {
                    found--;
                    break;
                }
            }
        }
        return found;
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
                assert false : "Invalid search range boundary - Received: " +
                               status;
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
