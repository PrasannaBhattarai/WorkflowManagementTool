import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { Bar } from 'react-chartjs-2';
import './../css/BarChart.css'
import ProjectTypeChart from './ProjectTypeChart.js';
import ProjectStatusPieChart from './ProjectStatusPieChart.js';
import ProjectCount from './ProjectCount.js';

const ChartComponent = () => {
    const [chartData, setChartData] = useState(null);

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

    console.log('ChartData:', chartData);

    return (
        <div className='container-chart'> 
            <h2>Bar Chart</h2>
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
                                aspectRatio: 1, // You can adjust this value as needed
                                scales: {
                                    y: {
                                        beginAtZero: true
                                    }
                                }
                            }}
                        />
                    </>
                ) : (
                    <p>Loading...</p>
                )}
                <ProjectTypeChart />
                <ProjectStatusPieChart />
                <ProjectCount />

            </div>
        </div>
    );
    
};

export default ChartComponent;
