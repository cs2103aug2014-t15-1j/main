package Parser;

public class TaskParam {
    private String name;
    private String field;

    public TaskParam(String name, String field) {
        this.name = name;
        this.field = field;
    }

    public String getName() {
        return name;
    }

    public String getField() {
        return field;
    }
    
    public void addToField(String str) {
        field = field.concat(" " + str);
    }
    
    public String toString() {
        return "[ " + name + " // " + field + " ]";
    }
    
}
