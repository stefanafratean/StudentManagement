package com.management.repository;

import com.management.domain.Grade;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.assertEquals;

public class TestGradeRepository {
    private GradeRepository gradeRepository;

    @Before
    public void setUp() {
        List<Grade> grades = new ArrayList<>();
        grades.add(new Grade(1, 1, "Math", 10));
        grades.add(new Grade(2, 1, "Literature", 7));
        grades.add(new Grade(3, 2, "Math", 5));

        gradeRepository = new GradeRepository(grades);
    }

    @Test
    public void grades_correctly_retrieved_for_studentid() {
        List<Grade> retrievedGrades = gradeRepository.getAllGrades(1);

        assertEquals(2, retrievedGrades.size());
        assertEquals(new Grade(1,1,"Math", 10), retrievedGrades.get(0));
        assertEquals(new Grade(2,1,"Literature", 7), retrievedGrades.get(1));
    }

    @Test
    public void empty_grade_list_retrieved_for_invalid_studentid(){
        List<Grade> retrievedGrades = gradeRepository.getAllGrades(0);

        assertTrue(retrievedGrades.isEmpty());
    }

    @Test
    public void average_grade_computed_for_valid_studentid(){
        assertEquals(8.5, gradeRepository.getAverageGrade(1));
    }

    @Test
    public void average_grade_zero_for_invalid_studentid(){
        assertEquals(0.0, gradeRepository.getAverageGrade(0));
    }

    @Test
    public void student_ids_retrieved_for_subject(){
        List<Integer> retrievedIds = gradeRepository.getStudentIds("Math", 5);

        assertEquals(Arrays.asList(2), retrievedIds);
    }

    @Test
    public void grade_added(){
        gradeRepository.addGrade(1, "Literature", 5);

        assertEquals(4, gradeRepository.getAllGrades().size());
    }

    @Test
    public void id_of_newly_added_grade_correct(){
        gradeRepository.addGrade(1, "Literature", 5);

        List<Grade> grades = gradeRepository.getAllGrades();
        assertEquals(4, grades.get(3).getId());
    }

    @Test
    public void grades_for_studentid_deleted(){
        gradeRepository.deleteGrades(1);

        assertEquals(1, gradeRepository.getAllGrades().size());
    }
}
