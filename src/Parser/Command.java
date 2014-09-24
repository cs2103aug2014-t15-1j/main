package Parser;

import java.util.ArrayList;

import Logic.CommandType;

public abstract class Command {

    // Variables for all Commands
    protected CommandType type;
    protected String error;

    // Tags
    protected ArrayList<String> tags = new ArrayList<String>();
    
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
    
    public CommandType getType() {
        return this.type;
    }
    
    public String getError() {
        return this.error;
    }
    
    public String get() {
        return null;
    }
    
    public String get(String str) {
        return null;
    }
    
    public ArrayList<String> getTags() {
        return null;
    }

}
