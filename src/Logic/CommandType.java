package Logic;

/**
 * List below are the Command Type available for the different operations
 * HELP     - Displays a list of Commands to user
 * ADD      - Add a Task
 * EDIT     - Edit an existing Task
 * DELETE   - Deletes an existing Task
 * RESTORE  - Restores a deleted Task
 * SEARCH   - Search using keywords/tags of existing Task
 * DISPLAY  - Shows/Displays existing Tasks
 * BLOCK    - Blocks a date
 * UNBLOCK  - Unblocks a blocked date
 * DONE     - Marks a task as done
 * TODO     - Marks a task as todo
 * UNDO     - Undo last action
 * REDO     - Redo last action undone
 * EXIT     - Exits from program
 * ERROR    - Error found in the command entered (Wrong format)
 */
public enum CommandType {
	HELP,
	ADD,
	EDIT, 
	DELETE,
	RESTORE,
	SEARCH,
	DISPLAY,
	BLOCK,
	UNBLOCK,
	DONE,
	TODO,
	UNDO,
	REDO,
	EXIT,
	ERROR;
}
