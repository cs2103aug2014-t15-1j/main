package objects;

import parser.objects.TaskParam;


public class TaskParamStub extends TaskParam {

    private String name;
    private String field;
        
    public TaskParamStub(String name, String field) {
        super("", "");
        this.name = name.trim();
        this.field = field.trim();
    }

    public String getName() {
        return name;
    }

    public String getField() {
        return field;
    }
    
}
