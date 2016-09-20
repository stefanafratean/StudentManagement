package com.management.repository;

import com.management.domain.Grade;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

public class GradeRepository {
    private List<Grade> grades;

    public GradeRepository() {
        grades = new ArrayList<>();
        loadGrades();
    }

    private void loadGrades() {
        grades.add(new Grade(1, 1, "Math", 10));
        grades.add(new Grade(2, 1, "Literature", 5));
        grades.add(new Grade(3, 2, "Literature", 9));

        File gradesFile = new File("grades.dat");
        if (!gradesFile.exists()) {

            grades.add(new Grade(1, 1, "Math", 10));
            grades.add(new Grade(2, 1, "Literature", 5));
            grades.add(new Grade(3, 2, "Literature", 9));
            saveToFile();
        } else {
            readGradesFromFile(gradesFile);
        }
    }

    private void readGradesFromFile(File gradesFile) {
        FileInputStream fis;
        try {
            fis = new FileInputStream(gradesFile);

            ObjectInputStream ois = new ObjectInputStream(fis);
            grades = (List<Grade>) ois.readObject();
            ois.close();
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }

    private void saveToFile() {
        try {
            File file = new File("grades.dat");
            FileOutputStream fos;

            fos = new FileOutputStream(file);

            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(grades);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    GradeRepository(List<Grade> grades) {
        this.grades = grades;
    }

    public List<Grade> getAllGrades(int studentId) {
        return grades.stream()
                .filter(g -> g.getStudentId() == studentId)
                .collect(Collectors.toList());
    }

    public Double getAverageGrade(int studentId) {
        OptionalDouble grade = getAllGrades(studentId).stream()
                .mapToInt(Grade::getGradeValue)
                .average();
        if (grade.isPresent()) {
            return grade.getAsDouble();
        }

        return Double.valueOf(0);
    }

    public List<Integer> getStudentIds(String subject, int grade) {
        return grades.stream()
                .filter(s -> s.getSubject().equals(subject) && s.getGradeValue() == grade)
                .map(Grade::getStudentId)
                .distinct()
                .collect(Collectors.toList());
    }

    public void addGrade(int studentId, String subject, int gradeValue) {
        int id = getFirstAvailableId();
        grades.add(new Grade(id, studentId, subject, gradeValue));
        saveToFile();
    }

    private int getFirstAvailableId() {
        List<Integer> existingIds = grades.stream()
                .map(Grade::getId)
                .collect(Collectors.toList());

        int current = 1;
        while (existingIds.contains(current)) {
            current++;
        }

        return current;
    }

    List<Grade> getAllGrades() {
        return grades;
    }

    public void deleteGrades(int studentId) {
        grades.removeIf(g -> g.getStudentId() == studentId);
        saveToFile();
    }
}
