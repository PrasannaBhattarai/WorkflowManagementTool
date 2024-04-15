import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { Bar } from 'react-chartjs-2';
import './../css/BarChart.css'
import ProjectTypeChart from './ProjectTypeChart.js';
import ProjectStatusPieChart from './ProjectStatusPieChart.js';
import ProjectCount from './ProjectCount.js';
import { Link } from 'react-router-dom';

const ChartComponent = () => {
    const [chartData, setChartData] = useState(null);
    const [activeLink, setActiveLink] = useState('Project Analytics');

    useEffect(() => {
        const fetchData = async () => {
            try {
                const response = await axios.get('http://127.0.0.1:8000/chart-data/');
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

    console.log('ChartData:', chartData);

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
            <div className='chart-pane'>
                <h2>Dashboard</h2>
                <hr className='horizontal-line'></hr>
                <br></br>
                <br></br>
                <div className='project-bar' >
                    {chartData ? (
                        <>
                            <Bar
                                data={{
                                    labels: chartData.labels,
                                    datasets: [{
                                        label: 'User Count',
                                        data: chartData.data,
                                        backgroundColor: 'rgba(75, 192, 192, 0.2)',
                                        borderColor: 'rgba(75, 192, 192, 1)',
                                        borderWidth: 1
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
                            <div className="chart-count">
                                <ProjectCount />
                            </div>
                        </>
                    ) : (
                        <p>Loading...</p>
                    )}
                </div>
                <br></br> <br></br> <br></br>
                <div className="charts-container">
                    <div className="chart-1">
                    <h2>Project Type Pie Chart</h2>
                    <center>
                        <ProjectTypeChart />
                    </center>
                    </div>
                    <div className="chart-2">
                    <h2>Project Status Pie Chart</h2>
                    <center>
                        <ProjectStatusPieChart />
                    </center>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default ChartComponent;
