package parser;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import logic.Command;
import logic.CommandAdd;
import logic.CommandBlock;
import logic.CommandDelete;
import logic.CommandDisplay;
import logic.CommandDone;
import logic.CommandEdit;
import logic.CommandOthers;
import logic.CommandRestore;
import logic.CommandSearch;
import logic.CommandTodo;
import logic.CommandUnblock;
import database.DateTime;
import database.Task;
import database.TaskType;

/**
 * This class is the interface/facade for all other parsing methods. These
 * include parsing from user input, parsing from file, as well as date-related
 * methods. The facade pattern is used to simplify interactions with the parser
 * package.
 * 
 * @author Yeo Zi Xian, Justin (A0116208N)
 *
 */
public class Parser {

    /**
     * Parses the input String into a Command of the relevant type. The Command
     * will store relevant information contained in the String.
     * 
     * @return Command object of the relevant subclass
     * @throws IllegalArgumentException
     *             when a user input is invalid. The exception will contain a
     *             message related to the error.
     */
    public static Command parse(String input) throws IllegalArgumentException {
        return InputParser.parse(input);
    }

    /**
     * Forms a <code>Task</code> object by parsing a <code>String</code>
     * containing the stored string literals.
     * <p>
     * Note that the input <code>String</code> must be of the given
     * format(below), contain all four parameters names ("start:" to "type:"),
     * and have spaces between tags and parameter names. The position of the
     * tags is flexible as long as it comes after "start:".
     * 
     * @param text
     *            format:
     *            {@literal "<name> ### start: <date/time> due: <date/time> completed: 
     * <date/time> <tags> type: <type>"}
     */
    public static Task parseToTask(String text) {
        return FileParser.parse(text);
    }

}
