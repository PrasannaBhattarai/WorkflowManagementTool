import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { Link } from 'react-router-dom';
import { Line } from 'react-chartjs-2';
import ProjectRoles from './ProjectRolesPieChart';
import TopUsers from './TopUsersList';
import UserCount from './UserCount';
import './../css/UserAnalytics.css';


const ProjectMonthlyLineGraph = () => {
    const [chartData, setChartData] = useState(null);
    const [activeLink, setActiveLink] = useState('User Analytics');

    useEffect(() => {
        const fetchData = async () => {
            try {
                const response = await axios.get('http://127.0.0.1:8000/project-monthly-data/');
                console.log('Fetched data:', response.data);
                setChartData(response.data);
            } catch (error) {
                console.error('Error fetching chart data:', error);
            }
        };
        fetchData();
    }, []);

    const handleLogout = () => {
        //clears cookies and localstorage
        localStorage.clear();
        document.cookie = 'custom_cookie=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;';

    };

    return (
        <div className='container-chart'> 
        <div className='navigation-pane'> 
                <center><h2>Admin Panel</h2></center>
                <br></br>
                <div className="navigation-links">
                    <Link to = '/chart'><a href="#" className={activeLink === 'Project Analytics' ? 'active' : ''} onClick={() => setActiveLink('Project Analytics')}>Project Analytics</a></Link>
                    <Link to = '/user'><a href="#" className={activeLink === 'User Analytics' ? 'active' : ''} onClick={() => setActiveLink('User Analytics')}>User Analytics</a></Link>
                    <Link to = '/request'><a href="#" className={activeLink === 'User Requests' ? 'active' : ''} onClick={() => setActiveLink('User Requests')}>User Requests</a></Link>
                    <div className="logout-button">
                        <Link to='/login' onClick={handleLogout}>Logout</Link>
                    </div>
                </div>
            </div>
            <div className='chart-pane' >
            <h2>Dashboard</h2>
                <hr className='horizontal-line'></hr>
                <br></br>
                <br></br>
                <div className='project-bar' >
                {chartData ? (
                    <Line
                        data={{
                            labels: chartData.data.map(item => item.month),
                            datasets: [{
                                label: 'Number of Records',
                                data: chartData.data.map(item => item.num_records),
                                fill: false,
                                borderColor: 'rgba(75, 192, 192, 1)',
                                tension: 0.1
                            }]
                        }}
                        options={{
                            maintainAspectRatio: true,
                            responsive: true,
                            aspectRatio: 1,
                            scales: {
                                y: {
                                    beginAtZero: true
                                }
                            }
                            
                        }}
                        
                    />
                ) : (
                    <p>Loading...</p>
                )}
                    <div className='chart-count'>
                        <UserCount/>
                    </div>
                </div>
                <br></br><br></br><br></br> <br></br>
                <div className='stats'>
                    <div className='top-projects'>
                        <h2>Users Distribution</h2>
                        <center>
                            <ProjectRoles/>
                        </center>
                    </div>
                    <br></br>
                    <div className='top-users'>
                        <TopUsers/>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default ProjectMonthlyLineGraph;
