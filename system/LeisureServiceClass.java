/**
 * @author Tomás Silvestre 68594 tm.silvestre@campus.fct.unl.pt
 * @author Ricardo Laur 68342 r.laur@campus.fct.unl.pt
 */
package system;
import java.io.Serializable;

public class LeisureServiceClass extends ServiceClass implements LeisureService, Serializable {
    private static final long serialVersionUID = 0L;

    private final float ticketPrice;
    private final float discount;

    /**
     * Construct a LeisureServiceClass.
     *
     * @pre ticketPrice > 0 && discount >= 0.0 && discount <= 1.0 && loc != null && st != null && serviceName != null
     * @param loc location of the service
     * @param ticketPrice base ticket price
     * @param discount discount ratio (0.0 - 1.0)
     * @param st service type (should be ServiceType.leisure)
     * @param serviceName canonical service name
     */
    public LeisureServiceClass(LocationClass loc, float ticketPrice, float discount, ServiceType st, String serviceName) {
        super(loc,st,serviceName);
        this.ticketPrice = ticketPrice;
        this.discount = discount;
    }

    /**
     * Return effective price after applying discount.
     *
     * @return discounted ticket price
     */
    @Override
    public float getPrice() {
        return ticketPrice - ticketPrice*discount;
    }
}