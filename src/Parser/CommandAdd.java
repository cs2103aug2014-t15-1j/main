package Parser;

import java.util.ArrayList;

public class CommandAdd extends Command {

    private String name;
    private String more;
    private String due;
    private String start;
    private String end;
    private String priority;
    private ArrayList<String> tags = new ArrayList<String>();

    public CommandAdd(ArrayList<TaskParam> content) {
        super(content);
        for (TaskParam param : content) {
            switch (param.getName()){
                case "name":
                case "n":
                    this.name = param.getField();
                    break;
                    
                case "more":
                case "m":
                    this.more = param.getField();
                    break;
                    
                case "due":
                case "d":
                    this.due = param.getField();
                    break;
                    
                case "start":
                case "s":
                    this.start = param.getField();
                    break;
                    
                case "end":
                case "e":
                    this.end = param.getField();
                    break;
                    
                case "priority":
                case "p":
                    this.priority = param.getField();
                    break;
                    
                case "tag":
                    this.tags.add(param.getField());
                    break;
                    
                default:
                    System.out.println("Houston, we have a problem.");    
            }
        }
    }
    
    public String toString() {
        
        String result = "name: " + name;
        result = result.concat("\n" + "more: " + more);
        result = result.concat("\n" + "due: " + due);
        result = result.concat("\n" + "start: " + start);
        result = result.concat("\n" + "end: " + end);
        result = result.concat("\n" + "priority: " + priority);
        result = result.concat("\n" + "tags: " + tags);
        
        return result;
    }

}
