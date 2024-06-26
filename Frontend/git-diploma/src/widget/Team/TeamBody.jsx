import React, { useState, useEffect } from 'react';
import api from '../../api/axiosConfig';
import { useParams } from 'react-router-dom';
import TeamSelection from './TeamSelection';
import TeamDetails from './TeamDetails';

const TeamBody = () => {
    const { sigle } = useParams();
    const [inTeam, setInTeam] = useState([]);

    const isInTeam = async () => {
        try {
          const response = await api.get("/api/project/studentInTeam", {
            params: { cip:'lepl1501', sigle:sigle }
          });
          console.log(response);
          setInTeam(response.data);
        } catch (err) {
          console.log("Error fetching data:", err);
        }
      };

    useEffect(() => {
        if (sigle) {
          isInTeam(sigle);
        }
    }, [sigle]);

    return(
        <>
        {inTeam ? (<TeamDetails sigle={sigle}/>) : (<TeamSelection sigle={sigle}/>)}
        </>
    )
};

export default TeamBody;