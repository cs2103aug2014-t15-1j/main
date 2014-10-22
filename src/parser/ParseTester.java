package parser;

public class ParseTester {
    // ========== TESTING (TO REMOVE) ==========//

    public static void main(String[] args) {
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

        // TEST ADD
        System.out.println(Parser.parse("add do homework due: 23/04/2014 start: 22/04/2014 0300 end: 0300 21/04/2014"));
    
        // TEST toBLOCK
        System.out.println(Parser.parseToBlock("23/04/2014 0000 to 23/04/2014 2359"));
        System.out.println(Parser.parseToBlock("23/04/2014 to 23/04/2014 2359"));
    }
}
