/**
 * @author Tomás Silvestre 68594 tm.silvestre@campus.fct.unl.pt
 * @author Ricardo Laur 68342 r.laur@campus.fct.unl.pt
 */
package system;
import java.io.Serializable;
import system.exceptions.*;

public abstract class StudentClass implements Student, Serializable {
    private static final long serialVersionUID = 0L;

    private final String name;
    private final String country;
    private LodgingServiceClass home;
    private ServiceClass location;
    private final StudentType type;

    /**
     * Construct a student with given parameters. The student initially is located at home.
     *
     * @param name student name
     * @param country student country
     * @param home student's lodging home
     * @param type student type
     */
    public StudentClass(String name, String country, LodgingServiceClass home, StudentType type) {
        this.name = name;
        this.country = country;
        this.home = home;
        this.location = home;
        this.type = type;
    }

    /**
     * Get the student type.
     *
     * @return StudentType
     */
    @Override
    public StudentType getType() {
        return type;
    }

    /**
     * Get student name.
     *
     * @return name string
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Get country of the student.
     *
     * @return country string
     */
    @Override
    public String getCountry() {
        return country;
    }

    /**
     * Get lodging home service.
     *
     * @return home lodging
     */
    @Override
    public LodgingServiceClass getHome() {
        return home;
    }

    /**
     * Get the student's current service/location.
     *
     * @return current location
     */
    @Override
    public ServiceClass getCurrentLocation() {
        return location;
    }

    /**
     * Get the student's current location.
     *
     * @return current location
     */
    @Override
    public LocationClass getCurrentCoordinatesLocation() {
        return location.getLocation();
    }

    /**
     * Internal helper to move the student to a new service. Validates that the
     * destination is a leisure or eating service and that, for eating services,
     * seats are available.
     *
     * @pre service instanceof LeisureService || service instanceof EatingService
     * @pre !(service instanceof EatingService) || ((EatingService)service).hasFreeSeats()
     * @param service destination service
     * @throws InvalidServiceException    if target is not leisure/eating
     * @throws EatingServiceFullException if eating service has no free seats
     */
    protected void moveTo(ServiceClass service) throws InvalidServiceException, EatingServiceFullException {
        if (!(service instanceof LeisureService || service instanceof EatingService)) {
            throw new InvalidServiceException();
        }

        if (service instanceof EatingService eatingService && !eatingService.hasFreeSeats()) {
            throw new EatingServiceFullException();
        }

        this.location = service;
    }

    /**
     * Internal helper to change the student's home. Validates free rooms
     * in target. Also moves current location to the new home and calls
     * changeLocation(home) (polymorphic behaviour).
     *
     * @pre newHome.hasFreeRooms()
     * @param newHome new lodging to become the student's home
     * @throws FullLodgingServiceException if there are no free rooms
     */
    protected void changeHome(LodgingServiceClass newHome) throws FullLodgingServiceException {
        if (!newHome.hasFreeRooms()) {
            throw new FullLodgingServiceException();
        }
        home = newHome;
        location = newHome;
        changeLocation(home);
    }

    /**
     * Abstract method implemented by concrete student types describing how
     * they behave when requested to change location.
     *
     * @param service destination service
     */
    public abstract void changeLocation(ServiceClass service);
}