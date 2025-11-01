/**
 * @author Tomás Silvestre 68594 tm.silvestre@campus.fct.unl.pt
 * @author Ricardo Laur 68342 r.laur@campus.fct.unl.pt
 */
package system.exceptions;
/**
 * Runtime exception thrown when attempting to move a student to the lodging
 * that is already their home.
 */
public class AlreadyAtHomeException extends RuntimeException {
    static final long serialVersionUID = 0L;
}
