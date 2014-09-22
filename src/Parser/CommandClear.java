package Parser;

import java.util.ArrayList;

public class CommandClear extends Command {

    private String name;
    private String more;
    private String due;
    private String start;
    private String end;
    private String priority;
    private String[] tags;

    public CommandClear() {
        super();
    }
    
    public CommandClear(ArrayList<TaskParam> content) {
        // OR: super();
        super(content);
        // TODO Auto-generated constructor stub
    }

}
