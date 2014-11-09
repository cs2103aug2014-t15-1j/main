package logic;

import java.util.ArrayList;
import java.util.List;

import database.Task;
import parser.objects.TaskParam;

/**
 * This class extends Command and does the display operations. The display
 * ranges from a single Task to upcoming/todo Tasks.
 * 
 * @author A0110751W
 *
 */
public class CommandDisplay extends Command {

    private String rangeType = "";

    private String id = "";

    public CommandDisplay(List<TaskParam> content) {
        assert (content != null);
        assert (!content.isEmpty());
        this.type = CommandType.DISPLAY;
        for (TaskParam param : content) {
            constructUsingParam(param);
        }
    }

    private void constructUsingParam(TaskParam param) {
        switch (param.getName()) {
            case PARAM_RANGE_TYPE:
                this.rangeType = param.getField();
                break;

            case PARAM_ID:
                this.id = param.getField();
                break;

            default:
                assert false : "Invalid constructor param - Received: " +
                               param.getName();
        }
    }

    /**
     * This method executes the "display" operation. It displays a list of
     * <code>Task</code>. Depending on the rangeType specified, the display tab
     * will differ from one another.
     * <p>
     * Allows <i>display</i>, <i>display {@literal <id>}</i>, <i>display
     * {@literal <rangeType>}</i> <br>
     * Allows <i>show</i>, <i>show {@literal <id>}</i>, <i>show
     * {@literal <rangeType>}</i>
     * 
     * @param userInput
     *            - Default is True (When user entered a command)
     * 
     * @return {@link logic.Result#Result(List, boolean, CommandType, boolean, String)
     *         Result}
     */
    @Override
    protected Result execute(boolean userInput) {
        Processor.log("Executing 'Display' Command...");

        Processor processor = Processor.getInstance();
        List<Task> list = new ArrayList<Task>();
        boolean success = true;
        String displayTab = DISPLAY_TAB_RESULT;

        switch (rangeType) {
            case RANGE_TYPE_ALL:
                displayTab = DISPLAY_TAB_ALL;
                list = processor.fetchAllTasks();
                break;

            case RANGE_TYPE_ID:
                int taskId = Integer.parseInt(id);
                list.add(processor.fetchTaskById(taskId));
                break;

            case RANGE_TYPE_BLOCK:
                list = processor.fetchBlockTasks();
                break;

            case RANGE_TYPE_DONE:
                list = processor.fetchDoneTasks();
                break;

            case RANGE_TYPE_DELETED:
                list = processor.fetchDeletedTasks();
                break;

            case RANGE_TYPE_TODO:
                list = processor.fetchToDoTasks();
                break;

            case RANGE_TYPE_TODAY:
                displayTab = DISPLAY_TAB_TODAY;
                list = processor.fetchTodayTasks();
                break;

            case RANGE_TYPE_TOMORROW:
                displayTab = DISPLAY_TAB_TOMORROW;
                list = processor.fetchTomorrowTasks();
                break;

            case RANGE_TYPE_UPCOMING:
                displayTab = DISPLAY_TAB_UPCOMING;
                list = processor.fetchNextWeekTasks();
                break;

            case RANGE_TYPE_SOMEDAY:
                displayTab = DISPLAY_TAB_SOMEDAY;
                list = processor.fetchFloatingTasks();
                break;

            case RANGE_TYPE_SEARCH:
                list = processor.fetchLastSearch();
                break;

            default:
                assert false : "Invalid rangeType - Received: " + rangeType;

        }

        return new Result(list, success, getType(), displayTab);
    }

    @Override
    public String get(String field) {
        switch (field) {
            case PARAM_RANGE_TYPE:
                return this.rangeType;

            case PARAM_ID:
                return this.id;

            default:
                return null;
        }
    }

    @Override
    public String toString() {
        return "cmddisplay rangetype: " + this.rangeType + " id: " + this.id;
    }
}
