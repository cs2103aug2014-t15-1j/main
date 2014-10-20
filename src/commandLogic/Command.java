package commandLogic;

import java.util.List;

import commandLogic.Result.ResultType;

public abstract class Command {

    // Variables for all Commands
    protected CommandType type;
    protected String error;
    
    public CommandType getType() {
        return this.type;
    }
    
    public String getError() {
        return this.error;
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
    
    protected Result execute(boolean userInput) {
        return new Result(null, false, CommandType.ERROR, ResultType.TASK);
    }
    
    protected Result executeComplement() {
        return new Result(null, false, CommandType.ERROR, ResultType.TASK);
    }
    
    protected void setType(CommandType type) {
        this.type = type;
    }

}
