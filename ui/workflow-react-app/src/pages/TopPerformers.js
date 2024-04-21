import React, { useState, useEffect } from 'react';
import { Link, useLocation } from 'react-router-dom';
import axios from 'axios';
import './css/Performers.css';

const TopPerformers = () => {
    const location = useLocation();
    const projectId = new URLSearchParams(location.search).get('id');
    const [projectRole, setProjectRole] = useState('');
    const [projectDes, setProjectDes] = useState('');
    const [topPerformers, setTopPerformers] = useState([])

    useEffect(() => {

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
              setProjectDes(response.data.userType)
            } catch(error){
              console.log(error);
              return null;
            }
          };


          
        const fetchPerformers = async (projectId) => {
            try{
              const token = localStorage.getItem("token");
              if(!token){
                console.log('No token for projectUsers');
                return
              }
              const response = await axios.get(`http://localhost:8081/api/project/performance/${projectId}`,{
                headers: {
                  Authorization: `Bearer ${token}`
                }
            });
              setTopPerformers(response.data)
            } catch(error){
              console.log(error);
              return null;
            }
          };

          fetchPerformers(projectId)
          fetchProjectUser(projectId)
        
    },[projectId]);


    return (
        <div className='performance'>
            <div className="performers-list">
                <h2>Top Performers in Project</h2>
                {topPerformers.map((performer, index) => (
            <div key={index} className="performer">
                {/* <svg xmlns="http://www.w3.org/2000/svg" width="25" height="25" fill="currentColor" class="bi bi-person-fill" viewBox="0 0 16 16">
                    <path d="M3 14s-1 0-1-1 1-4 6-4 6 3 6 4-1 1-1 1zm5-6a3 3 0 1 0 0-6 3 3 0 0 0 0 6"/>
                </svg> */}
               
                <div className='pic'>
                <img src={performer.imageUrl} alt="performer" width="47" height="47" />
                    {performer.firstName} {performer.lastName}</div>
                <div className='text'>&nbsp;&nbsp;&nbsp;{performer.userRatings}&nbsp;&nbsp;&nbsp;
                    {[...Array(5)].map((_, i) => (
                        <svg key={i} className="star" xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill={i + 1 <= Math.floor(performer.userRatings) ? "gold" : "none"} stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                            <path d="M12 2 L15.09 8.26 L22 9.27 L17 14.14 L18.18 21.02 L12 17.77 L5.82 21.02 L7 14.14 L2 9.27 L8.91 8.26 Z"></path>
                        </svg> 
                    ))}
                </div>
            </div>
            ))}

                <div className='role'>
                    <h3>
                        {projectRole==='Member' && (
                            <>
                                User Designation: {projectDes}
                                <br/>
                            </>
                        )}
                        Role: {projectRole}
                    </h3>
                </div>
            </div>
        </div>
    );
};

export default TopPerformers;