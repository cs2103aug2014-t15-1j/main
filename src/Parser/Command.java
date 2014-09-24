package Parser;
import java.util.ArrayList;


public abstract class Command {
    
    // Basic task info
    protected String type;
    
    // Consideration: "edit last"
    // Also can just be a string, let the logic handle?
    protected static Command last;
    
    // 'Add' and 'Edit' fields
    protected String name;
    protected String more;
    protected String due;
    protected String start;
    protected String end;
    protected String priority;
    protected String[] tags;
    protected String delete;
    
    // Delete fields [all, search, id, date/date_range, done]
    protected String delete_type;
    protected String delete_field;
    
    // Restore fields [all, id]
    protected String restore_type;
    protected String restore_id;
    
    // Search fields [combine-able?]
    protected String[] search_tags;
    protected String[] search_keywords;
    protected String[] search_dates;      // single date: start=end
    
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
    
    protected Command(){

    }
    
    
}
