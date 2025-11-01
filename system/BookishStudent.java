/**
 * @author Tomás Silvestre 68594 tm.silvestre@campus.fct.unl.pt
 * @author Ricardo Laur 68342 r.laur@campus.fct.unl.pt
 */
package system;
public interface BookishStudent extends LocationStoringStudent {

    /**
     * Change the student's current location to the provided service.
     * Implementations may ignore certain service types (e.g. Lodging).
     *
     * @pre service != null
     * @param service target service to move to
     */
    void changeLocation(ServiceClass service);
}