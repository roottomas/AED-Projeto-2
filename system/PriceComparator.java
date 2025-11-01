/**
 * @author Tomás Silvestre 68594 tm.silvestre@campus.fct.unl.pt
 * @author Ricardo Laur 68342 r.laur@campus.fct.unl.pt
 */
package system;
import dataStructures.Comparator;
import java.io.Serializable;

public class PriceComparator implements Comparator<ServiceClass>, Serializable {
    private static final long serialVersionUID = 0L;

    /**
     * Compare two services by their price.
     *
     * @pre newElem != null && ElemInList != null
     * @param newElem the element to insert / compare
     * @param ElemInList element already in the list
     * @return negative if newElem < ElemInList, positive if newElem > ElemInList, 1 if equal
     */
    @Override
    public int compare(ServiceClass newElem, ServiceClass ElemInList) {
        int cmp = Float.compare(newElem.getPrice(), ElemInList.getPrice());
        if (cmp != 0) {
            return cmp;
        }
        return 1;
    }
}