/**
 * @author Tomás Silvestre 68594 tm.silvestre@campus.fct.unl.pt
 * @author Ricardo Laur 68342 r.laur@campus.fct.unl.pt
 */
package system;
import java.io.Serializable;


public class EatingServiceClass extends StudentsStoringServiceClass implements EatingService, Serializable {
    private static final long serialVersionUID = 0L;

    private final float menuPrice;
    private final int numSeats;
    private int occupiedSeats;

    /**
     * Construct a new EatingServiceClass.
     *
     * @pre loc != null && st != null && serviceName != null
     * @param loc service location
     * @param price menu price
     * @param numSeats total number of seats
     * @param st service type (ServiceType.eating)
     * @param serviceName canonical service name
     */
    public EatingServiceClass(LocationClass loc, float price, int numSeats, ServiceType st, String serviceName) {
        super(loc, st, serviceName);
        this.menuPrice = price;
        this.numSeats = numSeats;
        occupiedSeats = 0;
    }

    /**
     * Returns whether there are free seats available.
     *
     * @return true iff occupiedSeats < numSeats
     */
    @Override
    public boolean hasFreeSeats() {
        return occupiedSeats < numSeats;
    }

    /**
     * Return the effective price used by price-sorted structures.
     *
     * @return menu price
     */
    @Override
    public float getPrice() {
        return menuPrice;
    }

    /**
     * Add a student to the service (occupies a seat and stores the student).
     *
     * @pre student != null
     * @param student student to add
     */
    @Override
    public void addStudent(StudentClass student) {
        storeStudent(student);
        occupiedSeats++;
    }

    /**
     * Remove a student from the service (frees a seat and removes the student).
     *
     * @pre student != null
     * @param student student to remove
     */
    @Override
    public void removeStudent(StudentClass student) {
        removeStoredStudent(student);
        occupiedSeats--;
    }
}