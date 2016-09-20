package com.management.service;

import com.management.domain.Grade;
import com.management.domain.Student;
import com.management.domain.exception.InvalidValueException;
import com.management.repository.GradeRepository;
import com.management.repository.StudentRepository;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Path("/StudentManagement")
public class StudentManagementService {
    static final String STUDENT_ADDED = "Student successfully added!";
    static final String GRADE_ADDED = "Grade successfully added!";
    static final String STUDENT_DELETED = "Student successfully deleted!";
    private StudentRepository studentRepository;
    private GradeRepository gradeRepository = new GradeRepository();

    public StudentManagementService() {
        this.studentRepository = new StudentRepository();
        this.gradeRepository = new GradeRepository();
    }

    @GET
    @Path("/students")
    @Produces(MediaType.APPLICATION_XML)
    public List<Student> getStudents() {
        return studentRepository.getAllStudents();
    }

    @GET
    @Path("/students/{studentid}")
    @Produces(MediaType.APPLICATION_XML)
    public Student getStudents(@PathParam("studentid") int studentId) {
        return studentRepository.getStudent(studentId);
    }


    @GET
    @Path("/grades/{studentid}")
    @Produces(MediaType.APPLICATION_XML)
    public List<Grade> getGrades(@PathParam("studentid") int studentId) {
        return gradeRepository.getAllGrades(studentId);
    }

    @GET
    @Path("/students/order")
    @Produces(MediaType.APPLICATION_XML)
    public List<Student> getStudentsSortedDescendingByAverageGrade() {
        List<Student> students = studentRepository.getAllStudents();
        Comparator<Student> averageGradeComparator = (Student s1, Student s2) -> -1 * gradeRepository.getAverageGrade(s1.getId())
                .compareTo(gradeRepository.getAverageGrade(s2.getId()));

        return students.stream()
                .sorted(averageGradeComparator)
                .collect(Collectors.toList());
    }

    @GET
    @Path("/grades/{subject}/{grade}")
    @Produces(MediaType.APPLICATION_XML)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public List<Student> getStudentsThatHaveTopic(@PathParam("subject") String subject, @PathParam("grade") int grade) {
        List<Integer> studentIds = gradeRepository.getStudentIds(subject, grade);
        List<Student> students = studentRepository.getAllStudents();
        return students.stream()
                .filter(s -> studentIds.contains(s.getId()))
                .collect(Collectors.toList());
    }

    @PUT
    @Path("/students")
    @Produces(MediaType.APPLICATION_XML)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public String addStudent(@FormParam("name") String name) {
        try {
            studentRepository.addStudent(name);
            return STUDENT_ADDED;
        } catch (InvalidValueException e) {
            return e.getMessage();
        }
    }

    @PUT
    @Path("/grades")
    @Produces(MediaType.APPLICATION_XML)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public String addGrade(@FormParam("studentname") String studentName, @FormParam("subject") String subject, @FormParam("gradevalue") int gradeValue) {
        int studentId;
        try {
            studentId = studentRepository.getStudentId(studentName);
        } catch (InvalidValueException e) {
            return e.getMessage();
        }
        gradeRepository.addGrade(studentId, subject, gradeValue);
        return GRADE_ADDED;
    }

    @DELETE
    @Path("/students/{studentname}")
    @Produces(MediaType.APPLICATION_XML)
    public String deleteStudent(@PathParam("studentname") String studentName) {
        try {
            int id = studentRepository.deleteStudent(studentName);
            gradeRepository.deleteGrades(id);
        } catch (InvalidValueException e) {
            return e.getMessage();
        }
        return STUDENT_DELETED;
    }
}
