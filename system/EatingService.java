/**
 * @author Tomás Silvestre 68594 tm.silvestre@campus.fct.unl.pt
 * @author Ricardo Laur 68342 r.laur@campus.fct.unl.pt
 */
package system;
public interface EatingService extends StudentsStoringService {
    /**
     * Check whether the eating service still has free seats.
     *
     * @return true iff there are free seats available, false otherwise.
     */
    boolean hasFreeSeats();
}