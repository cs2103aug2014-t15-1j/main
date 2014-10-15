package Logic;

import java.util.List;

import Parser.TaskParam;

public class CommandBlock extends Command {

    // Block dates range [get("start"), get("end"); returns date]
    // If it's only 1 day, start = end
    private String start;
    private String end;

    private CommandUnblock cmdUnblock;
    
    public CommandBlock(List<TaskParam> content) {
        this(content, false);
    }
    
    protected CommandBlock(List<TaskParam> content, boolean isComplement) {
        if (content.isEmpty()) {
            this.type = CommandType.ERROR;
            this.error = "No arguments for block";
        } else {
            this.type = CommandType.BLOCK;

            for (TaskParam param : content) {
                switch (param.getName()) {
                    case "start":
                        this.start = param.getField();
                        break;

                    case "end":
                        this.end = param.getField();
                        break;

                    default:
                        this.type = CommandType.ERROR;
                        this.error = "Block constructor parameter error";
                }
            }
            if (!isComplement) {
                initialiseComplementCommand(content);
            }
        }
    }

    private void initialiseComplementCommand(List<TaskParam> content) {
        this.cmdUnblock = new CommandUnblock(content, true);
    }
    
    public String get(String field) {
        switch (field) {
            case "start":
                return this.start;

            case "end":
                return this.end;

            default:
                return null;
        }
    }

    public String toString() {
        String result = "\n[[ CMD-BLOCK: ]]";
        result = result.concat("\nstart: " + start);
        result = result.concat("\nend: " + end);

        return result;
    }
    

    /**
     * Check if the date is blocked
     * @param cmd
     * @return Result
     */
    protected Result execute(boolean userInput) {
        Processor.getLogger().info("Executing 'Block' Command...");
        return new Result(null, false, CommandType.ERROR);
    }
    
    protected Result executeComplement() {
        return this.cmdUnblock.execute(false);
    }
}
