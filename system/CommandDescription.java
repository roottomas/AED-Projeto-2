/**
 * @author Tomás Silvestre 68594 tm.silvestre@campus.fct.unl.pt
 * @author Ricardo Laur 68342 r.laur@campus.fct.unl.pt
 */
package system;
public enum CommandDescription {
    BOUNDS("Defines the new geographic bounding rectangle"),
    SAVE("Saves the current geographic bounding rectangle to a text file"),
    LOAD("Load a geographic bounding rectangle from a text file"),
    SERVICE("Adds a new service to the current geographic bounding rectangle. The service may be eating, lodging or leisure"),
    SERVICES("Displays the list of services in current geographic bounding rectangle, in order of registration"),
    STUDENT("Adds a student to the current geographic bounding rectangle"),
    STUDENTS("Lists all the students or those of a given country in the current geographic bounding rectangle, in alphabetical order of the student's name"),
    LEAVE("Removes a student from the the current geographic bounding rectangle"),
    GO("Changes the location of a student to a leisure service, or eating service"),
    MOVE("Changes the home of a student"),
    USERS("List all students who are in a given service (eating or lodging)"),
    STAR("Evaluates a service"),
    WHERE("Locates a student"),
    VISITED("Lists locations visited by one student"),
    RANKING("Lists services ordered by star"),
    RANKED("Lists the service(s) of the indicated type with the given score that are closer to the student location"),
    TAG("Lists all services that have at least one review whose description contains the specified word"),
    FIND("Finds the most relevant service of a certain type, for a specific student"),
    HELP("Shows the available commands"),
    EXIT("Terminates the execution of the program");

    private final String description;

    CommandDescription(String description) {
        this.description = description;
    }

    /**
     * Return a short description of the command.
     *
     * @return description string
     */
    public String getDescription() {
        return description;
    }

    /**
     * Build a help message listing all commands and their descriptions.
     *
     * @return multi-line help message
     */
    public static String getHelpMessage() {
        StringBuilder sb = new StringBuilder();
        for (CommandDescription cmd : values()) {
            sb.append(cmd.name().toLowerCase())
                    .append(" - ")
                    .append(cmd.getDescription())
                    .append("\n");
        }
        return sb.toString();
    }
}