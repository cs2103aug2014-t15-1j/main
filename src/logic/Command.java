//@author A0110751W
package logic;

import objects.Task;
import parser.Parser;

/**
 * This is an abstract class Command, for the implementation of Command Pattern.
 * Different command type will be categorised and extends this class. This class
 * also contains the common parameters used in the Command classes.
 *
 */

public abstract class Command {

    protected CommandType type = null;

    /** Parameters used in the construction of Command object */
    protected static final String PARAM_ID = "id";
    protected static final String PARAM_NAME = "name";
    protected static final String PARAM_DUE = "due";
    protected static final String PARAM_START = "start";
    protected static final String PARAM_TAG = "tag";
    protected static final String PARAM_TO = "to";
    protected static final String PARAM_FROM = "from";
    protected static final String PARAM_RANGE_TYPE = "rangeType";
    protected static final String PARAM_DATE = "date";
    protected static final String PARAM_DELETE = "delete";
    protected static final String PARAM_STATUS = "status";
    protected static final String PARAM_WORD = "word";

    /** Parameters applicable to CommandDelete only: */
    protected static final String DELETE_NAME = "name";
    protected static final String DELETE_DUE = "due";
    protected static final String DELETE_START = "start";
    protected static final String DELETE_TAGS = "tags";

    /** Different Range Types available */
    protected static final String RANGE_TYPE_ALL = "all";
    protected static final String RANGE_TYPE_BLOCK = "block";
    protected static final String RANGE_TYPE_SEARCH = "search";
    protected static final String RANGE_TYPE_ID = "id";
    protected static final String RANGE_TYPE_DONE = "done";
    protected static final String RANGE_TYPE_DELETED = "deleted";
    protected static final String RANGE_TYPE_TODO = "todo";
    protected static final String RANGE_TYPE_DATE = "date";
    protected static final String RANGE_TYPE_TODAY = "today";
    protected static final String RANGE_TYPE_TOMORROW = "tomorrow";
    protected static final String RANGE_TYPE_SOMEDAY = "someday";
    protected static final String RANGE_TYPE_UPCOMING = "upcoming";

    /** Display Tabs */
    protected static final String DISPLAY_TAB_ALL = "all";
    protected static final String DISPLAY_TAB_TODAY = "today";
    protected static final String DISPLAY_TAB_TOMORROW = "tomorrow";
    protected static final String DISPLAY_TAB_UPCOMING = "upcoming";
    protected static final String DISPLAY_TAB_SOMEDAY = "someday";
    protected static final String DISPLAY_TAB_RESULT = "result";
    protected static final String DISPLAY_TAB_NO_CHANGE = "nochange";

    /** Error messages */
    protected static final String ERROR_BLOCK_ADD = "Unable to add Task, this clashes with a Block Task!";

    public CommandType getType() {
        assert this.type != null : "CommandType cannot be null!";
        return this.type;
    }

    public String get(String str) {
        return null;
    }

    abstract protected Result execute(boolean userInput);

    abstract protected Result executeComplement();

    /**
     * This method returns the appropriate tab to change to for a Task. <br>
     * For <code>Tasks</code> that have <code>due</code> dates due today, it
     * should switch to the <code>"today"</code> tab. Else, it should switch to
     * the <code>"all"</code> tab.
     * <p>
     * <i>Currently used only when dealing with only one Task in an
     * operation.</i>
     * 
     * @param task
     *            - Task
     * @return String
     */
    protected String getDisplayTab(Task task) {
        String displayTab = DISPLAY_TAB_ALL;
        String todayDate = Parser.getCurrDateStr();
        if (task.getDue().getDate().equals(todayDate) ||
            task.getStart().getDate().equals(todayDate)) {
            displayTab = DISPLAY_TAB_TODAY;
        }
        return displayTab;
    }
}
