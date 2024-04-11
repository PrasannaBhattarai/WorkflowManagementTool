import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { Line } from 'react-chartjs-2';
import ProjectRoles from './ProjectRolesPieChart';
import TopUsers from './TopUsersList';
import UserCount from './UserCount';

const ProjectMonthlyLineGraph = () => {
    const [chartData, setChartData] = useState(null);

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

    return (
        <div className='container-chart'> 
            <h2>Project Monthly Line Graph</h2>
            <div className='project-monthly-line-graph' >
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
                <ProjectRoles/>
                <TopUsers/>
                <UserCount/>
            </div>
        </div>
    );
};

export default ProjectMonthlyLineGraph;
