import React, { useEffect, useState } from 'react';
import axios from 'axios';

const ProjectCount = () => {
    const [count, setCount] = useState(null);

    useEffect(() => {
        const fetchCount = async () => {
            try {
                const response = await axios.get('http://127.0.0.1:8000/project-count/');
                console.log('Fetched count:', response.data.project_count);
                setCount(response.data.project_count);
            } catch (error) {
                console.error('Error fetching project count:', error);
            }
        };
        fetchCount();
    }, []);

    return (
        <div>
            <h2>Project Count</h2>
            {count !== null ? (
                <p>Total projects: <span className='big-count'>{count}</span></p>
            ) : (
                <p>Loading...</p>
            )}
        </div>
    );
};

export default ProjectCount;
