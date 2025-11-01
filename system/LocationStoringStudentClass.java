/**
 * @author Tomás Silvestre 68594 tm.silvestre@campus.fct.unl.pt
 * @author Ricardo Laur 68342 r.laur@campus.fct.unl.pt
 */
package system;
import dataStructures.Iterator;
import dataStructures.ListInArray;

import java.io.Serializable;

public abstract class LocationStoringStudentClass extends StudentClass implements LocationStoringStudent, Serializable {
    private static final long serialVersionUID = 0L;

    private ListInArray<ServiceClass> visitedPlaces;

    /**
     * Construct a LocationStoringStudentClass with initial home and metadata.
     *
     * @pre name != null && country != null && home != null && type != null
     * @param name student name
     * @param country student country
     * @param home lodging home
     * @param type student type
     */
    public LocationStoringStudentClass(String name, String country, LodgingServiceClass home, StudentType type) {
        super(name, country, home, type);
        visitedPlaces = new ListInArray<>(50);
    }

    /**
     * Return an iterator over services visited by this student.
     *
     * @return iterator of ServiceClass objects
     */
    @Override
    public Iterator<ServiceClass> listVisited() {
        return visitedPlaces.iterator();
    }

    /**
     * Insert a service into the visited list if it is not already present.
     *
     * @pre service != null
     * @param service service to insert
     */
    protected void insertService(ServiceClass service) {
        if (visitedPlaces.indexOf(service) == -1) visitedPlaces.addLast(service);
    }
}