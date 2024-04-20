import TopPerformers from '../TopPerformers.js';
import React, { useState, useEffect } from 'react';
import axios from 'axios';
import './../css/Tasks.css';
import './../css/Performers.css';
import ErrorMessagePopup from '../error/ErrorMessagePopup';

const AllTasks = () => {
    const [errorMessage, setErrorMessage] = useState('');
  const [activeTasks, setActiveTasks] = useState([]);
  const [nonActiveTasks, setNonActiveTasks] = useState([]);
  const [projectRole, setProjectRole] = useState('');
  const [showForm, setShowForm] = useState(false);
  const [formData, setFormData] = useState({
    taskDescription: '',
    taskDeadline: '',
    taskPriority: 'Medium' // default priority
  });

  useEffect(() => {
    const fetchTasks = async () => {
      try {
        const projectId = new URLSearchParams(window.location.search).get("id");
        const token = localStorage.getItem("token");

        const activeResponse = await fetch(`http://localhost:8081/api/tasks/guestTasks/active?projectId=${projectId}`, {
          headers: {
            Authorization: `Bearer ${token}`
          }
        });
        const activeData = await activeResponse.json();
        setActiveTasks(activeData);

        const nonActiveResponse = await fetch(`http://localhost:8081/api/tasks/guestTasks/inactive?projectId=${projectId}`, {
          headers: {
            Authorization: `Bearer ${token}`
          }
        });
        const nonActiveData = await nonActiveResponse.json();
        setNonActiveTasks(nonActiveData);

        const projectRole = await fetchProjectUser(projectId);
        setProjectRole(projectRole);
      } catch (error) {
        console.error('Error fetching data:', error);
      }
    };

    fetchTasks();
  }, []); 

  const formatDeadline = (deadline) => {
    const deadlineDate = new Date(deadline);
    const day = deadlineDate.getDate();
    const month = deadlineDate.toLocaleString('en-us', { month: 'long' });
    const year = deadlineDate.getFullYear();
    const hour = deadlineDate.getHours();
    const minute = deadlineDate.getMinutes();

    return `${day} ${month}, ${year}, ${hour % 12 || 12}:${minute < 10 ? '0' : ''}${minute} ${hour >= 12 ? 'PM' : 'AM'}`;
  };

  const fetchProjectUser = async (projectId) => {
    try {
      const token = localStorage.getItem("token");
      if (!token) {
        console.log('No token for projectUsers');
        return '';
      }
      const response = await axios.get(`http://localhost:8081/api/project/projectUser/${projectId}`, {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });
      return response.data.projectRole;
    } catch (error) {
      console.log(error);
      return '';
    }
  };

  const toggleForm = () => {
    setShowForm(!showForm);
  };

  const handleCheckboxClick = (task) => {
    if (projectRole === 'Guest') {
      setErrorMessage('Guests are not allowed to partake in tasks');
    }
  };


  return (
    <div className='tasks'>
      <div className='tasklist'>
        <br/>
        <div className='scrollable-table'>
          <table className='task-table'>
            <thead>
              <tr>
                <th>Mark as Done</th>
                <th>Tasks</th>
                <th>Priority</th>
                <th>Deadline</th>
              </tr>
            </thead>
            <tbody>
              {activeTasks.length === 0 ? (
                <tr>
                  <td colSpan="4" style={{ textAlign: 'center' }}>No new tasks</td>
                </tr>
              ) : (
                activeTasks.map(task => (
                  <tr key={task.taskId}>
                    <td>
                      <center>
                        <input
                          type="checkbox"
                          checked={task.checked}
                          disabled={task.checked}
                          onChange={() => handleCheckboxClick(task)}
                        />
                      </center>
                    </td>
                    <td>{task.checked ? <s>{task.taskDescription}</s> : task.taskDescription}</td>
                    <td style={{ color: task.taskPriority === 'High' ? 'red' : task.taskPriority === 'Low' ? 'blue' : 'green' }}>
                      {task.checked ? <s>{task.taskPriority}</s> : task.taskPriority}
                    </td>
                    <td>{task.checked ? <s>{formatDeadline(task.taskDeadline)}</s> : formatDeadline(task.taskDeadline)}</td>
                  </tr>
                ))
              )}
              {nonActiveTasks.map(task => (
                <tr key={task.taskId} className="non-active-task">
                  <td><center><input type="checkbox" checked disabled /></center></td>
                  <td><s>{task.taskDescription}</s></td>
                  <td><s>{task.taskPriority}</s></td>
                  <td><s>{formatDeadline(task.taskDeadline)}</s></td>
                </tr>
              ))}
            </tbody>
          </table>
          {errorMessage && (
        <ErrorMessagePopup message={errorMessage} onClose={() => setErrorMessage('')} />
      )}
        </div>
      </div>
        <div className='performers'>
          <TopPerformers />
        </div>
    </div>
  );
};

export default AllTasks;
