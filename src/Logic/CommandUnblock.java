package Logic;

import java.util.List;

import Parser.TaskParam;

public class CommandUnblock extends Command {

    // Only one type of unblock search: "id"
    // Use get("id"); returns string
    protected String id;

    private CommandBlock cmdBlock;
    
    public CommandUnblock(List<TaskParam> content) {
        this(content, false);
    }
    
    protected CommandUnblock(List<TaskParam> content, boolean isComplement) {
        if (content.isEmpty()) {
            this.type = CommandType.ERROR;
            this.error = "No arguments for unblock";
        } else {
            this.type = CommandType.UNBLOCK;

            for (TaskParam param : content) {
                switch (param.getName()) {
                    case "id":
                        this.id = param.getField();
                        break;

                    default:
                        this.type = CommandType.ERROR;
                        this.error = "Unblock constructor parameter error";
                }
            }
            if (!isComplement) {
                initialiseComplementCommand(content);
            }
        }
    }

    
    private void initialiseComplementCommand(List<TaskParam> content) {
        this.cmdBlock = new CommandBlock(content, true);
    }
    
    // TODO: Change to empty get()?
    public String get(String field) {
        switch (field) {
            case "id":
                return this.id;

            default:
                return null;
        }
    }

    /**
     * @return the cmdBlock
     */
    public CommandBlock getCmdBlock() {
        return cmdBlock;
    }

    /**
     * @param cmdBlock the cmdBlock to set
     */
    public void setCmdBlock(CommandBlock cmdBlock) {
        this.cmdBlock = cmdBlock;
    }

    public String toString() {
        String result = "\n[[ CMD-Unblock: ]]";
        result = result.concat("\nid: " + id);

        return result;
    }

    protected Result execute(boolean userInput) {
        Processor.getLogger().info("Executing 'Unblock' Command...");
        return new Result(null, false, CommandType.ERROR);
    }
    
    protected Result executeComplement() {
        return this.cmdBlock.execute(false);
    }
}
