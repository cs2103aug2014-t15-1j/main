package Parser;

public class TaskParam {
    private String name;
    private String field;

    public TaskParam(String name, String field) {
        this.name = name.trim();
        this.field = field.trim();
    }

    public String getName() {
        return name;
    }

    public String getField() {
        return field;
    }
    
    public void addToField(String str) {
        if (field.length()>0) {
            this.field = this.field.concat(" " + str.trim());
        } else {
            this.field = str.trim();
        }
    }
    
    public String toString() {
        return "[ " + name + " // " + field + " ]";
    }
    
}
