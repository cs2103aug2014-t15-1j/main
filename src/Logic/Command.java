package Logic;

import java.util.List;

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
        // Stub
        return null;
    }
    
    public List<String> getTags() {
        // Stub
        return null;
    }
    
    public List<String> getKeywords() {
        // Stub
        return null;
    }
    
    protected Result execute(boolean userInput) {
        return new Result(null, false, CommandType.ERROR);
    }
    
    protected Result executeComplement() {
        return new Result(null, false, CommandType.ERROR);
    }
    
    protected void setType(CommandType type) {
        this.type = type;
    }

}
