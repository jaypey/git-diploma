package com.diploma.git.backend.mapper;

import com.diploma.git.backend.model.Project;
import com.diploma.git.backend.model.Student;
import jakarta.websocket.server.PathParam;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TeamMapper {
    @Select("SELECT c.id_project " +
            "FROM course_project c " +
            "WHERE c.sigle = #{sigle} ")
    List<Project> getProjectsFromCourse(@PathParam("sigle") String sigle);

    @Select("SELECT s.cip, s.lastname, s.firstname, s.email " +
            "FROM student s " +
            "INNER JOIN student_project " +
            "ON s.cip = student_project.cip " +
            "WHERE student_project.id_project = #{id_project} ")
    List<Student> getStudentsFromProject(@PathParam("id_project") int id_project);

    @Select("SELECT c.team_size " +
            "FROM course c " +
            "WHERE c.sigle = #{sigle} ")
    int getTeamSizeFromCourse(@PathParam("sigle") String sigle);

    @Insert("INSERT INTO student_project VALUES (#{cip}, #{id_project})")
    void registerStudentInProject(@PathParam("id_project") int id_project,
                                  @PathParam("cip") String cip);
}
