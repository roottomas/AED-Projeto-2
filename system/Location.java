/**
 * @author Tomás Silvestre 68594 tm.silvestre@campus.fct.unl.pt
 * @author Ricardo Laur 68342 r.laur@campus.fct.unl.pt
 */
package system;
public interface Location {

    /**
     * Return the raw coordinate array (latitude, longitude).
     *
     * @return array with two long values
     */
    long[] getLocation();

    /**
     * Compute a distance to another location (metric defined by implementation).
     *
     * @pre location != null
     * @param location other location
     * @return distance (double)
     */
    double distanceTo(Location location);
}