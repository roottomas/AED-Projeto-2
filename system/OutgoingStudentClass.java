/**
 * @author Tomás Silvestre 68594 tm.silvestre@campus.fct.unl.pt
 * @author Ricardo Laur 68342 r.laur@campus.fct.unl.pt
 */
package system;
import java.io.Serializable;

public class OutgoingStudentClass extends LocationStoringStudentClass implements OutgoingStudent, Serializable {
    private static final long serialVersionUID = 0L;

    /**
     * OutgoingStudentClass constructor.
     *
     * @pre name != null && country != null && home != null && type != null
     * @param name student name
     * @param country student country
     * @param home lodging service that is the student's home
     * @param type student type enum
     */
    public OutgoingStudentClass(String name, String country, LodgingServiceClass home, StudentType type) {
        super(name, country, home, type);
        insertService(home);
    }

    /**
     * Move the student to the provided service. If the service is a lodging,
     * the lodging is recorded in the visited list and the student is not moved
     * away from home (lodgings are treated as special).
     *
     * @pre service != null
     * @param service destination service
     */
    @Override
    public void changeLocation(ServiceClass service) {
        if (service instanceof LodgingServiceClass lodging) {
            insertService(lodging);
            return;
        }
        moveTo(service);
        insertService(service);
    }
}