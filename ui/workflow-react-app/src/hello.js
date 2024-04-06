import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import axios from 'axios';

const Hello = () => {
  const [data, setData] = useState([]);

  useEffect(() => {
    const fetchData = async () => {
      try {
        // Retrieve the token from local storage
        const token = localStorage.getItem('token');
        
        if (!token) {
          console.log('No token found, redirecting to login.');
          return;
        }

        const response = await axios.get('http://localhost:8081/api/project/projects', {
          headers: {
            Authorization: `Bearer ${token}`
          }
        });
        console.log(response.data);
        setData(response.data); // Assuming the data is an array, update state with fetched data
      } catch (error) {
        console.error('Error fetching data:', error);
        // Optionally, handle specific error cases. For example, if token is expired, redirect to login
      }
    };

    fetchData();
  }, []); // Empty dependency array ensures useEffect only runs once

  return (
    <div>
      <h2>Hello User With Token</h2>
      <Link to="/home">Go to Home Page</Link>
      <div>
        <h3>Fetched Data:</h3>
        <ul>
          {data.map((item, index) => (
            <li key={index}>{item.name ? item.name : 'No name provided'}</li> // Ensure that item.name exists, or provide a fallback
          ))}
        </ul>
      </div>
    </div>
  );
};

export default Hello;
