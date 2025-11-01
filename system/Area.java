/**
 * @author Tomás Silvestre 68594 tm.silvestre@campus.fct.unl.pt
 * @author Ricardo Laur 68342 r.laur@campus.fct.unl.pt
 */
package system;
import dataStructures.Iterator;

/**
 * Public interface describing the operations supported by the Area.
 * The Area manages services and students inside a bounded plane and provides
 * listing, searching and modification methods used by the application.
 */
public interface Area {

    /**
     * Add a new service inside the area boundary.
     *
     * @pre name != null
     * @param type      service type (eating, lodging, leisure)
     * @param latitude  service latitude
     * @param longitude service longitude
     * @param price     service price parameter (meaning depends on type)
     * @param value     service value parameter (capacity, discount, etc.)
     * @param name      service name (may contain spaces)
     */
    void addService(ServiceType type, long latitude, long longitude, float price, float value, String name);

    /**
     * List services in insertion order.
     *
     * @return iterator over services by insertion order
     */
    Iterator<ServiceClass> listServicesByInsertion();

    /**
     * List services ordered by evaluation average (descending).
     *
     * @return iterator over services by evaluation ranking
     */
    Iterator<ServiceClass> listServicesByEvaluation();

    /**
     * Find services of a given textual type (eating|lodging|leisure) that have
     * a particular rounded stars rating and are closest to the given student.
     *
     * @param type textual type
     * @param stars evaluation stars (1..5)
     * @param s student name
     * @return iterator of the closest services that match the criteria
     */
    Iterator<ServiceClass> findServicesByRating(String type, int stars, String s);

    /**
     * Find the most relevant service (depending on student's type and service type).
     *
     * @param s student name
     * @param type textual service type
     * @return the selected ServiceClass
     */
    ServiceClass findRelevantServiceForStudent(String s, String type);

    /**
     * Add a new student to the area.
     *
     * @param type student behaviour type
     * @param name student name
     * @param country student country
     * @param lodgingName home lodging name
     */
    void addStudent(StudentType type, String name, String country, String lodgingName);

    /**
     * List students coming from a given country.
     *
     * @pre country != null
     * @param country country name (case-insensitive)
     * @return iterator over matching students
     */
    Iterator<StudentClass> listStudentsByCountry(String country);

    /**
     * List all registered students alphabetically.
     *
     * @return iterator of students ordered alphabetically
     */
    Iterator<StudentClass> listAllStudentsAlphabetically();

    /**
     * Make a student go to a service (eating or leisure).
     *
     * @param studentName student name
     * @param locationName service name
     */
    void goToLocation(String studentName, String locationName);

    /**
     * Change a student's home to another lodging service.
     *
     * @param studentName student name
     * @param lodgingName target lodging name
     */
    void moveStudentHome(String studentName, String lodgingName);

    /**
     * Remove a student from the area (they leave the system).
     *
     * @param name student name
     */
    void removeStudent(String name);

    /**
     * List locations visited by a student (for bookish/outgoing students).
     *
     * @param studentName student name
     * @return iterator of visited ServiceClass objects
     */
    Iterator<ServiceClass> listVisited(String studentName);

    /**
     * Add an evaluation (stars + description) to a service.
     *
     * @pre name != null && country != null && lodgingName != null
     * @param stars stars 1..5
     * @param serviceName service name
     * @param description description text
     */
    void starService(int stars, String serviceName, String description);

    /**
     * Get area name.
     *
     * @return the area name
     */
    String getName();

    /* Convenience getters for service/student attributes */

    String getServiceName(ServiceClass service);

    String getServiceType(ServiceClass service);

    double getServiceLatitude(ServiceClass service);

    double getServiceLongitude(ServiceClass service);

    String getStudentType(StudentClass s);

    String getStudentLocationName(StudentClass s);

    String getStudentName(StudentClass s);

    /**
     * Return the canonical (cased) service name if it exists, otherwise null.
     *
     * @param name service name lookup (case-insensitive)
     * @return canonical service name or null
     */
    String getServiceCasedName(String name);

    /**
     * Return the canonical (cased) student name if it exists, otherwise null.
     *
     * @param name student name lookup (case-insensitive)
     * @return canonical student name or null
     */
    String getStudentCasedName(String name);

    /**
     * List students in a service either forward ('>') or reverse ('<') order.
     *
     * @param order '>' or '<'
     * @param serviceName service name
     * @return iterator (TwoWayIterator when '<' is used)
     */
    Iterator<StudentClass> listStudentsInService(char order, String serviceName);

    /* --- Student location lookups by student name --- */

    String getStudentLocationNameByName(String studentName);

    String getStudentLocationTypeByName(String studentName);

    long getStudentLocationLatitudeByName(String studentName);

    long getStudentLocationLongitudeByName(String studentName);

    int getServiceAverage(ServiceClass service);

    /**
     * Return an iterator of services that contain the given tag in their evaluations.
     *
     * @pre tag != null
     * @param tag tag to search for
     * @return iterator of services with the tag
     */
    Iterator<ServiceClass> getServicesWithTag(String tag);
}
