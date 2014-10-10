package Parser;

import java.util.List;

import Logic.CommandType;

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

}
