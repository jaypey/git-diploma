import { useState, useEffect } from 'react';
import './App.css';
import Navbar from "./widget/navbar";
import {
    BrowserRouter as Router,
    Routes,
    Route,
    Outlet,
} from "react-router-dom";
import api from './api/axiosConfig';

function App() {
  //const [membre, setMembre] = useState();
//
  //const getMembres = async() => {
  //  try{
  //    const response = await api.get("/api/student/getStudent", {
  //      params:{
  //        cip: 'aubm1811'
  //      }
  //    });
  //    console.log(response.data);
  //    setMembre(response.data);
  //  }catch(err){
  //    console.log(err);
  //  }
  //}
//
  //useEffect(() =>{
  //  getMembres();
  //}, [])

  return (
    <>
      <Navbar />
      <div>
        <Outlet />
      </div>
    </>
  );
}

export default App
