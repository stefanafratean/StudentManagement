package com.management.service;

import com.management.domain.Grade;
import com.management.domain.Student;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Client used for testing the server responses.
 * The server needs to be manually started.
 */
public class TestClient {
    public static final String STUDENTS_PATH = "/students";
    public static final String GRADES_PATH = "/grades";
    private Client client;
    private String BASE_URL = "http://localhost:9999/StudentManagement";
    private static final String SUCCESS_RESULT = "<result>success</result>";

    @Before
    public void init() throws IOException {
        this.client = ClientBuilder.newClient();
    }

    @Test
    public void all_students_retrieved() {
        GenericType<List<Student>> list = new GenericType<List<Student>>() {
        };
        List<Student> students = client.target(BASE_URL).path(STUDENTS_PATH).request(MediaType.APPLICATION_XML).get(list);
        assertFalse(students.isEmpty());
    }

    @Test
    public void student_with_id_is_retrieved() {
        Student student = client.target(BASE_URL).path(STUDENTS_PATH + "/{studentid}").resolveTemplate("studentid", 1).request(MediaType.APPLICATION_XML).get(Student.class);
        assertEquals(1, student.getId());
    }

    @Test
    public void grades_retrieved_for_student() {
        GenericType<List<Grade>> list = new GenericType<List<Grade>>() {
        };
        List<Grade> grades = client.target(BASE_URL).path(GRADES_PATH + "/{studentid}").resolveTemplate("studentid", 1).request(MediaType.APPLICATION_XML).get(list);
        assertFalse(grades.isEmpty());
    }

    @Test
    public void students_sorted_by_descending_average_grade() {
        GenericType<List<Student>> list = new GenericType<List<Student>>() {
        };
        List<Student> students = client.target(BASE_URL).path(STUDENTS_PATH + "/order").request(MediaType.APPLICATION_XML).get(list);
        assertFalse(students.isEmpty());
    }

    @Test
    public void students_retrieved_for_subject() {
        GenericType<List<Student>> list = new GenericType<List<Student>>() {
        };
        List<Student> students = client.target(BASE_URL).path(GRADES_PATH + "/{subject}/{grade}").resolveTemplate("subject", "Literature").resolveTemplate("grade", 9).request(MediaType.APPLICATION_XML).get(list);
        assertFalse(students.isEmpty());
    }

    @Test
    public void student_with_valid_name_added() {
        String callResult = addStudent("Albert");

        Assert.assertEquals(StudentManagementService.STUDENT_ADDED, callResult);
    }

    private String addStudent(String name) {
        Form form = new Form();
        form.param("name", name);

        return client.target(BASE_URL).path(STUDENTS_PATH).request(MediaType.APPLICATION_XML).put(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE), String.class);
    }

    @Test
    public void student_with_duplicate_name_not_added() {
        String callResult = addStudent("George");

        assertNotEquals(StudentManagementService.STUDENT_ADDED, callResult);
    }

    @Test
    public void grade_added_for_valid_student_name() {
        String callResult = addGrade("George");

        assertEquals(StudentManagementService.GRADE_ADDED, callResult);
    }

    @Test
    public void grade_not_added_for_invalid_student_name() {
        String callResult = addGrade("InvalidStudentName");

        assertNotEquals(StudentManagementService.GRADE_ADDED, callResult);
    }

    private String addGrade(String studentName) {
        Form form = new Form();
        form.param("studentname", studentName);
        form.param("subject", "Literature");
        form.param("gradevalue", "5");

        return client.target(BASE_URL).path(GRADES_PATH).request(MediaType.APPLICATION_XML).put(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE), String.class);
    }

    @Test
    public void student_with_valid_name_deleted() {
        String callResult = client
                .target(BASE_URL)
                .path(STUDENTS_PATH + "/{studentname}")
                .resolveTemplate("studentname", "George")
                .request(MediaType.APPLICATION_XML)
                .delete(String.class);

        assertEquals(StudentManagementService.STUDENT_DELETED, callResult);
    }

    @Test
    public void student_with_invalid_name_not_deleted() {
        String callResult = client
                .target(BASE_URL)
                .path(STUDENTS_PATH + "/{studentname}")
                .resolveTemplate("studentname", "InvalidName")
                .request(MediaType.APPLICATION_XML)
                .delete(String.class);

        assertNotEquals(StudentManagementService.STUDENT_DELETED, callResult);
    }
}
