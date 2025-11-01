/**
 * @author Tomás Silvestre 68594 tm.silvestre@campus.fct.unl.pt
 * @author Ricardo Laur 68342 r.laur@campus.fct.unl.pt
 */
package system;
import java.io.Serializable;

public class LodgingServiceClass extends StudentsStoringServiceClass implements LodgingService, Serializable {
    private static final long serialVersionUID = 0L;

    private final float monthlyCost;
    private final int singleRoomsNum;
    private int occupiedRoomsNum;

    /**
     * Construct a LodgingServiceClass.
     *
     * @pre loc != null && st != null && serviceName != null && singleRoomsNum > 0 && price > 0
     * @param loc location
     * @param price monthly cost
     * @param singleRoomsNum number of single rooms available
     * @param st service type (ServiceType.lodging)
     * @param serviceName canonical service name
     */
    public LodgingServiceClass(LocationClass loc, float price, int singleRoomsNum, ServiceType st, String serviceName) {
        super(loc, st, serviceName);
        monthlyCost = price;
        this.singleRoomsNum = singleRoomsNum;
        occupiedRoomsNum = 0;
    }

    /**
     * Check whether the lodging still has free rooms.
     *
     * @return true iff there are free rooms, false otherwise.
     */
    @Override
    public boolean hasFreeRooms() {
        return occupiedRoomsNum < singleRoomsNum;
    }

    /**
     * Returns the lodging service's monthly price.
     *
     * @return monthlyCost.
     */
    @Override
    public float getPrice() {
        return monthlyCost;
    }

    /**
     * Add a student (occupy a room).
     *
     * @pre student != null
     * @param student student to add
     */
    @Override
    public void addStudent(StudentClass student) {
        storeStudent(student);
        occupiedRoomsNum++;
    }

    /**
     * Remove a student (free a room).
     *
     * @pre student != null
     * @param student student to remove
     */
    @Override
    public void removeStudent(StudentClass student) {
        removeStoredStudent(student);
        occupiedRoomsNum--;
    }
}