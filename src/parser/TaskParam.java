package parser;

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

    public void setField(String str) {
        this.field = str.trim();
    }

    public void addToField(String str) {
        if (str.length() > 0) {
            if (field.length() > 0) {
                this.field = this.field.concat(" " + str.trim());
            } else {
                this.field = str.trim();
            }
        }
    }

    @Override
    public String toString() {
        return "[ " + name + " // " + field + " ]";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (obj == null || (obj.getClass() != this.getClass())) {
            return false;
        }

        TaskParam param = (TaskParam) obj;
        return name.equals(param.getName()) && field.equals(param.getField());
    }

}
