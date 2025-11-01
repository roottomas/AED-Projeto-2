/**
 * @author Tomás Silvestre 68594 tm.silvestre@campus.fct.unl.pt
 * @author Ricardo Laur 68342 r.laur@campus.fct.unl.pt
 */
package system;
public interface OutgoingStudent extends LocationStoringStudent {

    /**
     * Request the student to change location to the supplied service.
     *
     * @pre service != null
     * @param service target service (may be eating, leisure or lodging)
     */
    void changeLocation(ServiceClass service);
}