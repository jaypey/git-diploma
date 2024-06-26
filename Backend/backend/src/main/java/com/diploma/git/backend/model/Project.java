package com.diploma.git.backend.model;

import java.util.List;

public class Project {
    private int id_project;
    private List<Student> students;
    List<Course> courses;
    private int max_member;

    public int getMax_member() {
        return max_member;
    }

    public void setMax_member(int max_member) {
        this.max_member = max_member;
    }

    public int getId_project() {
        return id_project;
    }

    public void setId_project(int id_project) {
        this.id_project = id_project;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }
}
