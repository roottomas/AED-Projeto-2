/**
 * @author Tomás Silvestre 68594 tm.silvestre@campus.fct.unl.pt
 * @author Ricardo Laur 68342 r.laur@campus.fct.unl.pt
 */
package system;
import java.io.Serializable;

public abstract class ServiceClass implements Service, Serializable {
    private static final long serialVersionUID = 0L;
    private final LocationClass serviceLocation;
    private EvaluationClass serviceEvaluation;
    private final ServiceType serviceType;
    private final String serviceName;

    /**
     * Construct a ServiceClass with basic metadata.
     *
     * @pre loc != null && st != null && serviceName != null
     * @param loc location of the service
     * @param st service type
     * @param serviceName canonical name
     */
    public ServiceClass(LocationClass loc, ServiceType st, String serviceName) {
        this.serviceLocation = loc;
        serviceType = st;
        serviceEvaluation = new EvaluationClass();
        this.serviceName = serviceName;
    }

    /**
     * Get service name.
     *
     * @return name
     */
    @Override
    public String getName() {
        return serviceName;
    }

    /**
     * Get the EvaluationClass for this service.
     *
     * @return evaluation object
     */
    @Override
    public EvaluationClass getEvaluation() {
        return serviceEvaluation;
    }

    /**
     * Get the service type.
     *
     * @return ServiceType enum
     */
    @Override
    public ServiceType getType() {
        return serviceType;
    }

    /**
     * Get rounded evaluation average (1..5).
     *
     * @return rounded average
     */
    @Override
    public int getEvaluationAverage() {
        return Math.round(serviceEvaluation.getAverage());
    }

    /**
     * Add an evaluation entry to the internal evaluation structure.
     *
     * @param entry evaluation entry to add
     */
    @Override
    public void addEvaluation(EvaluationEntry entry) {
        serviceEvaluation.addEvaluationEntry(entry);
    }

    /**
     * Get the stored LocationClass instance.
     *
     * @return location
     */
    @Override
    public LocationClass getLocation() {
        return serviceLocation;
    }

    /**
     * Shortcut to get latitude coordinate.
     *
     * @return latitude
     */
    @Override
    public double getLatitude() {
        return serviceLocation.getLocation()[0];
    }

    /**
     * Shortcut to get longitude coordinate.
     *
     * @return longitude
     */
    @Override
    public double getLongitude() {
        return serviceLocation.getLocation()[1];
    }

    /**
     * Concrete subclasses must return the applicable price for that type.
     *
     * @return price value
     */
    @Override
    public abstract float getPrice();
}