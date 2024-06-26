package com.diploma.git.backend.mapper;

import com.diploma.git.backend.model.*;
import jakarta.websocket.server.PathParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface StudentMapper {
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

    @Select("SELECT t.cip, t.firstname, t.lastname, t.email " +
            "FROM tutor t " +
            "INNER JOIN tutor_course " +
            "ON t.cip = tutor_course.cip " +
            "INNER JOIN course_project " +
            "ON tutor_course.sigle = course_project.sigle " +
            "WHERE course_project.id_project = #{id_project} ")
    List<Tutor> getTutorsFromProject(@PathParam("id_project") int id_project);

    @Select("SELECT c.sigle, c.name, c.remise " +
            "FROM course c " +
            "INNER JOIN course_project " +
            "ON c.sigle = course_project.sigle " +
            "WHERE course_project.id_project = #{id_project} ")
    List<Course> getCoursesFromProject(@PathParam("id_project") int id_project);

    @Select("SELECT c.sigle, c.name " +
            "FROM course c " +
            "INNER JOIN tutor_course " +
            "ON c.sigle = tutor_course.sigle " +
            "WHERE tutor_course.cip = #{cip} ")
    List<Course> getCoursesFromTutor(@PathParam("cip") String cip);

    @Select("SELECT s.sigle " +
            "FROM student_course s " +
            "WHERE s.cip = #{cip} ")
    List<Course> getCoursesFromStudent(@PathParam("cip") String cip);

    @Select("SELECT e.id_event, e.cip, e.date_event, e.id_project " +
            "FROM event e " +
            "WHERE e.id_project = #{id_project} ")
    List<Event> getEventFromProject(@PathParam("id_project") int id_project);

    @Select("SELECT f.id_file, f.name, f.size, f.last_change, f.id_project " +
            "FROM file f " +
            "WHERE f.id_project = #{id_project} ")
    List<File> getFilesFromProject(@PathParam("id_project") int id_project);
}
