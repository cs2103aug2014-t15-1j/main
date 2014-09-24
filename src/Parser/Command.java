package Parser;

import Logic.CommandType;

public class Command {

    // Variables for all Commands
    protected CommandType type;
    protected String error;

    // Delete fields [all, search, id, date/date_range, done]
    protected String delete_type;
    protected String delete_field;

    // Restore fields [all, id]
    protected String restore_type;
    protected String restore_id;

    // Search fields [combine-able?]
    protected String[] search_tags;
    protected String[] search_keywords;
    protected String[] search_dates; // single date: start=end

    // Display fields [all, id, block - only id needs field]
    protected String display_type;
    protected String display_field;

    // Block fields [single date: start=end]
    protected String[] block_dates_times;

    // Done fields [all, id, date/date_range]
    protected String done_type;
    protected String done_field;

    // Undone fields [last, id]
    protected String undone_type;
    protected String undone_id;
   

    public Command(){
        this.type = CommandType.ERROR;
        this.error = "Empty Command constructor";
    }
    
    public Command(String type) {
        switch(type.toLowerCase()) {
            case "clear":
                this.type = CommandType.CLEAR;
                break;
            
            case "exit":
                this.type = CommandType.EXIT;
                break;
            
            case "joke":
                this.type = CommandType.JOKE;
                break;
            
            case "redo":
                this.type = CommandType.REDO;
                break;
                
            case "undo":
                this.type = CommandType.UNDO;
                break;
            
            default:
                this.type = CommandType.ERROR;
                this.error = "Error in Command constructor (command type)";
        }
    }

    public CommandType getType() {
        return this.type;
    }

}
