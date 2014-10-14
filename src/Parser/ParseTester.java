package Parser;

import Logic.Command;
import Storage.Task;

public class ParseTester {
    // ========== TESTING (TO REMOVE) ==========//

    private static String tempTaskToString(Task task) {
        String result = "\n[[ Task ]]";
        result = result.concat("\nName: " + task.getName());
        result = result.concat("\nDue: " + task.getDue());
        result = result.concat("\nStart: " + task.getStart());
        result = result.concat("\nEnd: " + task.getEnd());
        result = result.concat("\nTags: " + task.getTags());
        result = result.concat("\nDoneness: " +
                               (task.isDone() ? "#Done" : "#ToDo"));
        return result;
    }

    public static void main(String[] args) {
        // TODO: Test "\n" when input from command line

        // TEST DELETE System.out.println(Parser.parse("delete all"));
        System.out.println(Parser.parse("delete search"));
        System.out.println(Parser.parse("delete done"));
        System.out.println(Parser.parse("delete 11"));
        System.out.println(Parser.parse("delete days"));
        System.out.println(Parser.parse("delete"));

        // TEST HELP System.out.println(Parser.parse("help me"));
        System.out.println(Parser.parse("help"));
        System.out.println(Parser.parse("help all"));
        System.out.println(Parser.parse("help add"));

        // TEST EDIT
        System.out
                .println(Parser
                        .parse("edit 1 ten twenty start: #cs2103 #cs2103 #CS2103 end: due: tmr delete: name"));
        System.out.println(Parser
                .parse("edit 2 delete: nil n: to: do: #cs2103 #cs2103"));
        System.out
                .println(Parser
                        .parse("edit 3 delete: name name nil name name n: todo homework delete: name name"));
        System.out.println(Parser.parse("edit one two"));
        System.out.println(Parser.parse("edit"));
        System.out.println(Parser.parse("edit 1"));
        System.out
                .println(Parser
                        .parse("edit 1 name:Start:e:tomorrow n:n:code it x:n:fail n:x:s:fail n:x:fails"));
        System.out.println(Parser
                .parse("edit 1 delete:s:Start n:delete:tomorrow delete:end"));

        // TEST RESTORE
        System.out.println(Parser.parse("restore all"));
        System.out.println(Parser.parse("restore 2"));
        System.out.println(Parser.parse("restore"));
        System.out.println(Parser.parse("restore b"));

        // TEST DISPLAY
        System.out.println(Parser.parse("display all"));
        System.out.println(Parser.parse("display 2"));
        System.out.println(Parser.parse("display block"));
        System.out.println(Parser.parse("display"));
        System.out.println(Parser.parse("display b"));

        // TEST OTHERS
        System.out.println(Parser.parse("undo"));
        System.out.println(Parser.parse("redo"));
        System.out.println(Parser.parse("exit"));

        // TEST UNBLOCK
        System.out.println(Parser.parse("unblock 2"));
        System.out.println(Parser.parse("unblock 2 3"));
        System.out.println(Parser.parse("unblock one"));
        System.out.println(Parser.parse("unblock"));

        // TEST TO-DO
        System.out.println(Parser.parse("todo 1"));
        System.out.println(Parser.parse("todo 1 3"));
        System.out.println(Parser.parse("todo lAst"));
        System.out.println(Parser.parse("todo one"));
        System.out.println(Parser.parse("tODo"));

        // TEST DONE
        System.out.println(Parser.parse("done all"));
        System.out.println(Parser.parse("done 2"));
        System.out.println(Parser.parse("done 23/04/2014"));
        System.out.println(Parser.parse("done 23/04/2014 to 23/05/2014"));

        // TEST SEARCH
        System.out.println(Parser.parse("search"));
        System.out.println(Parser.parse("search "));
        System.out.println(Parser.parse("search one two three"));
        System.out.println(Parser.parse("search #one #two #three"));
        System.out.println(Parser.parse("search #done #tag keyword"));
        System.out.println(Parser.parse("search #done #deleted #todo after"));
        System.out.println(Parser.parse("search 23/04/2014 to 23/05/2014"));
        System.out.println(Parser.parse("search 23/04/2014"));
        System.out.println(Parser
                .parse("search 23/04/2014 to 23/05/2014 #done"));

        // TEST BLOCK // TODO: today? tomorrow? Required?
        System.out.println(Parser.parse("block"));
        System.out.println(Parser.parse("block "));
        System.out.println(Parser.parse("block one two three"));
        System.out.println(Parser.parse("block today"));
        System.out.println(Parser.parse("block today to tomorrow"));

        System.out.println(Parser.parse("block 23/04/2014"));
        System.out.println(Parser.parse("block 23/04/2014 to 23/05/2014"));
        System.out.println(Parser.parse("block 23/04/201"));
        System.out.println(Parser.parse("block 23/4/2014"));
        System.out.println(Parser.parse("block 2/04/2014"));
        System.out.println(Parser.parse("block 23/13/2014"));
        System.out.println(Parser.parse("block 33/04/2014"));
        System.out.println(Parser.parse("block -3/04/2014"));
        System.out.println(Parser.parse("block 03/04/-014"));
        System.out.println(Parser.parse("block 23/04/2014 23/05/2014"));
        System.out.println(Parser.parse("block 23/04/2014 to 23/05/2012"));
        System.out.println(Parser.parse("block 23/04/2012 to 23/05/2014"));
        System.out.println(Parser.parse("block 23/04/1*14"));

    }
}
