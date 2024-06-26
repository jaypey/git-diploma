import React, { useState, useEffect } from "react";
import styles from './styles.module.css';
import { projet } from './data.js';
import api from '../api/axiosConfig';
import { Link } from "react-router-dom";

const StudentDashboard = () => {

    const [projects, setProjects] = useState([]);

    const openProjet = projects.filter(project => new Date(project.courses[0].remise) >= Date.now());
    const closedProjet = projects.filter(project => new Date(project.courses[0].remise) < Date.now());
    const upcomingP = projects.filter(project => new Date(project.courses[0].remise) > Date.now()).sort(project => project.courses[0].remise).reverse();
    
    const listeProjet = openProjet.map((project, index) => {
        const sigles = project.courses.map(course => course.sigle).join(", ");
        return (
            <tr key={index}>
                <td><Link to={`/project/${project.id_project}`}>{sigles}</Link></td>
                <td>{new Date(project.courses[0].remise).toLocaleDateString("en-US")}</td>
            </tr>
        );
    });

    const ancienProjet = closedProjet.map((project, index) =>{
        const sigles = project.courses.map(course => course.sigle).join(", ");
        return(
            <tr key={index}>
                <td><Link to={`/project/${project.id_project}`}>{sigles}</Link></td>
                <td>{new Date(project.courses[0].remise).toLocaleDateString("en-US")}</td>
            </tr>
        );
    }
    );

    const upcoming = upcomingP.map(project =>{
        const sigles = project.courses.map(course => course.sigle).join(", ");
        return(
            <li>
                <p>{sigles}</p>
                <p>{new Date(project.courses[0].remise).toLocaleDateString("en-US")}</p>
            </li>
        )
    }
    )

    const getProjects = async () => {
        try {
            const response = await api.get("/api/student/getProjects", {
                params: { cip: "lepl1501" }
            });
            console.log(response);
            setProjects(response.data);
        } catch (err) {
            console.log("Error fetching data:", err);
        }
    };

    useEffect(() => {
        getProjects();
    }, []);

    return (
        <>
        <div className={styles.divContent}>

            <div className={styles.divListe}>
                <div>
                    <h2>projets</h2>
                    <div>
                        <table className={styles.tableProjet}>
                            <thead>
                                <tr>
                                    <th>Identifiant</th>
                                    <th>last commit</th>
                                </tr>
                            </thead>
                            <tbody>
                                {listeProjet}
                            </tbody>
                        </table>
                    </div>
                </div>
                <div>
                    <h2>anciens projets</h2>
                    <div>
                        <table className={styles.tableProjet}>
                            <thead>
                                <tr>
                                    <th>cours</th>
                                    <th>last commit</th>
                                </tr>
                            </thead>
                            <tbody>
                                {ancienProjet}
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
            <div className={styles.divUpcoming}>
                <h2 className={styles.h2Upcoming}>remise à venir</h2>
                <ul>
                    {upcoming}
                </ul>
            </div>
        </div>
        </>
    );
};

export default StudentDashboard;