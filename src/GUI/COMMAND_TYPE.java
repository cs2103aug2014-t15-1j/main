public enum COMMAND_TYPE {
    HELP("help"), ADD("add"), EDIT("edit"), DELETE("delete"), RESTORE("restore"), SEARCH(
            "search"), DISPLAY("display"), BLOCK("block"), UNBLOCK("unblock"), DONE(
            "done"), TODO("todo"), UNDO("undo"), REDO("redo"), CLEAR("clear"), JOKE(
            "joke"), EXIT("exit");

    private String command;

    private COMMAND_TYPE(final String command) {
        this.command = command;
    }

    public String getValue() {
        return this.command;
    }
}
