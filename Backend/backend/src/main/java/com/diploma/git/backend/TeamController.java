package com.diploma.git.backend;

import com.diploma.git.backend.mapper.TeamMapper;
import com.diploma.git.backend.model.Project;
import com.diploma.git.backend.model.Student;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/team")
@CrossOrigin
public class TeamController {
    @Autowired
    TeamMapper teamMapper;

    @GetMapping("/getProjects")
    public List<Project> getProjects(@RequestParam(value = "sigle") String sigle) {
        List<Project> teams = teamMapper.getProjectsFromCourse(sigle);
        for(int i = 0; i < teams.size(); i++) {
            teams.get(i).setStudents(teamMapper.getStudentsFromProject(teams.get(i).getId_project()));
            teams.get(i).setMax_member(teamMapper.getTeamSizeFromCourse(sigle));
        }
        return teams;
    }

    @GetMapping("/registerInProject")
    public boolean registerInProject(@RequestParam(value = "sigle") String sigle,
                                  @RequestParam(value = "id_project") int id_project,
                                  @RequestParam(value = "cip") String cip) {
        if (isInProject(sigle, cip))
            return false;
        teamMapper.registerStudentInProject(id_project, cip);
        return true;
    }

    @GetMapping("/isInProject")
    public boolean isInProject(@RequestParam(value = "sigle") String sigle,
                               @RequestParam(value = "cip") String cip) {
        List<Project> teams = getProjects(sigle);
        for (int i = 0; i < teams.size(); i++) {
            List<Student> students = teams.get(i).getStudents();
            for (int j = 0; j < students.size(); j++)
                if (students.get(i).getCip().equals(cip))
                    return true;
        }
        return false;
    }
}
