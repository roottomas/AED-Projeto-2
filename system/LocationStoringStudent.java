/**
 * @author Tomás Silvestre 68594 tm.silvestre@campus.fct.unl.pt
 * @author Ricardo Laur 68342 r.laur@campus.fct.unl.pt
 */
package system;
import dataStructures.Iterator;

public interface LocationStoringStudent extends Student {

    /**
     * Return an iterator over services visited by this student.
     *
     * @return iterator of ServiceClass objects
     */
    Iterator<ServiceClass> listVisited();
}