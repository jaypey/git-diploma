import React, { useState } from "react";
import styles from "./styles.module.css"
import { course } from "./data";



const AddCourse = () => {

    const courseNotSet = course.filter(course => course.end == null);
    const [selectedCourseId, setSelectedCourseId] = useState('');
    const [endDate, setEndDate] = useState('');
    const [teamSize, setTeamSize] = useState('');
    const [tempEndDates, setTempEndDates] = useState({});
  
    const handleSelectCourse = (e) => {
      setSelectedCourseId(e.target.value);
      setEndDate(tempEndDates[e.target.value] || '');
    };
  
    const handleEndDateChange = (e) => {
      setEndDate(e.target.value);
    };

    const handleTeamSizeChange = (e) => {
      setTeamSize(e.target.value);
    };
  
    const handleSubmit = async () => {
      try {
        const response = await api.get("/api/tutor/getCourses", {
            headers: {'Authorization': 'Bearer ' + keycloak.token},
            params: { cip: keycloak.tokenParsed.preferred_username }
        });
        console.log(response);
        setCours(response.data);
    } catch (err) {
        console.log("Error fetching data:", err);
    }

      setTempEndDates({
        ...tempEndDates,
        [selectedCourseId]: endDate
      });
      console.log(`Temporary End Date set for Course ID: ${selectedCourseId}, End Date: ${endDate}`);
    };
  
    return (
      <div className={styles.divCentered}>
        <div className={styles.divCenteredBorder}>
            <h2>Course Selection</h2>
            <div className={styles.divCenteredList}>
                <div className={styles.inputGroup}>    
                    <select onChange={handleSelectCourse} value={selectedCourseId}>
                    <option value="">Select a course</option>
                    {courseNotSet.map((course) => (
                        <option key={course.courseId} value={course.courseId}>
                        {course.name}, {course.code}
                        </option>
                    ))}
                    </select>
                </div>
                <div className={styles.inputGroup}>
                <label htmlFor="endDate">End Date: </label>
                <input
                    type="date"
                    id="endDate"
                    value={endDate}
                    onChange={handleEndDateChange}
                    disabled={!selectedCourseId}
                />
                </div>
                <div className={styles.inputGroup}>
                <label htmlFor="teamSize">Taille des équipes: </label>
                <input
                    type="number"
                    id="teamSize"
                    value={teamSize}
                    onChange={handleTeamSizeChange}
                    disabled={!selectedCourseId}
                    min="1"
                />
                </div>
                <button onClick={handleSubmit} disabled={!selectedCourseId || !endDate} className={styles.setEndDateButton}>
                Set End Date
                </button>
                {selectedCourseId && (
                <div>
                    <h2>Selected Course</h2>
                    <p>{courseNotSet.find(course => course.courseId === parseInt(selectedCourseId))?.name}</p>
                    <p>Temporary End Date: {tempEndDates[selectedCourseId]}</p>
                </div>
                )}
            </div>
        </div>
      </div>
    );
};



export default AddCourse;