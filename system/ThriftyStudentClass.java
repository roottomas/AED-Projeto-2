/**
 * @author Tomás Silvestre 68594 tm.silvestre@campus.fct.unl.pt
 * @author Ricardo Laur 68342 r.laur@campus.fct.unl.pt
 */
package system;
import java.io.Serializable;
import system.exceptions.*;

public class ThriftyStudentClass extends StudentClass implements ThriftyStudent, Serializable {
    private static final long serialVersionUID = 0L;

    private float cheapestEatingPrice;
    private float cheapestLodgingPrice;

    /**
     * Create a thrifty student and initialise cheapest-known prices from home.
     *
     * @param name student name
     * @param country student country
     * @param home initial home lodging
     * @param type student type
     */
    public ThriftyStudentClass(String name, String country, LodgingServiceClass home, StudentType type) {
        super(name, country, home, type);
        cheapestLodgingPrice = home.getPrice();
        cheapestEatingPrice = Integer.MAX_VALUE;
    }

    /**
     * If moving to an eating service: if the new price is greater than the current cheapestEatingPrice,
     *   the student is distracted (student still goes there but DistractedStudentException is thrown).
     * If moving to a leisure service: just move (no price effect).
     * If moving to a lodging: if new lodging is more expensive than the cheapest known, a
     *   MoreExpensiveLodgingException is thrown; otherwise update the cheapest lodging.
     *
     * @pre service != null
     * @param service destination service
     * @throws DistractedStudentException if student becomes distracted after choosing an expensive eating service
     * @throws MoreExpensiveLodgingException if new lodging is more expensive than the cheapest known
     */
    @Override
    public void changeLocation(ServiceClass service) throws DistractedStudentException, MoreExpensiveLodgingException{
        if (service instanceof EatingServiceClass e) {
            moveTo(service);
            if (e.getPrice() > cheapestEatingPrice) {
                e.addStudent(this);
                throw new DistractedStudentException();
            }
            updateCheapestEating(e);
        } else if (service instanceof LeisureServiceClass) {
            moveTo(service);
        } else if (service instanceof LodgingServiceClass) {
            if (service.getPrice() > cheapestLodgingPrice) {
                throw new MoreExpensiveLodgingException();
            }
            updateCheapestLodging((LodgingServiceClass) service);
        }
    }

    /**
     * Update the cached cheapest eating price after a successful move to an eating service.
     *
     * @param e eating service instance
     */
    private void updateCheapestEating(EatingServiceClass e) {
        cheapestEatingPrice = e.getPrice();
    }

    /**
     * Update the cached cheapest lodging price.
     *
     * @param l lodging instance
     */
    private void updateCheapestLodging(LodgingServiceClass l) {
        this.cheapestLodgingPrice = l.getPrice();
    }
}