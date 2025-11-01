/**
 * @author Tomás Silvestre 68594 tm.silvestre@campus.fct.unl.pt
 * @author Ricardo Laur 68342 r.laur@campus.fct.unl.pt
 */
package system;
public enum StudentType {
    bookish(0),
    outgoing(1),
    thrifty(2);

    private final int index;

    StudentType(int index) {
        this.index = index;
    }
}