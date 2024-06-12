package com.diploma.git.backend.model;

import java.util.List;

public class Project {
    private int id_project;
    private List<Student> students;
    private int max_member;

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
}
