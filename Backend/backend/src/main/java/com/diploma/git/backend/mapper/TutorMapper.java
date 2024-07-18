package com.diploma.git.backend.mapper;

import com.diploma.git.backend.model.*;
import jakarta.websocket.server.PathParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TutorMapper {
    @Select("SELECT s.cip, s.lastname, s.firstname, s.email " +
            "FROM student s " +
            "INNER JOIN student_project " +
            "ON s.cip = student_project.cip " +
            "WHERE student_project.id_project = #{id_project} ")
    List<Student> getStudentsFromProject(@PathParam("id_project") int id_project);

    @Select("SELECT p.id_project " +
            "FROM project p " +
            "INNER JOIN student_project " +
            "ON p.id_project = student_project.id_project " +
            "WHERE student_project.cip = #{cip} ")
    List<Project> getProjectsFromStudent(@PathParam("cip") String cip);

    @Select("SELECT c.sigle, c.name, c.remise " +
            "FROM course c " +
            "INNER JOIN tutor_course " +
            "ON c.sigle = tutor_course.sigle " +
            "WHERE tutor_course.cip = #{cip} ")
    List<Course> getCoursesFromTutor(@PathParam("cip") String cip);

    @Select("SELECT c.id_project " +
            "FROM course_project c " +
            "WHERE c.sigle = #{sigle} ")
    List<Project> getProjectsFromCourse(@PathParam("sigle") String sigle);

    @Select("SELECT e.id_event, e.cip, e.date_event, e.id_project " +
            "FROM event e " +
            "WHERE e.id_project = #{id_project} ")
    List<Event> getEventFromProject(@PathParam("id_project") int id_project);

    @Select("SELECT t.ssh " +
            "FROM tutor t " +
            "WHERE t.cip = #{cip} ")
    String getSSHFromTutor(@PathParam("cip") String cip);

    @Select("SELECT f.id_file, f.name, f.size, f.last_change, f.id_project " +
            "FROM file f " +
            "WHERE f.id_project = #{id_project} ")
    List<File> getFilesFromProject(@PathParam("id_project") int id_project);
}
