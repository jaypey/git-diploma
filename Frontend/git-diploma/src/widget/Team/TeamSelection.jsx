import React, { useState, useEffect } from 'react';
import { redirect } from 'react-router-dom';
import api from '../../api/axiosConfig';
import styles from '../styles.module.css';

function TeamSelection({sigle}) {
  const [projects, setProjects] = useState([]);
  let id_project = -1;

  const onValueChange = async (project_id) => {
      id_project = project_id;
    };

  const getProjects = async () => {
    try {
      const response = await api.get("/api/team/getProjects", {
        params: { sigle }
      });
      console.log(response);
      setProjects(response.data);
    } catch (err) {
      console.log("Error fetching data:", err);
    }
  };

  const registerInProject = async () => {
    try {
      const response = await api.get("/api/team/registerInProject", {
        params: { sigle: sigle, id_project: id_project, cip: 'lepl1501' }
      });
      console.log(response);
      window.location.href = '/student/teamBody/' + sigle;
    } catch (err) {
      console.log("Error fetching data:", err);
    }
  };

  useEffect(() => {
    if (sigle) {
      getProjects();
    }
  }, [sigle]);

  const ProjectList = projects.map((project, index) => (
    <tr key={index}>
      <td>{project.max_member > project.students.length? <input type='radio' onClick={() => onValueChange(project.id_project)} /> : ""} {project.id_project}</td>
      <td>{project.students.map(student => (<p>{student.firstname} {student.lastname},</p>))}</td>
    </tr>
  ));

  return (
    <div className={styles.divContent}>
      <div className={styles.divListe}>
        <h2>Choix d'équipe pour le cours: {sigle}</h2>
        <table className={styles.tableProjet}>
          <thead>
            <tr>
              <th>Numéro d'équipe</th>
              <th>Membres de l'équipe</th>
            </tr>
          </thead>
          <tbody>
            {ProjectList}
          </tbody>
        </table>
        <button onClick={registerInProject}> Confirmer </button>
      </div>
    </div>
  );
};

export default TeamSelection;