/**
 * @author Tomás Silvestre 68594 tm.silvestre@campus.fct.unl.pt
 * @author Ricardo Laur 68342 r.laur@campus.fct.unl.pt
 */
package system;
import java.io.Serializable;

public class EvaluationEntryClass implements EvaluationEntry, Serializable {
    private static final long serialVersionUID = 0L;

    private final int stars;
    private final String description;

    /**
     * Construct a new evaluation entry.
     *
     * @param stars number of stars (1..5)
     * @param description text describing the evaluation
     * @pre stars >= 1 && stars <= 5 && description != null
     */
    public EvaluationEntryClass(int stars, String description) {
        this.stars = stars;
        this.description = description;
    }

    /**
     * Return the number of stars for this evaluation entry.
     *
     * @return integer number of stars (usually 1..5)
     */
    @Override
    public int getStars() {
        return stars;
    }

    /**
     * Return the textual description associated with this evaluation entry.
     *
     * @return description string
     */
    @Override
    public String getDescription() {
        return description;
    }
}