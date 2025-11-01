/**
 * @author Tomás Silvestre 68594 tm.silvestre@campus.fct.unl.pt
 * @author Ricardo Laur 68342 r.laur@campus.fct.unl.pt
 */
package system;
import java.io.Serializable;

public class PlaneOfLocationClass implements PlaneOfLocation, Serializable {
    private static final long serialVersionUID = 0L;

    private final Location[] vertices;

    /**
     * Construct a rectangular plane using two opposite corner locations.
     *
     * @pre l1 != null && l2 != null
     * @param l1 one corner location
     * @param l2 opposite corner location
     */
    public PlaneOfLocationClass(Location l1, Location l2) {
        vertices = new Location[2];
        vertices[0] = l1;
        vertices[1] = l2;
    }

    /**
     * Test whether a location lies inside (inclusive) the rectangle defined by the two vertices.
     *
     * @pre location != null
     * @param location location to test
     * @return true if inside rectangle (inclusive), false otherwise
     */
    @Override
    public boolean contains(Location location) {
        long[] coords = location.getLocation();
        long xMin = Math.min(vertices[0].getLocation()[0], vertices[1].getLocation()[0]);
        long xMax = Math.max(vertices[0].getLocation()[0], vertices[1].getLocation()[0]);
        long yMin = Math.min(vertices[0].getLocation()[1], vertices[1].getLocation()[1]);
        long yMax = Math.max(vertices[0].getLocation()[1], vertices[1].getLocation()[1]);

        return (coords[0] >= xMin && coords[0] <= xMax)
                && (coords[1] >= yMin && coords[1] <= yMax);
    }
}