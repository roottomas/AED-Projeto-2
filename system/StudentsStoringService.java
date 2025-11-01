/**
 * @author Tomás Silvestre 68594 tm.silvestre@campus.fct.unl.pt
 * @autho
 * r Ricardo Laur 68342 r.laur@campus.fct.unl.pt
 */
package system;
import dataStructures.TwoWayList;

public interface StudentsStoringService extends Service {
    /**
     * Add a student to the service internal registry.
     *
     * @pre student != null
     * @param student student to add
     */
    void addStudent(StudentClass student);

    /**
     * Remove a student from the service internal registry.
     *
     * @pre student != null
     * @param student student to remove
     */
    void removeStudent(StudentClass student);

    /**
     * Return the internal two-way list holding the students currently at this service.
     *
     * @return TwoWayList of StudentClass
     */
    TwoWayList<StudentClass> getStudents();

    /**
     * Replace the internal students list (used during deserialization / reconstruction).
     *
     * @param newStudents new TwoWayList to set as internal storage
     */
    void setStudents(TwoWayList<StudentClass> newStudents);
}