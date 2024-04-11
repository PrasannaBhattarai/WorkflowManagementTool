import React, { useState, useEffect } from 'react';
import { useLocation } from 'react-router-dom';
import axios from 'axios';
import './../css/RateTasks.css';

const RateTasks = () => {
  const [tasks, setTasks] = useState([]);
  const [passiveTasks, setPassiveTasks] = useState([]);
  const [ratedTasks, setRatedTasks] = useState([]);
  const [rating, setRating] = useState('');
  const location = useLocation();
  const projectId = new URLSearchParams(location.search).get('id');
  const [isButtonDisabled, setIsButtonDisabled] = useState(true);

  const fetchTasks = async () => {
    try {
      const token = localStorage.getItem('token');

      // Fetch active tasks
      const activeTasksResponse = await fetch(`http://localhost:8081/api/tasks/getActiveTasksForLeader/${projectId}`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      if (!activeTasksResponse.ok) {
        throw new Error('Failed to fetch active tasks');
      }
      const activeTasksData = await activeTasksResponse.json();
      setTasks(activeTasksData);

      // Fetch passive tasks
      const passiveTasksResponse = await fetch(`http://localhost:8081/api/tasks/getPassiveTasksForLeader/${projectId}`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      if (!passiveTasksResponse.ok) {
        throw new Error('Failed to fetch passive tasks');
      }
      const passiveTasksData = await passiveTasksResponse.json();
      setPassiveTasks(passiveTasksData);

      // Fetch rated tasks
      const ratedTasksResponse = await fetch(`http://localhost:8081/api/tasks/getRatedTasksForLeader/${projectId}`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      if (!ratedTasksResponse.ok) {
        throw new Error('Failed to fetch rated tasks');
      }
      const ratedTasksData = await ratedTasksResponse.json();
      setRatedTasks(ratedTasksData);
    } catch (error) {
      console.error(error);
    }
  };

  useEffect(() => {
    if (projectId) {
      fetchTasks();
    }
  }, [projectId]);

  const handleRatingChange = (event) => {
    const value = parseFloat(event.target.value);
    if (!isNaN(value) && value >= 1 && value <= 5) {
      setRating(value);
      setIsButtonDisabled(false);
    } else {
      setRating('');
      setIsButtonDisabled(true);
    }
  };

  const handleSubmitRating = async (taskId) => {
    try {
      const token = localStorage.getItem('token');
      const selectedTask = tasks.find(task => task.taskId === taskId);
      const assignedUserEmail = selectedTask.assignedUserEmail;

      const response = await axios.post(`http://localhost:8081/api/tasks/rateTask/${projectId}`, {
        rating: parseFloat(rating),
        taskId: taskId,
        email: assignedUserEmail,
      }, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      if (response.status === 200) {
        console.log('Rating submitted successfully');
        setRating('');
        setIsButtonDisabled(true);
        fetchTasks();
      } else {
        console.error('Failed to submit rating');
      }
    } catch (error) {
      console.error('Error submitting rating:', error);
    }
  };

  return (
    <div>
      <div className='rating-card'>
        <h2>Active Tasks</h2>
        {tasks.map(task => (
          <div className='card-container' key={task.taskId}>
            <div className='task-info'>
              <p>Description: {task.taskDescription}</p>
              <p>Deadline: {new Date(task.taskDeadline).toLocaleString()}</p>
              <p>Status: {task.taskStatus}</p>
              <p>
                Priority: <span className={task.taskPriority === 'Low' ? 'blue' : task.taskPriority === 'Medium' ? 'green' : 'red'}>
                  {task.taskPriority}
                </span>
              </p>
            </div>
            <label htmlFor={`rating_${task.taskId}`}>Rate the task:</label>
            <input className='input-field'
              type="number"
              id={`rating_${task.taskId}`}
              min="1"
              max="5"
              step="0.1"
              value={rating}
              onChange={handleRatingChange}
            />
            <button className='custom-submit' onClick={() => handleSubmitRating(task.taskId)} disabled={isButtonDisabled}>
              Submit Rating
            </button>
          </div>
        ))}
        <h2>Passive Tasks</h2>
        {passiveTasks.map(task => (
          <div className='card-container' key={task.taskId}>
            <div className='task-info'>
              <p>Description: {task.taskDescription}</p>
              <p>Deadline: {new Date(task.taskDeadline).toLocaleString()}</p>
              <p>Status: {task.taskStatus}</p>
              <p>Deadline Missed: {task.deadlineMissed ? 'Yes' : 'No'}</p>
              <p>
                Priority: <span className={task.taskPriority === 'Low' ? 'blue' : task.taskPriority === 'Medium' ? 'green' : 'red'}>
                  {task.taskPriority}
                </span>
              </p>
            </div>
          </div>
        ))}
        <h2>Rated Tasks</h2>
        {ratedTasks.map(task => (
          <div className='card-container' key={task.taskId}>
            <div className='task-info'>
              <p>Description: {task.taskDescription}</p>
              <p>Deadline: {new Date(task.taskDeadline).toLocaleString()}</p>
              <p>Status: {task.taskStatus}</p>
              <p>
                Priority: <span className={task.taskPriority === 'Low' ? 'blue' : task.taskPriority === 'Medium' ? 'green' : 'red'}>
                  {task.taskPriority}
                </span>
              </p>
              <p>
                Ratings: <span className='yellow'>{task.rating}</span>
              </p>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};

export default RateTasks;


