/**
 * @author Tomás Silvestre 68594 tm.silvestre@campus.fct.unl.pt
 * @author Ricardo Laur 68342 r.laur@campus.fct.unl.pt
 */
package system;
import dataStructures.*;
import java.io.Serializable;
import system.exceptions.*;

public abstract class StudentsStoringServiceClass extends ServiceClass implements StudentsStoringService, Serializable {
    private static final long serialVersionUID = 0L;

    private TwoWayList<StudentClass> students;

    /**
     * Construct a students-storing service.
     *
     * @param loc location
     * @param st service type
     * @param serviceName canonical name
     */
    public StudentsStoringServiceClass(LocationClass loc, ServiceType st, String serviceName) {
        super(loc, st, serviceName);
        students = new DoublyLinkedList<>();
    }

    /**
     * Get the two-way list containing students currently registered at this service.
     *
     * @return TwoWayList of students
     */
    public TwoWayList<StudentClass> getStudents() {
        return students;
    }

    /**
     * Append a student to the internal storage (helper).
     *
     * @param student student to store
     */
    protected void storeStudent(StudentClass student) {
        students.addLast(student);
    }

    /**
     * Remove an existing student from internal storage (helper).
     *
     * @param student student to remove
     */
    protected void removeStoredStudent(StudentClass student) {
        students.remove(students.indexOf(student));
    }

    /**
     * Add a student (concrete behaviour defined by subclasses).
     *
     * @pre student != null
     * @param student student to add
     */
    public abstract void addStudent(StudentClass student);

    /**
     * Remove a student (concrete behaviour defined by subclasses).
     *
     * @pre student != null
     * @param student student to remove
     */
    public abstract void removeStudent(StudentClass student);

    /**
     * Replace the internal students list (used during deserialization / reconstruction).
     *
     * @param newStudents new two-way list to store
     */
    public void setStudents(TwoWayList<StudentClass> newStudents) {
        this.students = newStudents;
    }
}