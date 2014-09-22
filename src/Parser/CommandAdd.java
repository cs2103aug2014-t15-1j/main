package Parser;


public class CommandAdd extends Command {

    private String name;
    private String more;
    private String due;
    private String start;
    private String end;
    private String priority;
    private String[] tags;

    public CommandAdd(String[] content) {
        // OR: super();
        super(content);
        // TODO Auto-generated constructor stub
    }

}
