package com.diploma.git.backend.mapper;

import com.diploma.git.backend.model.Project;
import com.diploma.git.backend.model.Student;
import jakarta.websocket.server.PathParam;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ProjectMapper {
    @Select("SELECT s.cip, s.lastname, s.firstname, s.email " +
            "FROM student s " +
            "INNER JOIN student_project " +
            "ON s.cip = student_project.cip " +
            "WHERE student_project.id_project = #{id_project} ")
    List<Student> getProjectMembers(@PathParam("id_project") int id_project);

    @Select("SELECT c.id_project, p.max_member " +
            "FROM course_project c " +
            "JOIN Project p ON p.id_project = c.id_project " +
            "WHERE c.sigle = #{sigle} ")
    List<Project> getProjectsFromCourse(@PathParam("sigle") String sigle);

    @Insert("INSERT INTO student_project VALUES (#{cip}, #{id_project})")
    void addTeamMember(@PathParam("id_project") int id_project, @PathParam("cip") String cip);

    @Select("SELECT COALESCE(COUNT(s.cip), 0) AS project_count " +
            "FROM Course_Project c " +
            "LEFT JOIN Student_Project s ON s.id_project = c.id_project AND s.cip = #{cip} " +
            "WHERE c.sigle = #{sigle}")
    int studentInProject(@PathParam("cip") String cip, @PathParam("sigle") String sigle);
}
