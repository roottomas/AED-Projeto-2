/**
 * @author Tomás Silvestre 68594 tm.silvestre@campus.fct.unl.pt
 * @author Ricardo Laur 68342 r.laur@campus.fct.unl.pt
 */
package system;
public interface Evaluation {

    /**
     * Add an evaluation entry (stars + description).
     *
     * @pre entry != null
     * @param entry evaluation entry
     */
    void addEvaluationEntry(EvaluationEntry entry);

    /**
     * Return the average evaluation (as a float).
     *
     * @return average rating value
     */
    float getAverage();

    /**
     * Return whether any evaluation entry description contains the word/tag (word boundaries).
     *
     * @pre tag != null
     * @param tag search tag
     * @return true if any entry contains tag
     */
    boolean containsTag(String tag);
}