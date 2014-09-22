package Parser;
import java.util.ArrayList;


public abstract class Command {
    // Full content for tracing/debugging purposes
    private String[] content;
    
    // Basic task info
    private String type;
    
    // Unique ID. Counter keeps track of UIDs assigned.
    private int uid;
    private static int uid_counter = 1; // how to set this properly if program closes? get storage to save? init()?
    
    // Consideration: "edit last"
    // Also can just be a string, let the logic handle?
    private static Command last;
    
    // 'Add' and 'Edit' fields
    private String name;
    private String more;
    private String due;
    private String start;
    private String end;
    private String priority;
    private String[] tags;
    private String delete;
    
    // Delete fields [all, search, id, date/date_range, done]
    private String delete_type;
    private String delete_field;
    
    // Restore fields [all, id]
    private String restore_type;
    private String restore_id;
    
    // Search fields [combine-able?]
    private String[] search_tags;
    private String[] search_keywords;
    private String[] search_dates;      // single date: start=end
    
    // Display fields [all, id, block - only id needs field]
    private String display_type;
    private String display_field;
    
    // Block fields [single date: start=end]
    private String[] block_dates_times;
    
    // Done fields [all, id, date/date_range]
    private String done_type;
    private String done_field;
    
    // Undone fields [last, id]
    private String undone_type;
    private String undone_id;
    
    protected Command(){
        this.uid = uid_counter;
        uid_counter++;
    }
    
    protected Command(String[] content){
        this.content = content.clone();
        this.uid = uid_counter;
        uid_counter++;
    }
    
    
}
