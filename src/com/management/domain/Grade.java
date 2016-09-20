package com.management.domain;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "grade")
public class Grade {
    private int id;
    private int studentId;
    private String subject;
    private int gradeValue;

    public Grade() {

    }

    public Grade(int id, int studentId, String subject, int gradeValue) {
        this.id = id;
        this.studentId = studentId;
        this.subject = subject;
        this.gradeValue = gradeValue;
    }

    public String getSubject() {
        return subject;
    }

    public int getGradeValue() {
        return gradeValue;
    }

    public int getId() {
        return id;
    }

    public int getStudentId() {
        return studentId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (!(o instanceof Grade)) {
            return false;
        }
        Grade other = (Grade) o;
        return other.id == id && other.studentId == studentId && other.gradeValue == gradeValue && other.subject.equals(subject);
    }

    @XmlElement
    public void setId(int id) {
        this.id = id;
    }

    @XmlElement
    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    @XmlElement
    public void setSubject(String subject) {
        this.subject = subject;
    }

    @XmlElement
    public void setGradeValue(int gradeValue) {
        this.gradeValue = gradeValue;
    }
}
