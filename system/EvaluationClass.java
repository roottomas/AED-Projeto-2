/**
 * @author Tomás Silvestre 68594 tm.silvestre@campus.fct.unl.pt
 * @author Ricardo Laur 68342 r.laur@campus.fct.unl.pt
 */
package system;
import dataStructures.*;
import java.io.Serializable;

public class EvaluationClass implements Evaluation, Serializable {
    private static final long serialVersionUID = 0L;

    private List<EvaluationEntry> evaluations;
    private float average;
    private long timeStamp;
    /**
     * Create an EvaluationClass containing a default initial entry.
     * A default evaluation with 4 stars and empty description is added to avoid empty averages.
     */
    public EvaluationClass() {
        evaluations = new ListInArray<>(50);
        EvaluationEntry defaultEntry = new EvaluationEntryClass(4, "");
        evaluations.addLast(defaultEntry);
        this.updateAverage();
        timeStamp = System.currentTimeMillis();
    }

    /**
     * Add an evaluation entry and update the running average.
     *
     * @pre entry != null
     * @param entry new evaluation entry
     */
    @Override
    public void addEvaluationEntry(EvaluationEntry entry) {
        evaluations.addLast(entry);
        updateAverage();
    }

    /**
     * Recompute the average rating from stored entries.
     */
    private void updateAverage() {
        float sum = 0;
        int count = 0;

        for (int i = 0; i < evaluations.size(); i++) {
            sum += evaluations.get(i).getStars();
            count++;
        }
        timeStamp = System.currentTimeMillis() - timeStamp;
        average = sum / count;
    }

    /**
     * Return the computed average rating.
     *
     * @return average value
     */
    @Override
    public float getAverage() {
        return average;
    }

    /**
     * Returns true if any evaluation entry description contains the whole word `tag` (case-insensitive).
     *
     * @pre tag != null
     * @param tag search tag
     * @return true if at least one entry contains the tag
     */
    public boolean containsTag(String tag) {
        Predicate<EvaluationEntry> pred = e -> {
            String d = e.getDescription();
            return d.toLowerCase().matches(".*\\b" + tag.toLowerCase() + "\\b.*");
        };
        FilterIterator<EvaluationEntry> fit =
                new FilterIterator<>(evaluations.iterator(), pred);

        return fit.hasNext();
    }

}