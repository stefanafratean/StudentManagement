package com.management.repository;

import com.management.domain.Student;
import com.management.domain.exception.InvalidValueException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class StudentRepository {
    private List<Student> students;

    public StudentRepository() {
        students = new ArrayList<>();
        loadStudents();
    }

    private void loadStudents() {
        File studentsFile = new File("students.dat");
        if (!studentsFile.exists()) {

            students.add(new Student(1, "George"));
            students.add(new Student(2, "Sarah"));
            saveToFile();
        } else {
            readStudentsFromFile(studentsFile);
        }
    }

    private void readStudentsFromFile(File studentsFile) {
        FileInputStream fis;
        try {
            fis = new FileInputStream(studentsFile);

            ObjectInputStream ois = new ObjectInputStream(fis);
            students = (List<Student>) ois.readObject();
            ois.close();
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }

    private void saveToFile() {
        try {
            File file = new File("students.dat");
            FileOutputStream fos;

            fos = new FileOutputStream(file);

            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(students);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    StudentRepository(List<Student> students) {
        this.students = students;
    }

    public Student getStudent(int id) {
        Optional<Student> optional = students.stream()
                .filter(s -> s.getId() == id)
                .findFirst();
        if (optional.isPresent()) {
            return optional.get();
        }
        return null;
    }

    public Optional<Student> getStudent(String name) {
        return students.stream()
                .filter(s -> s.getName().equals(name))
                .findFirst();
    }

    public List<Student> getAllStudents() {
        return students;
    }

    public void addStudent(String name) throws InvalidValueException {
        if (studentWithNameExists(name)) {
            throw new InvalidValueException("Error! A student with the name " + name + " already exists!");
        }
        int id = getFirstAvailableId();
        students.add(new Student(id, name));
        saveToFile();
    }

    private int getFirstAvailableId() {
        List<Integer> existingIds = students.stream()
                .map(Student::getId)
                .collect(Collectors.toList());

        int current = 1;
        while (existingIds.contains(current)) {
            current++;
        }

        return current;
    }

    private boolean studentWithNameExists(String name) {
        return getStudent(name).isPresent();
    }

    public int getStudentId(String name) throws InvalidValueException {
        Optional<Student> student = getStudent(name);
        if (student.isPresent()) {
            return student.get().getId();
        } else {
            throw new InvalidValueException("Error! Student with given name does not exist!");
        }
    }

    public int deleteStudent(String name) throws InvalidValueException {
        Optional<Student> toRemove = getStudent(name);
        if (!toRemove.isPresent()) {
            throw new InvalidValueException("Student with this name does not exist!");
        }
        Student studentToRemove = toRemove.get();
        students.remove(studentToRemove);
        saveToFile();

        return studentToRemove.getId();
    }
}
