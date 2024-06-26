import React from 'react'
import ReactDOM from 'react-dom/client'
import App from './App.jsx'
import './index.css'
import { 
  createBrowserRouter,
  RouterProvider 
} from 'react-router-dom';
import StudentDashboard from './widget/studentDashboard.jsx';
import ProjectDetails from './widget/projectDetails.jsx';
import Login from './widget/loginPage.jsx';
import CourseSelection from './widget/CourseSelection.jsx';
import TeamBody from './widget/Team/TeamBody.jsx';

const router = createBrowserRouter([
  {
    path: "/",
    element: <App />,
    children: [
      {
        path: "/",
        element: <StudentDashboard />,
      },
      {
        path: "project/:projectId",
        element: <ProjectDetails />
      },
      {
        path: "Login",
        element: <Login />
      },
      {
         path: "equipe",
         element: <CourseSelection />
      },
      {
         path: "student/teamBody/:sigle",
         element: <TeamBody />
      }
    ]
  },
]);



ReactDOM.createRoot(document.getElementById('root')).render(
  <React.StrictMode>
    <RouterProvider router={router} />
  </React.StrictMode>,
)
