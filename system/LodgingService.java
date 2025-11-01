/**
 * @author Tomás Silvestre 68594 tm.silvestre@campus.fct.unl.pt
 * @author Ricardo Laur 68342 r.laur@campus.fct.unl.pt
 */
package system;
public interface LodgingService extends StudentsStoringService {

    /**
     * Check whether the lodging still has free rooms.
     *
     * @return true iff there are free rooms, false otherwise.
     */
    boolean hasFreeRooms();
}