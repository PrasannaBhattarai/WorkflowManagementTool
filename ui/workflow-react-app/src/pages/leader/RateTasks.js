import React, { useState, useEffect } from 'react';
import { useLocation } from 'react-router-dom';
import axios from 'axios';
import './../css/RateTasks.css';
import WarningPopup from '../error/WarningPopup';
import ErrorMessagePopup from '../error/ErrorMessagePopup';
import SuccessPopup from '../error/SuccessPopup';

const RateTasks = () => {
  const [tasks, setTasks] = useState([]);
  const [passiveTasks, setPassiveTasks] = useState([]);
  const [ratedTasks, setRatedTasks] = useState([]);
  const [ratings, setRatings] = useState({});
  const location = useLocation();
  const projectId = new URLSearchParams(location.search).get('id');
  const [isButtonDisabled, setIsButtonDisabled] = useState(true);
  const [showWarning, setShowWarning] = useState(false);
  const [errorMessage, setErrorMessage] = useState('');
  const [showErrorMessage, setShowErrorMessage] = useState(false);
  const [successMessage, setSuccessMessage] = useState('');
  const [showSuccessMessage, setShowSuccessMessage] = useState(false);

  const fetchTasks = async () => {
    try {
      const token = localStorage.getItem('token');
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

  const handleRatingChange = (event, taskId) => {
    const value = event.target.value;
    setRatings({ ...ratings, [taskId]: value });
    setIsButtonDisabled(false);
  };

  const handleSubmitRating = async (taskId) => {
    const token = localStorage.getItem('token');
    const selectedTask = tasks.find((task) => task.taskId === taskId);
    const assignedUserEmail = selectedTask.assignedUserEmail;
    const ratingValue = parseFloat(ratings[taskId]);

    if (ratingValue >= 1 && ratingValue <= 5) {
      try {
        const response = await axios.post(
          `http://localhost:8081/api/tasks/rateTask/${projectId}`,
          {
            rating: ratingValue,
            taskId: taskId,
            email: assignedUserEmail,
          },
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );

        if (response.status === 200) {
          setSuccessMessage('Rating submitted successfully');
          setShowSuccessMessage(true);
          setRatings({ ...ratings, [taskId]: '' });
          setIsButtonDisabled(true);
          fetchTasks();
        } else {
          console.error('Failed to submit rating');
        }
      } catch (error) {
        console.error('Error submitting rating:', error);
      }
    } else {
      setShowWarning(true);
    }
  };

  const handleCloseWarning = () => {
    setShowWarning(false);
  };

  const handleRemoveTask = (taskId) => {
    console.log("Removing task with ID:", taskId);
    try {
      setErrorMessage(`Task has been removed.`);
      setShowErrorMessage(true);
    } catch (error) {
      console.error("Error removing task:", error);
      setErrorMessage("Error removing task. Please try again.");
      setShowErrorMessage(true);
    }
  };

  const handleErrorMessageClose = () => {
    setShowErrorMessage(false);
  };

  const handleSuccessMessageClose = () => {
    setShowSuccessMessage(false);
  };

  return (
    <div>
      <div className='rating-card'>
        {tasks.length === 0 && passiveTasks.length === 0 && ratedTasks.length === 0 ? (
          <p>No tasks available!</p>
        ) : (
          <>
            {tasks.length > 0 && (
              <> 
                <h2>Active Tasks</h2>
                {tasks.map(task => (
                  <div className='card-containers' key={task.taskId}>
                    <div className='imgs'>
                      <svg xmlns="http://www.w3.org/2000/svg" width="26" height="26" fill="currentColor" class="bi bi-star-fill" viewBox="0 0 16 16">
                        <path d="M3.612 15.443c-.386.198-.824-.149-.746-.592l.83-4.73L.173 6.765c-.329-.314-.158-.888.283-.95l4.898-.696L7.538.792c.197-.39.73-.39.927 0l2.184 4.327 4.898.696c.441.062.612.636.282.95l-3.522 3.356.83 4.73c.078.443-.36.79-.746.592L8 13.187l-4.389 2.256z"/>
                      </svg>
                    </div>
                    <div className='task-info'>
                      <p><span className='labelsInfo'>Description:</span> {task.taskDescription}</p>
                      <p><span className='labelsInfo'>Deadline:</span> {new Date(task.taskDeadline).toLocaleString()}</p>
                      <p><span className='labelsInfo'>Status:</span> {task.taskStatus}</p>
                      <p>
                      <span className='labelsInfo'>Priority: </span><span className={task.taskPriority === 'Low' ? 'blue' : task.taskPriority === 'Medium' ? 'green' : 'red'}>
                          {task.taskPriority}
                        </span>
                      </p>
                    
                    <span className='labelsInfo'> Rate the task:</span>
                    <span className='msg'>*Must be between 1 to 5</span>
                    <input
                      className='input-field'
                      type="number"
                      id={`rating_${task.taskId}`}
                      value={ratings[task.taskId]}
                      onChange={(event) => handleRatingChange(event, task.taskId)}
                    />
                    </div>
                    <button
                      className='custom-submit'
                      onClick={() => handleSubmitRating(task.taskId)}
                      disabled={isButtonDisabled}
                    >
                      Submit Rating
                    </button>
                    
                  </div>
                ))}
              </>
            )}
            {passiveTasks.length > 0 && (
              <>
                <h2>Incomplete Tasks</h2>
                {passiveTasks.map(task => (
                  <div className='card-container' key={task.taskId}>
                    <div className='imgs'>
                      <svg xmlns="http://www.w3.org/2000/svg" width="26" height="26" fill="currentColor" class="bi bi-hourglass-bottom" viewBox="0 0 16 16">
                        <path d="M2 1.5a.5.5 0 0 1 .5-.5h11a.5.5 0 0 1 0 1h-1v1a4.5 4.5 0 0 1-2.557 4.06c-.29.139-.443.377-.443.59v.7c0 .213.154.451.443.59A4.5 4.5 0 0 1 12.5 13v1h1a.5.5 0 0 1 0 1h-11a.5.5 0 1 1 0-1h1v-1a4.5 4.5 0 0 1 2.557-4.06c.29-.139.443-.377.443-.59v-.7c0-.213-.154-.451-.443-.59A4.5 4.5 0 0 1 3.5 3V2h-1a.5.5 0 0 1-.5-.5m2.5.5v1a3.5 3.5 0 0 0 1.989 3.158c.533.256 1.011.791 1.011 1.491v.702s.18.149.5.149.5-.15.5-.15v-.7c0-.701.478-1.236 1.011-1.492A3.5 3.5 0 0 0 11.5 3V2z"/>
                      </svg></div>
                    <div className='task-info'>
                      <p><span className='labelsInfo'>Description:</span> {task.taskDescription}</p>
                      <p><span className='labelsInfo'>Deadline:</span> {new Date(task.taskDeadline).toLocaleString()}</p>
                      <p><span className='labelsInfo'>Status: </span>{task.taskStatus}</p>
                      <p><span className='labelsInfo'>Deadline Missed:</span> {task.deadlineMissed ? 'Yes' : 'No'}</p>
                      <p>
                      <span className='labelsInfo'>Priority: </span><span className={task.taskPriority === 'Low' ? 'blue' : task.taskPriority === 'Medium' ? 'green' : 'red'}>
                          {task.taskPriority}
                        </span>
                      </p>
                      {task.deadlineMissed && (
                        <button
                          className='custom-remove'
                          onClick={() => handleRemoveTask(task.taskId)}
                        >
                          Remove
                        </button>
                      )}
                    </div>
                  </div>
                ))}
              </>
            )}
            {ratedTasks.length > 0 && (
              <>
                <h2>Rated Tasks</h2>
                {ratedTasks.map(task => (
                  <div className='card-container' key={task.taskId}>
                    <div className='imgs'>
                      <svg xmlns="http://www.w3.org/2000/svg" width="26" height="26" fill="currentColor" class="bi bi-check-lg" viewBox="0 0 16 16">
                        <path d="M12.736 3.97a.733.733 0 0 1 1.047 0c.286.289.29.756.01 1.05L7.88 12.01a.733.733 0 0 1-1.065.02L3.217 8.384a.757.757 0 0 1 0-1.06.733.733 0 0 1 1.047 0l3.052 3.093 5.4-6.425z"/>
                      </svg>
                    </div>
                    <div className='task-info'>
                      <p><span className='labelsInfo'>Description:</span> {task.taskDescription}</p>
                      <p><span className='labelsInfo'>Deadline: </span>{new Date(task.taskDeadline).toLocaleString()}</p>
                      <p><span className='labelsInfo'>Status:</span> {task.taskStatus}</p>
                      <p>
                      <span className='labelsInfo'>Priority: </span><span className={task.taskPriority === 'Low' ? 'blue' : task.taskPriority === 'Medium' ? 'green' : 'red'}>
                          {task.taskPriority}
                        </span>
                      </p>
                      <p>
                      <span className='labelsInfo'>Ratings:</span> <span className='yellow'>{task.rating}</span>
                      </p>
                      <button
                      className='custom-remove'
                      onClick={() => handleRemoveTask(task.taskId)}
                    >Remove</button>
                    </div>
                  </div>
                ))}
              </>
            )}
          </>
        )}
        {showWarning && (
          <WarningPopup
            message="Value must be between 1 and 5"
            onClose={handleCloseWarning}
          />
        )}
         {showErrorMessage && (
        <ErrorMessagePopup
          message={errorMessage}
          onClose={handleErrorMessageClose}
        />
      )}
      {showSuccessMessage && (
        <SuccessPopup
          message={successMessage}
          onClose={handleSuccessMessageClose}
        />
      )}
      </div>
    </div>
  );
};

export default RateTasks;
