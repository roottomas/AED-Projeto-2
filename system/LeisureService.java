/**
 * @author Tomás Silvestre 68594 tm.silvestre@campus.fct.unl.pt
 * @author Ricardo Laur 68342 r.laur@campus.fct.unl.pt
 */
package system;
public interface LeisureService extends Service {

    /**
     * Return effective price after applying discount.
     *
     * @return discounted ticket price
     */
    float getPrice();
}