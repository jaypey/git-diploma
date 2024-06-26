package com.diploma.git.backend;

import com.diploma.git.backend.mapper.StudentMapper;
import com.diploma.git.backend.model.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/student")
@CrossOrigin
public class StudentController {
    @Autowired
    StudentMapper studentMapper;

    @GetMapping("/getStudents")
    public List<Student> getStudents(@RequestParam(value = "id_project") int id_project) {
        Student s;
        List<Project> projects;
        List<Student>  students = studentMapper.getStudentsFromProject(id_project);
        for (int i = 0; i < students.size(); i++) {
            s = students.get(i);
            projects = studentMapper.getProjectsFromStudent(s.getCip());
            students.get(i).setProjets(projects);
        }
        return students;
    }

    @GetMapping("/getProjects")
    public List<Project> getProjects(@RequestParam(value = "cip") String cip) {
        List<Project> projects = studentMapper.getProjectsFromStudent(cip);
        for (int i = 0; i < projects.size(); i++) {
            projects.get(i).setStudents(getStudents(projects.get(i).getId_project()));
            projects.get(i).setCourses(getCoursesFromProject(projects.get(i).getId_project()));
        }
        return projects;
    }

    @GetMapping("/getCoursesFromProject")
    public List<Course> getCoursesFromProject(@RequestParam(value = "id_project") int id_project) {
        return studentMapper.getCoursesFromProject(id_project);
    }

    @GetMapping("/getCourses")
    public List<Course> getCourses(@RequestParam(value = "cip") String cip) {
        return studentMapper.getCoursesFromStudent(cip);
    }

    @GetMapping("/getTutors")
    public List<Tutor> getTutors(@RequestParam(value = "id_project") int id_project) {
        List<Tutor> tutors = studentMapper.getTutorsFromProject(id_project);
        for (int i = 0; i < tutors.size(); i++) {
            tutors.get(i).setCourseList(studentMapper.getCoursesFromTutor(tutors.get(i).getCip()));
        }
        return tutors;
    }

    @GetMapping("/getEvents")
    public List<Event> getEvents(@RequestParam(value = "id_project") int id_project) {
        return studentMapper.getEventFromProject(id_project);
    }

    @GetMapping("/getFiles")
    public List<File> getFiles(@RequestParam(value = "id_project") int id_project) {
        return studentMapper.getFilesFromProject(id_project);
    }
}
