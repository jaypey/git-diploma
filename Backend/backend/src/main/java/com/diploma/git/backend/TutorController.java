package com.diploma.git.backend;

import com.diploma.git.backend.mapper.StudentMapper;
import com.diploma.git.backend.mapper.TutorMapper;
import com.diploma.git.backend.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin
@RestController
@RequestMapping("/api/tutor")
public class TutorController {
    @Autowired
    private TutorMapper tutorMapper;

    @GetMapping("/getStudents")
    public List<Student> getStudents(@RequestParam(value = "id_project") int id_project) {
        List<Student>  students = tutorMapper.getStudentsFromProject(id_project);
        for (int i = 0; i < students.size(); i++) {
            students.get(i).setProjets(tutorMapper.getProjectsFromStudent(students.get(i).getCip()));
        }
        return students;
    }

    @GetMapping("/getCourses")
    public List<Course> getCourses(@RequestParam(value = "cip") String cip) {
        return tutorMapper.getCoursesFromTutor(cip);
    }

    @GetMapping("/getProjects")
    public List<Project> getProjects(@RequestParam(value = "sigle") String sigle) {
        return tutorMapper.getProjectsFromCourse(sigle);
    }

    @GetMapping("/getEvents")
    public List<Event> getEvents(@RequestParam(value = "id_project") int id_project) {
        return tutorMapper.getEventFromProject(id_project);
    }

    @GetMapping("/getSSH")
    public String getSSH(@RequestParam(value = "cip") String cip) {
        return tutorMapper.getSSHFromTutor(cip);
    }

    @GetMapping("/getFiles")
    public List<File> getFiles(@RequestParam(value = "id_project") int id_project) {
        return tutorMapper.getFilesFromProject(id_project);
    }

    @GetMapping("/getCourseEndDate")
    public String getCourseEndDate(@RequestParam(value = "sigle") String sigle) {
        return tutorMapper.getCourseEndDate(sigle);
    }

    @GetMapping()

    @GetMapping("/setEndDate")
    public boolean setEndDate(@RequestParam(value = "sigle") String sigle,
                              @RequestParam(value = "remise") String end_date) {
        if (!isInProject(sigle, ))
            return false;
        tutorMapper.setEndDate(id_project, cip, end_date);
        return true;
    }

}
