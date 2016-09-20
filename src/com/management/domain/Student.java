package com.management.domain;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement(name = "student")
public class Student implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String name;

    public Student(){

    }

    public Student(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @XmlElement
    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    @XmlElement
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null){
            return false;
        }
        if (!(o instanceof Student)) {
            return false;
        }
        Student other = (Student) o;
        return other.getId() == id && other.getName().equals(name);
    }
}
