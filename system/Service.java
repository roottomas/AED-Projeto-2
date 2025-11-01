/**
 * @author Tomás Silvestre 68594 tm.silvestre@campus.fct.unl.pt
 * @author Ricardo Laur 68342 r.laur@campus.fct.unl.pt
 */
package system;
public interface Service {

    /**
     * Get the rounded evaluation average (1..5).
     *
     * @return rounded average
     */
    int getEvaluationAverage();

    /**
     * Add a new evaluation entry to the service.
     *
     * @param entry evaluation entry to add
     */
    void addEvaluation(EvaluationEntry entry);

    /**
     * Get the location object of the service.
     *
     * @return LocationClass representing coordinates
     */
    LocationClass getLocation();

    /**
     * Get the effective price of the service (meaning depends on service type).
     *
     * @return price value
     */
    float getPrice();

    /**
     * Get the service type enum.
     *
     * @return ServiceType
     */
    ServiceType getType();

    /**
     * Get the canonical name of the service.
     *
     * @return service name
     */
    String getName();

    /**
     * Get the latitude (first coordinate) of the service.
     *
     * @return latitude as double
     */
    double getLatitude();

    /**
     * Get the longitude (second coordinate) of the service.
     *
     * @return longitude as double
     */
    double getLongitude();

    /**
     * Get the internal EvaluationClass (full object with entries and timestamp).
     *
     * @return EvaluationClass instance
     */
    EvaluationClass getEvaluation();
}