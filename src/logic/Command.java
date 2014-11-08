package logic;

import java.util.List;

import parser.Parser;
import database.Task;

public abstract class Command {

    protected CommandType type = null;

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

    protected static final String DELETE_NAME = "name";
    protected static final String DELETE_DUE = "due";
    protected static final String DELETE_START = "start";
    protected static final String DELETE_TAGS = "tags";
    
    protected static final String DISPLAY_TAB_ALL = "all";
    protected static final String DISPLAY_TAB_TODAY = "today";
    protected static final String DISPLAY_TAB_TOMORROW = "tomorrow";
    protected static final String DISPLAY_TAB_UPCOMING = "upcoming";
    protected static final String DISPLAY_TAB_SOMEDAY = "someday";
    protected static final String DISPLAY_TAB_RESULT = "result";
    protected static final String DISPLAY_TAB_NO_CHANGE = "nochange";
    
    protected static final String ERROR_BLOCK_ADD = "Unable to add Task, this clashes with a Block Task!";
    
    public CommandType getType() {
        assert this.type != null : "CommandType cannot be null!";
        return this.type;
    }

    public String get(String str) {
        return null;
    }

    public List<String> getTags() {
        return null;
    }

    public List<String> getKeywords() {
        return null;
    }

    public List<String> getDelete() {
        return null;
    }

    protected Result execute(boolean userInput) {
        return new Result();
    }

    protected Result executeComplement() {
        return new Result();
    }

    protected void setType(CommandType type) {
        this.type = type;
    }

    protected String getDisplayTab(Task toDelete) {
        String displayTab = DISPLAY_TAB_ALL;
        String todayDate = Parser.getCurrDateStr();
        if (toDelete.getDue().getDate().equals(todayDate) ||
            toDelete.getStart().getDate().equals(todayDate)) {
            displayTab = DISPLAY_TAB_TODAY;
        }
        return displayTab;
    }
}
