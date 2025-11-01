/**
 * @author Tomás Silvestre 68594 tm.silvestre@campus.fct.unl.pt
 * @author Ricardo Laur 68342 r.laur@campus.fct.unl.pt
 */
package system;
public interface ThriftyStudent extends Student {
    /**
     * Change location according to thrifty student's rules.
     *
     * @pre service != null
     * @param service destination
     */
    void changeLocation(ServiceClass service);
}