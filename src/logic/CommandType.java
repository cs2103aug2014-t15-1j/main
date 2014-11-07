package logic;

/**
 * List below are the Command Type available for the different operations<br>
 * HELP - Displays a list of Commands to user<br>
 * ADD - Add a Task<br>
 * EDIT - Edit an existing Task<br>
 * DELETE - Deletes an existing Task<br>
 * RESTORE - Restores a deleted Task<br>
 * SEARCH - Search using keywords/tags of existing Task<br>
 * DISPLAY - Shows/Displays existing Tasks<br>
 * BLOCK - Blocks a date<br>
 * UNBLOCK - Unblocks a blocked date<br>
 * DONE - Marks a task as done<br>
 * TODO - Marks a task as todo<br>
 * UNDO - Undo last action<br>
 * REDO - Redo last action undone<br>
 * EXIT - Exits from program<br>
 * ERROR - Error found in the command entered (Wrong format)<br>
 */
public enum CommandType {
    HELP, ADD, EDIT, DELETE, RESTORE, SEARCH, DISPLAY, BLOCK, UNBLOCK, DONE, TODO, UNDO, REDO, EXIT, RESET;
}
