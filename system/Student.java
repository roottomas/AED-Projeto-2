/**
 * @author Tomás Silvestre 68594 tm.silvestre@campus.fct.unl.pt
 * @author Ricardo Laur 68342 r.laur@campus.fct.unl.pt
 */
package system;
public interface Student {

    /**
     * Return the current location of the student.
     *
     * @return ServiceClass current location
     */
    ServiceClass getCurrentLocation();

    /**
     * Get the student's current location.
     *
     * @return current location
     */
    LocationClass getCurrentCoordinatesLocation();

    /**
     * Get student name.
     *
     * @return name string
     */
    String getName();

    /**
     * Get country of origin.
     *
     * @return country string
     */
    String getCountry();

    /**
     * Get the lodging service that represents the student's home.
     *
     * @return LodgingServiceClass home
     */
    LodgingServiceClass getHome();

    /**
     * Get the student behaviour type.
     *
     * @return StudentType enum
     */
    StudentType getType();

    /**
     * Ask student to change location to the specified service.
     *
     * @param service destination
     */
    void changeLocation(ServiceClass service);
}