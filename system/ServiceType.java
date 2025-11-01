/**
 * @author Tomás Silvestre 68594 tm.silvestre@campus.fct.unl.pt
 * @author Ricardo Laur 68342 r.laur@campus.fct.unl.pt
 */
package system;

public enum ServiceType {
    eating(0),
    lodging(1),
    leisure(2);

    private final int index;

    ServiceType(int index) {
        this.index = index;
    }

    /**
     * Get the small integer index used to index arrays/tables for types.
     *
     * @return index (0..2)
     */
    public int getIndex() {
        return index;
    }
}