package logic;

import java.util.List;

public abstract class Command {

    protected CommandType type = null;
    protected String error = "";

    public CommandType getType() {
        assert this.type != null : "CommandType cannot be null!";
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

}
