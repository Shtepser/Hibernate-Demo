package ru.ac.uniyar.databasescourse.entities;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Teacher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String degree;
    private String name;
    private String post;

    @OneToMany(mappedBy = "advisor")
    private Set<Student> students;

    public Teacher() {
        setStudents(new HashSet<>());
    }

    public Teacher(String name, String degree, String post) {
        this();
        this.setName(name);
        this.setDegree(degree);
        this.setPost(post);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public Set<Student> getStudents() {
        return students;
    }

    public void setStudents(Set<Student> students) {
        this.students = students;
    }

    public String toString() {
        return String.format("entities.Teacher[idTeacher=%d]", id);
    }

}
