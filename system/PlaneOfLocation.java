/**
 * @author Tomás Silvestre 68594 tm.silvestre@campus.fct.unl.pt
 * @author Ricardo Laur 68342 r.laur@campus.fct.unl.pt
 */
package system;
public interface PlaneOfLocation {

    /**
     * Check whether the supplied location is contained inside this plane/bounding box.
     *
     * @pre location != null
     * @param location location to test
     * @return true if location is inside bounds, false otherwise
     */
    boolean contains(Location location);
}