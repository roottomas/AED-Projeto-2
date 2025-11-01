/**
 * @author Tomás Silvestre 68594 tm.silvestre@campus.fct.unl.pt
 * @author Ricardo Laur 68342 r.laur@campus.fct.unl.pt
 */
package system;
import system.exceptions.*;
import dataStructures.Comparator;
import java.io.Serializable;

/**
 * Comparator that orders StudentClass instances by name (case-insensitive).
 * This comparator is used for alphabetical ordering of students.
 */
public class AlphOrderComparator implements Comparator<StudentClass>, Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * @pre s1 != null && s2 != null
     *
     * Compare two students by their name ignoring case.
     *
     * @param s1 first student
     * @param s2 second student
     * @return a negative integer, zero, or a positive integer as the
     *         first argument is less than, equal to, or greater than
     *         the second, lexicographically ignoring case.
     */
    @Override
    public int compare(StudentClass s1, StudentClass s2) {
        return s1.getName().compareToIgnoreCase(s2.getName());
    }
}