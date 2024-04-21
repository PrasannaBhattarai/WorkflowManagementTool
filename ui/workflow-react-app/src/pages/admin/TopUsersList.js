import React, { useEffect, useState } from 'react';
import axios from 'axios';
import './../css/TopUsersList.css';

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
            <hr></hr>
            {userData.map((user, index) => (
                <div key={index} className='user-rating'>
                    <p>{user.name} - Ratings: {user.user_ratings}</p>
                    <div className='stars'>
                        {[...Array(5)].map((_, i) => (
                            <svg key={i} className="star" xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill={i + 1 <= Math.floor(user.user_ratings) ? "gold" : "none"} stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                                <path d="M12 2 L15.09 8.26 L22 9.27 L17 14.14 L18.18 21.02 L12 17.77 L5.82 21.02 L7 14.14 L2 9.27 L8.91 8.26 Z"></path>
                            </svg> 
                        ))}
                    </div>
                </div>
            ))}
        </div>
    );
    
};

export default TopUsersList;
