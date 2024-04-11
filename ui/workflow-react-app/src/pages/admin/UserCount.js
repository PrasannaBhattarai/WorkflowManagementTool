import React, { useEffect, useState } from 'react';
import axios from 'axios';

const UserCount = () => {
    const [count, setCount] = useState(null);

    useEffect(() => {
        const fetchCount = async () => {
            try {
                const response = await axios.get('http://127.0.0.1:8000/user-count/');
                console.log('Fetched count:', response.data.user_count);
                setCount(response.data.user_count);
            } catch (error) {
                console.error('Error fetching user count:', error);
            }
        };
        fetchCount();
    }, []);

    return (
        <div>
            <h2>User Count</h2>
            {count !== null ? (
                <p>Total users: {count}</p>
            ) : (
                <p>Loading...</p>
            )}
        </div>
    );
};

export default UserCount;
