import React, { useState, useEffect } from 'react';
import { Link, useLocation } from 'react-router-dom';
import axios from 'axios';
import './../css/ManageTasks.css';
import UpcomingTasks from '../member/UpcomingTasks';
import AssignTasks from '../leader/AssignTasks';
import RateTasks from '../leader/RateTasks';

const ManageTasks = () => {
    const location = useLocation();
    const projectId = new URLSearchParams(location.search).get('id');
    const [project, setProject] = useState(null);
    const [activeComponent, setActiveComponent] = useState(null);

    useEffect(() => {
        const fetchProjectDetails = async () => {
            try {
                const token = localStorage.getItem('token');
                
                if (!token || !projectId) {
                    console.log('Token or projectId not found, unable to fetch project details.');
                    return;
                }
        
                const response = await axios.get(`http://localhost:8081/api/project/projectType/${projectId}`, {
                    headers: {
                        Authorization: `Bearer ${token}`
                    }
                });
                setProject(response.data);
            } catch (error) {
                console.error('Error fetching project details:', error);
            }
        };
    
        fetchProjectDetails();
    }, [projectId]);

    useEffect(() => {
        // Set active component based on project type
        if (project && project.projectType === 'group') {
            setActiveComponent('SelfTasks');
        } else {
            setActiveComponent(null);
        }
    }, [project]);

    return (
        <div>
            {activeComponent && (
                <div className="tabs">
                    <div
                        className={`tab ${activeComponent === 'SelfTasks' ? 'active-tab' : ''}`}
                        onClick={() => setActiveComponent('SelfTasks')}
                    >
                        Self Tasks
                    </div>
                    <div
                        className={`tab ${activeComponent === 'AssignTasks' ? 'active-tab' : ''}`}
                        onClick={() => setActiveComponent('AssignTasks')}
                    >
                        Assign Tasks
                    </div>
                    {(project && project.projectType === 'group') && (
                        <div
                            className={`tab ${activeComponent === 'RateCompletedAssignedTasks' ? 'active-tab' : ''}`}
                            onClick={() => setActiveComponent('RateCompletedAssignedTasks')}
                        >
                            Rate Completed Assigned Tasks
                        </div>
                    )}
                </div>
            )}
            <div className="component-container">
                {(activeComponent === 'SelfTasks' || (project && project.projectType === 'solo')) && (
                    <UpcomingTasks />
                )}
                {(activeComponent === 'AssignTasks' && project && project.projectType !== 'solo') && (
                    <div className="taskContent">
                        <AssignTasks/>
                    </div>
                )}
                {activeComponent === 'RateCompletedAssignedTasks' && (
                    <div className="taskContent">
                        <RateTasks />
                    </div>
                )}
            </div>
        </div>
    );
};

export default ManageTasks;
