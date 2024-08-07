import React, { useState, useEffect } from "react";
import { useParams, Link } from "react-router-dom";
import { useKeycloak } from '@react-keycloak/web';
import api from "../api/axiosConfig";
import styles from './styles.module.css';

const CourseDetails = () => {
    const { sigle } = useParams();
    const { keycloak } = useKeycloak();
    const [projects, setProjects] = useState([]);
    const [cours, setCours] = useState([]);
    const [coursProj, setCoursProj] = useState([]);
    let id_project = -1;

    const onValueChange = async (project_id) => {
        id_project = project_id;
    };

    const getProjects = async () => {
        try {
            const response = await api.get("/api/tutor/getProjects", {
                headers: {'Authorization': 'Bearer ' + keycloak.token},
                params: { sigle }
            });
            console.log(response.data)
            setProjects(response.data);
        } catch (err) {
            console.log("Error fetching projects:", err);
        }
    };

    const getCourseDetails = async () => {
        try {
            const response = await api.get(`/api/tutor/getCourseDetails`, {
                headers: {'Authorization': 'Bearer ' + keycloak.token},
                params: { sigle }
            });
            setCours(response.data);
        } catch (err) {
            console.log("Error fetching course details:", err);
        }
    };

    useEffect(() => {
        getCourseDetails();
        getProjects();
    }, [sigle]);

    const courseInfo = (course =>
        <p key={course.sigle}>{course.name}, {course.sigle}</p>
    );

    const courseEnd = (course =>
        <p key={course.remise}>fin: {new Date(course.remise).toLocaleDateString("en-US")}</p>
    );


    const projLink = projects.map(project => (
        <tr key={project.projectId}>
            <td className={styles.tableSectionName}>
                {project.id_project}            
            </td>
            <td className={styles.tableSectionMembers}>{project.students.map(student =>
                <p>{student.firstname} {student.lastname} </p>
            )}</td>
        </tr>
    ));

    return (
        <div className={styles.divContentTuteur}>
            <div className={styles.divListe}>
                <div>
                    <h2>{sigle}</h2>
                    <div>
                        <h3>{courseInfo}</h3>
                        {courseEnd}
                    </div>
                </div>
                <div>
                    <h3>équipes:</h3>
                    <div>
                        <table className={styles.tableProjet}>
                            <thead>
                                <tr>
                                    <th>équipe</th>
                                    <th>membres</th>
                                </tr>
                            </thead>
                            <tbody>
                                {projLink}
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>    
        </div>
    );
};

export default CourseDetails;
