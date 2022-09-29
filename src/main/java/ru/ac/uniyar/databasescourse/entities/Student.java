package ru.ac.uniyar.databasescourse.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String address;

    @Temporal(TemporalType.TIMESTAMP)
    private Date birthDate;
    private String phone;

    @ManyToOne
    private Teacher advisor;

    @ManyToMany
    @Fetch(FetchMode.SUBSELECT)
    private Set<Subject> subjects;

    public Student() {
        setSubjects(new HashSet<>());
    }

    public Student(String name, Date birthDate, String address, String phone, Teacher advisor) {
        this();
        this.setName(name);
        this.setBirthDate(birthDate);
        this.setAddress(address);
        this.setPhone(phone);
        this.setAdvisor(advisor);
    }

    public String toString() {
        return String.format("entities.Student[idStudent=%d]", id);
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getBirthDate() {
        return this.birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public Teacher getAdvisor() {
        return advisor;
    }

    public void setAdvisor(Teacher advisor) {
        this.advisor = advisor;
    }

    public Set<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(Set<Subject> subjects) {
        this.subjects = subjects;
    }

}
