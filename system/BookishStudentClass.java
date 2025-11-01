/**
 * @author Tomás Silvestre 68594 tm.silvestre@campus.fct.unl.pt
 * @author Ricardo Laur 68342 r.laur@campus.fct.unl.pt
 */
package system;
import java.io.Serializable;

public class BookishStudentClass extends LocationStoringStudentClass implements BookishStudent, Serializable {
    private static final long serialVersionUID = 0L;

    /**
     * BookishStudent constructor.
     *
     * @pre name != null && country != null && home != null && type == StudentType.bookish
     * @param name student name
     * @param country student country
     * @param home student's home lodging service
     * @param type student type (should be bookish when called)
     */
    public BookishStudentClass(String name, String country, LodgingServiceClass home, StudentType type) {
        super(name, country, home, type);
    }

    /**
     * Change student's location.
     * If the target service is a LodgingService, do nothing.
     * Otherwise, move the student to the new service and, if it is a leisure service,
     * record the service in the visited history.
     *
     * @pre service != null
     * @param service target service
     */
    @Override
    public void changeLocation(ServiceClass service) {
        if (service instanceof LodgingServiceClass) {
            return;
        }
        moveTo(service);
        if (service instanceof LeisureServiceClass) {
            insertService(service);
        }
    }
}