/**
 * @author Tomás Silvestre 68594 tm.silvestre@campus.fct.unl.pt
 * @author Ricardo Laur 68342 r.laur@campus.fct.unl.pt
 */
package system;
public interface EvaluationEntry {

    /**
     * Return the number of stars for this evaluation entry.
     *
     * @return integer number of stars (usually 1..5)
     */
    int getStars();

    /**
     * Return the textual description associated with this evaluation entry.
     *
     * @return description string
     */
    String getDescription();
}