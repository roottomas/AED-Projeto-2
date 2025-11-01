/**
 * @author Tomás Silvestre 68594 tm.silvestre@campus.fct.unl.pt
 * @author Ricardo Laur 68342 r.laur@campus.fct.unl.pt
 */
package system;
public enum CommandType {

    CMD_BOUNDS("BOUNDS"),
    CMD_SAVE("SAVE"),
    CMD_LOAD("LOAD"),
    CMD_SERVICE("SERVICE"),
    CMD_SERVICES("SERVICES"),
    CMD_STUDENT("STUDENT"),
    CMD_STUDENTS("SETUDENTS"),
    CMD_RECTANGLE("RECTANGLE"),
    CMD_LEAVE("LEAVE"),
    CMD_GO("GO"),
    CMD_MOVE("MOVE"),
    CMD_USERS("USERS"),
    CMD_STAR("STAR"),
    CMD_WHERE("WHERE"),
    CMD_VISITED("VISITED"),
    CMD_RANKING("RANKING"),
    CMD_RANKED("RANKED"),
    CMD_TAG("TAG"),
    CMD_FIND("FIND"),
    CMD_HELP("HELP"),
    CMD_EXIT("EXIT");


    private String command;

    CommandType(String command) {
        this.command = command;
    }

    /**
     * Return the textual command associated with this enum value.
     *
     * @return command token
     */
    public String toString() {
        return command;
    }

    /**
     * Convert an uppercase command token to the corresponding enum value.
     * Returns null for unknown tokens.
     *
     * @param command uppercase command token
     * @return corresponding CommandType or null if not found
     */
    public static CommandType getEnum(String command) {
        switch (command) {
            case "BOUNDS":
                return CMD_BOUNDS;
            case "SAVE":
                return CMD_SAVE;
            case "LOAD":
                return CMD_LOAD;
            case "SERVICE":
                return CMD_SERVICE;
            case "SERVICES":
                return CMD_SERVICES;
            case "STUDENT":
                return CMD_STUDENT;
            case "STUDENTS":
                return CMD_STUDENTS;
            case "RECTANGLE":
                return CMD_RECTANGLE;
            case "LEAVE":
                return CMD_LEAVE;
            case "GO":
                return CMD_GO;
            case "MOVE":
                return CMD_MOVE;
            case "USERS":
                return CMD_USERS;
            case "STAR":
                return CMD_STAR;
            case "WHERE":
                return CMD_WHERE;
            case "VISITED":
                return CMD_VISITED;
            case "RANKING":
                return CMD_RANKING;
            case "RANKED":
                return CMD_RANKED;
            case "TAG":
                return CMD_TAG;
            case "FIND":
                return CMD_FIND;
            case "HELP":
                return CMD_HELP;
            case "EXIT":
                return CMD_EXIT;
            default:
                return null;
        }
    }
}