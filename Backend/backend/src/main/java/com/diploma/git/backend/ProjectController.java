package com.diploma.git.backend;

import com.diploma.git.backend.mapper.ProjectMapper;
import com.diploma.git.backend.mapper.StudentMapper;
import com.diploma.git.backend.model.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/project")
@CrossOrigin
public class ProjectController {
    @Autowired
    ProjectMapper projectMapper;

    @GetMapping("/getProjectsFromCourse")
    public List<Project> getProjectsFromCourse(@RequestParam("sigle") String sigle) {
        List<Project> list = projectMapper.getProjectsFromCourse(sigle);
        for (Project p : list) {
            p.setStudents(projectMapper.getProjectMembers(p.getId_project()));
        }
        return list;
    }

    @GetMapping("/setTeamMember")
    public boolean setTeamMember(@RequestParam("cip") String cip, @RequestParam("project_id") int project_id) {
        try {
            projectMapper.addTeamMember(project_id, cip);
        }catch (Exception exception){
            return false;
        }
        return true;
    }

    @GetMapping("/studentInTeam")
    public boolean studentInTeam(@RequestParam("cip") String cip, @RequestParam("sigle") String sigle){
        boolean isInTeam = false;

        isInTeam = projectMapper.studentInProject(cip, sigle) >= 1;

        return isInTeam;
    }
}
