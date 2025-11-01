/**
 * @author Tomás Silvestre 68594 tm.silvestre@campus.fct.unl.pt
 * @author Ricardo Laur 68342 r.laur@campus.fct.unl.pt
 */
package system;
import java.io.Serializable;

public class LocationClass implements Location, Serializable {
    private static final long serialVersionUID = 0L;

    private final long[] coordinates;

    /**
     * Construct a LocationClass with given latitude and longitude values.
     * @param lat  latitude value
     * @param longi longitude value
     */
    public LocationClass(long lat, long longi) {
        coordinates = new long[2];
        coordinates[0] = lat;
        coordinates[1] = longi;
    }

    /**
     * Return the raw coordinate array (latitude, longitude).
     *
     * @return array with two long values
     */
    @Override
    public long[] getLocation() {
        return coordinates;
    }

    /**
     * Compute Manhattan distance to another Location.
     *
     * @pre location != null
     * @param location other location
     * @return Manhattan distance (absolute coordinate differences summed)
     */
    @Override
    public double distanceTo(Location location) {
        long[] coords = location.getLocation();
        return Math.abs(coords[0] - coordinates[0]) + Math.abs(coords[1] - coordinates[1]);
    }
}