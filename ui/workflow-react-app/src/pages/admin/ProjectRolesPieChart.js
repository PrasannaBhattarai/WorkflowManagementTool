// ProjectRolesPieChart.js

import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { Pie } from 'react-chartjs-2';

const ProjectRolesPieChart = () => {
    const [chartData, setChartData] = useState(null);

    useEffect(() => {
        const fetchData = async () => {
            try {
                const response = await axios.get('http://127.0.0.1:8000/project-roles-data/');
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
            <div className='project-roles-pie-chart' >
                {chartData ? (
                    <Pie
                        data={{
                            labels: chartData.data.map(item => item.project_role),
                            datasets: [{
                                label: 'Count',
                                data: chartData.data.map(item => item.count),
                                backgroundColor: [
                                    'rgba(255, 99, 132, 0.6)',
                                    'rgba(54, 162, 235, 0.6)',
                                    'rgba(255, 206, 86, 0.6)',
                                    'rgba(75, 192, 192, 0.6)',
                                    'rgba(153, 102, 255, 0.6)',
                                    'rgba(255, 159, 64, 0.6)'
                                ]
                            }]
                        }}
                        options={{
                            maintainAspectRatio: true,
                            responsive: true,
                            aspectRatio: 1,
                        }}
                    />
                ) : (
                    <p>Loading...</p>
                )}
            </div>
        </div>
    );
};

export default ProjectRolesPieChart;
