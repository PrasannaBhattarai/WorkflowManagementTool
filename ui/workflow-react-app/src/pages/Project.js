import React, { useState, useEffect } from 'react';
import { Link, useLocation } from 'react-router-dom';
import axios from 'axios';
import './css/Home.css';
import './css/Project.css';
import Guest from './Guest.js';
import Leader from './Leader.js';
import ToDo from './member/Todo.js';
import UpcomingTasks from './member/UpcomingTasks';
import NoticeBoard from './NoticeBoard';
import Chatroom from './Chat';

const Project = () => {
  const location = useLocation();
  const [projects, setProjects] = useState([]);
  const [showProjects, setShowProjects] = useState(false);
  const [user, setUser] = useState({ name: '' });
  const projectId = new URLSearchParams(location.search).get('id');
  const [projectRole, setProjectRole] = useState('');
  const [activeComponent, setActiveComponent] = useState(null);
  const [notificationNumber, setNotificationNumber] = useState(0);

  useEffect(() => {
    const fetchProjects = async () => {
      try {
        const token = localStorage.getItem('token');
        
        if (!token) {
          console.log('No token found, unable to fetch projects.');
          return;
        }

        const response = await axios.get('http://localhost:8081/api/project/projects', {
          headers: {
            Authorization: `Bearer ${token}`
          }
        });
        setProjects(response.data);
      } catch (error) {
        console.error('Error fetching projects:', error);
      }
    };

    const fetchNotificationNumber = async () => {
      try {
        const token = localStorage.getItem('token');
        if (!token) {
          console.log('No token found, unable to fetch notification number.');
          return;
        }
        const response = await axios.get('http://localhost:8081/api/project/getNotificationNumber', {
          headers: {
            Authorization: `Bearer ${token}`
          }
        });
        setNotificationNumber(response.data);
      } catch (error) {
        console.error('Error fetching notification number:', error);
      }
    };

    const fetchUser = async () => { 
      try {
        const token = localStorage.getItem('token');
        
        if (!token) {
          console.log('No token found, unable to fetch user.');
          return;
        }

        const response = await axios.get('http://localhost:8081/api/user/userDetails', {
          headers: {
            Authorization: `Bearer ${token}`
          }
        });
        setUser(response.data); // Set user data
      } catch (error) {
        console.error('Error fetching user:', error);
      }
    };

    const fetchProjectUser = async (projectId) => {
      try{
        const token = localStorage.getItem("token");
        if(!token){
          console.log('No token for projectUsers');
          return
        }
        const response = await axios.get(`http://localhost:8081/api/project/projectUser/${projectId}`,{
          headers: {
            Authorization: `Bearer ${token}`
          }
      });
        setProjectRole(response.data.projectRole)
      } catch(error){
        console.log(error);
        return null;
      }
    };

    fetchProjects();
    fetchUser();
    fetchNotificationNumber();
    fetchProjectUser(projectId)
  }, [projectId]);


  const handleLinkClick = (component) => {
    setActiveComponent(component);
  };
  

  const handleProjectClick = () => {
    setShowProjects(!showProjects); // Toggle showProjects
  };

  const handleSubmit = (event) => {
    event.preventDefault();
  };

  const renderActiveComponent = () => {
    switch (activeComponent) {
      case 'ToDo':
        return <ToDo />;
      case 'UpcomingTasks':
        return <UpcomingTasks />;
      case 'NoticeBoard':
        return <NoticeBoard />;
      case 'Chatroom':
        return <Chatroom />;
      default:
        return null;
    }
  };

  return (
    <div className="container">
      <div className="nav-panel">
        <div className="logo">
          <span>WORKFLOW MANAGEMENT TOOL</span>
        </div>
        <ul>
          <li>
            <Link to="/home">
              <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" fill="currentColor" class="bi bi-plus-circle" viewBox="0 0 16 16">
                <path d="M8 15A7 7 0 1 1 8 1a7 7 0 0 1 0 14m0 1A8 8 0 1 0 8 0a8 8 0 0 0 0 16"/>
                <path d="M8 4a.5.5 0 0 1 .5.5v3h3a.5.5 0 0 1 0 1h-3v3a.5.5 0 0 1-1 0v-3h-3a.5.5 0 0 1 0-1h3v-3A.5.5 0 0 1 8 4"/>
              </svg>
              Create a Project
            </Link>
          </li>
          <li><Link to="/edit-user-details">
            <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" fill="currentColor" class="bi bi-wrench-adjustable" viewBox="0 0 16 16">
              <path d="M16 4.5a4.5 4.5 0 0 1-1.703 3.526L13 5l2.959-1.11q.04.3.041.61"/>
              <path d="M11.5 9c.653 0 1.273-.139 1.833-.39L12 5.5 11 3l3.826-1.53A4.5 4.5 0 0 0 7.29 6.092l-6.116 5.096a2.583 2.583 0 1 0 3.638 3.638L9.908 8.71A4.5 4.5 0 0 0 11.5 9m-1.292-4.361-.596.893.809-.27a.25.25 0 0 1 .287.377l-.596.893.809-.27.158.475-1.5.5a.25.25 0 0 1-.287-.376l.596-.893-.809.27a.25.25 0 0 1-.287-.377l.596-.893-.809.27-.158-.475 1.5-.5a.25.25 0 0 1 .287.376M3 14a1 1 0 1 1 0-2 1 1 0 0 1 0 2"/>
            </svg>
            Edit User Details
          </Link></li>
          <li className={location.pathname.includes('/project') ? 'active' : ''}>
            <span onClick={handleProjectClick}>
              <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" fill="currentColor" class="bi bi-folder" viewBox="0 0 16 16">
                <path d="M.54 3.87.5 3a2 2 0 0 1 2-2h3.672a2 2 0 0 1 1.414.586l.828.828A2 2 0 0 0 9.828 3h3.982a2 2 0 0 1 1.992 2.181l-.637 7A2 2 0 0 1 13.174 14H2.826a2 2 0 0 1-1.991-1.819l-.637-7a2 2 0 0 1 .342-1.31zM2.19 4a1 1 0 0 0-.996 1.09l.637 7a1 1 0 0 0 .995.91h10.348a1 1 0 0 0 .995-.91l.637-7A1 1 0 0 0 13.81 4zm4.69-1.707A1 1 0 0 0 6.172 2H2.5a1 1 0 0 0-1 .981l.006.139q.323-.119.684-.12h5.396z"/>
              </svg>
              Open Projects
            </span>
            {showProjects && (
              <ul className="sub-menu">
               {projects.map((project) => (
                <li key={project.projectId} className={projectId === project.projectId ? 'active' : ''}>
                    <Link to={`/project?id=${project.projectId}`} onClick={() => handleProjectClick(project.projectId)}>
                    {project.projectName}
                    </Link>
                </li>
                ))}
              </ul>
            )}
          </li>
        </ul>
        <Link to="/" className="logout">
          <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" fill="currentColor" class="bi bi-box-arrow-left" viewBox="0 0 16 16">
            <path fill-rule="evenodd" d="M6 12.5a.5.5 0 0 0 .5.5h8a.5.5 0 0 0 .5-.5v-9a.5.5 0 0 0-.5-.5h-8a.5.5 0 0 0-.5.5v2a.5.5 0 0 1-1 0v-2A1.5 1.5 0 0 1 6.5 2h8A1.5 1.5 0 0 1 16 3.5v9a1.5 1.5 0 0 1-1.5 1.5h-8A1.5 1.5 0 0 1 5 12.5v-2a.5.5 0 0 1 1 0z"/>
            <path fill-rule="evenodd" d="M.146 8.354a.5.5 0 0 1 0-.708l3-3a.5.5 0 1 1 .708.708L1.707 7.5H10.5a.5.5 0 0 1 0 1H1.707l2.147 2.146a.5.5 0 0 1-.708.708z"/>
          </svg>
          Log Out
        </Link>
      </div>
      <div className="content">
        <div className="greeting">
        <div className='person'>
              <img src={`http://localhost:8081/${user.imageUrl}`} className="user-image" /> 
          </div>
          Name: {user.firstName} {user.lastName}  
          <Link to="/notifications">
          <div className='number'>
          <svg xmlns="http://www.w3.org/2000/svg" width="30" height="30" class="bi bi-bell" viewBox="0 0 16 16">
            <path d="M8 16a2 2 0 0 0 2-2H6a2 2 0 0 0 2 2M8 1.918l-.797.161A4 4 0 0 0 4 6c0 .628-.134 2.197-.459 3.742-.16.767-.376 1.566-.663 2.258h10.244c-.287-.692-.502-1.49-.663-2.258C12.134 8.197 12 6.628 12 6a4 4 0 0 0-3.203-3.92zM14.22 12c.223.447.481.801.78 1H1c.299-.199.557-.553.78-1C2.68 10.2 3 6.88 3 6c0-2.42 1.72-4.44 4.005-4.901a1 1 0 1 1 1.99 0A5 5 0 0 1 13 6c0 .88.32 4.2 1.22 6"/>
          </svg>
          </div>
          
            <div className='noti'>
              <span className="notification-badge">{notificationNumber}</span>
            </div>
          </Link>
        </div>

        {projectRole === 'Member' ? (
        <div className='projectPage'>
          <div className="top-panel">
            <a href="#" className="top-link" onClick={() => handleLinkClick('ToDo')}>
            <svg xmlns="http://www.w3.org/2000/svg" width="26" height="26" fill="currentColor" class="bi bi-list-task" viewBox="0 0 16 16">
              <path fill-rule="evenodd" d="M2 2.5a.5.5 0 0 0-.5.5v1a.5.5 0 0 0 .5.5h1a.5.5 0 0 0 .5-.5V3a.5.5 0 0 0-.5-.5zM3 3H2v1h1z"/>
              <path d="M5 3.5a.5.5 0 0 1 .5-.5h9a.5.5 0 0 1 0 1h-9a.5.5 0 0 1-.5-.5M5.5 7a.5.5 0 0 0 0 1h9a.5.5 0 0 0 0-1zm0 4a.5.5 0 0 0 0 1h9a.5.5 0 0 0 0-1z"/>
              <path fill-rule="evenodd" d="M1.5 7a.5.5 0 0 1 .5-.5h1a.5.5 0 0 1 .5.5v1a.5.5 0 0 1-.5.5H2a.5.5 0 0 1-.5-.5zM2 7h1v1H2zm0 3.5a.5.5 0 0 0-.5.5v1a.5.5 0 0 0 .5.5h1a.5.5 0 0 0 .5-.5v-1a.5.5 0 0 0-.5-.5zm1 .5H2v1h1z"/>
            </svg>
              To-Do List
              </a>
            <a href="#" className="top-link" onClick={() => handleLinkClick('UpcomingTasks')}>
            <svg xmlns="http://www.w3.org/2000/svg" width="26" height="26" fill="currentColor" class="bi bi-puzzle-fill" viewBox="0 0 16 16">
              <path d="M3.112 3.645A1.5 1.5 0 0 1 4.605 2H7a.5.5 0 0 1 .5.5v.382c0 .696-.497 1.182-.872 1.469a.5.5 0 0 0-.115.118l-.012.025L6.5 4.5v.003l.003.01q.005.015.036.053a.9.9 0 0 0 .27.194C7.09 4.9 7.51 5 8 5c.492 0 .912-.1 1.19-.24a.9.9 0 0 0 .271-.194.2.2 0 0 0 .036-.054l.003-.01v-.008l-.012-.025a.5.5 0 0 0-.115-.118c-.375-.287-.872-.773-.872-1.469V2.5A.5.5 0 0 1 9 2h2.395a1.5 1.5 0 0 1 1.493 1.645L12.645 6.5h.237c.195 0 .42-.147.675-.48.21-.274.528-.52.943-.52.568 0 .947.447 1.154.862C15.877 6.807 16 7.387 16 8s-.123 1.193-.346 1.638c-.207.415-.586.862-1.154.862-.415 0-.733-.246-.943-.52-.255-.333-.48-.48-.675-.48h-.237l.243 2.855A1.5 1.5 0 0 1 11.395 14H9a.5.5 0 0 1-.5-.5v-.382c0-.696.497-1.182.872-1.469a.5.5 0 0 0 .115-.118l.012-.025.001-.006v-.003l-.003-.01a.2.2 0 0 0-.036-.053.9.9 0 0 0-.27-.194C8.91 11.1 8.49 11 8 11s-.912.1-1.19.24a.9.9 0 0 0-.271.194.2.2 0 0 0-.036.054l-.003.01v.002l.001.006.012.025c.016.027.05.068.115.118.375.287.872.773.872 1.469v.382a.5.5 0 0 1-.5.5H4.605a1.5 1.5 0 0 1-1.493-1.645L3.356 9.5h-.238c-.195 0-.42.147-.675.48-.21.274-.528.52-.943.52-.568 0-.947-.447-1.154-.862C.123 9.193 0 8.613 0 8s.123-1.193.346-1.638C.553 5.947.932 5.5 1.5 5.5c.415 0 .733.246.943.52.255.333.48.48.675.48h.238z"/>
            </svg>
              View Upcoming Tasks
              </a>
            <a href="#" className="top-link" onClick={() => handleLinkClick('NoticeBoard')}>
            <svg xmlns="http://www.w3.org/2000/svg" width="26" height="26" fill="currentColor" class="bi bi-file-earmark-text-fill" viewBox="0 0 16 16">
              <path d="M9.293 0H4a2 2 0 0 0-2 2v12a2 2 0 0 0 2 2h8a2 2 0 0 0 2-2V4.707A1 1 0 0 0 13.707 4L10 .293A1 1 0 0 0 9.293 0M9.5 3.5v-2l3 3h-2a1 1 0 0 1-1-1M4.5 9a.5.5 0 0 1 0-1h7a.5.5 0 0 1 0 1zM4 10.5a.5.5 0 0 1 .5-.5h7a.5.5 0 0 1 0 1h-7a.5.5 0 0 1-.5-.5m.5 2.5a.5.5 0 0 1 0-1h4a.5.5 0 0 1 0 1z"/>
            </svg>
              Notice Board</a>
            <a href="#" className="top-link" onClick={() => handleLinkClick('Chatroom')}>
            <svg xmlns="http://www.w3.org/2000/svg" width="26" height="26" fill="currentColor" class="bi bi-chat-fill" viewBox="0 0 16 16">
              <path d="M8 15c4.418 0 8-3.134 8-7s-3.582-7-8-7-8 3.134-8 7c0 1.76.743 3.37 1.97 4.6-.097 1.016-.417 2.13-.771 2.966-.079.186.074.394.273.362 2.256-.37 3.597-.938 4.18-1.234A9 9 0 0 0 8 15"/>
            </svg>
              Chatroom</a>
          </div>
          {renderActiveComponent()}
        </div>) : 
        projectRole === 'Leader' ? (
            <Leader />
        ) : (
            <Guest />
        )}
      </div>
      <br/><br/>
    </div>
  );
};

export default Project;
