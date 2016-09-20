package com.management.repository;

import com.management.domain.Student;
import com.management.domain.exception.InvalidValueException;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TestStudentRepository {
    private StudentRepository studentRepository;

    @Before
    public void setUp() {
        List<Student> students = new ArrayList<>();
        students.add(new Student(1, "George"));
        students.add(new Student(2, "Marta"));
        studentRepository = new StudentRepository(students);
    }

    @Test
    public void student_retrieved_by_id() {
        assertEquals(new Student(1, "George"), studentRepository.getStudent(1));
    }

    @Test
    public void student_not_retrieved_by_invalid_id() {
        assertNull(studentRepository.getStudent(0));
    }

    @Test(expected = InvalidValueException.class)
    public void student_with_invalid_name_not_added() throws InvalidValueException {
        studentRepository.addStudent("George");
    }

    @Test
    public void student_with_valid_name_added() throws InvalidValueException {
        studentRepository.addStudent("Karla");

        List<Student> students = studentRepository.getAllStudents();
        assertEquals(3, students.size());
    }

    @Test
    public void id_of_newly_added_student_correct() throws InvalidValueException {
        studentRepository.addStudent("Karla");

        List<Student> students = studentRepository.getAllStudents();
        assertEquals(2, students.get(1).getId());
    }

    @Test
    public void id_retrieved_for_valid_name() throws InvalidValueException {
        int id = studentRepository.getStudentId("George");
        assertEquals(1, id);
    }

    @Test(expected = InvalidValueException.class)
    public void id_not_retrieved_for_invalid_student_name() throws InvalidValueException {
        studentRepository.getStudentId("InvalidName");
    }

    @Test
    public void student_with_valid_name_deleted() throws InvalidValueException {
        studentRepository.deleteStudent("George");

        assertEquals(1, studentRepository.getAllStudents().size());
    }

    @Test
    public void studentid_retrieved_for_deleted_student() throws InvalidValueException {
        int studentId = studentRepository.deleteStudent("George");

        assertEquals(1, studentId);
    }

    @Test(expected = InvalidValueException.class)
    public void exception_for_deleting_invalid_name_student() throws InvalidValueException {
        studentRepository.deleteStudent("InvalidName");
    }
}
