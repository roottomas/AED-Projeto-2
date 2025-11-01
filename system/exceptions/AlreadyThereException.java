/**
 * @author Tomás Silvestre 68594 tm.silvestre@campus.fct.unl.pt
 * @author Ricardo Laur 68342 r.laur@campus.fct.unl.pt
 */
package system.exceptions;
/**
 * Runtime exception thrown when a student attempts to move to a service
 * where they already are (no movement needed).
 */
public class AlreadyThereException extends RuntimeException {
    static final long serialVersionUID = 0L;
}
