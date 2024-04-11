import React, { useEffect, useState } from 'react';
import axios from 'axios';

const TopUsersList = () => {
    const [userData, setUserData] = useState([]);

    useEffect(() => {
        const fetchData = async () => {
            try {
                const response = await axios.get('http://127.0.0.1:8000/top-users-data/');
                console.log('Fetched data:', response.data);
                setUserData(response.data.data);
            } catch (error) {
                console.error('Error fetching user data:', error);
            }
        };
        fetchData();
    }, []);

    return (
        <div>
            <h2>Top 5 Users</h2>
            <ul>
                {userData.map((user, index) => (
                    <li key={index}>{user.name} - Ratings: {user.user_ratings}</li>
                ))}
            </ul>
        </div>
    );
};

export default TopUsersList;
