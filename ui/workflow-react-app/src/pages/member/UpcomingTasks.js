import TopPerformers from '../TopPerformers.js';
import React, { useState, useEffect } from 'react';
import axios from 'axios';
import './../css/Tasks.css';
import './../css/Performers.css';

const UpcomingTasks = () => {
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

        const activeResponse = await fetch(`http://localhost:8081/api/tasks/active?projectId=${projectId}`, {
          headers: {
            Authorization: `Bearer ${token}`
          }
        });
        const activeData = await activeResponse.json();
        setActiveTasks(activeData);

        const nonActiveResponse = await fetch(`http://localhost:8081/api/tasks/non-active?projectId=${projectId}`, {
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

  const handleCheckboxClick = async (taskId) => {
    try {
      const token = localStorage.getItem("token");

      await fetch(`http://localhost:8081/api/tasks/disable/${taskId}`, {
        method: 'PUT',
        headers: {
          Authorization: `Bearer ${token}`,
          'Content-Type': 'application/json'
        }
      });

      setActiveTasks(prevActiveTasks => 
        prevActiveTasks.map(task => 
          task.taskId === taskId ? { ...task, checked: true } : task
        )
      );
    } catch (error) {
      console.error('Error disabling task:', error);
    }
  };

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

  const handleFormSubmit = async (event) => {
    event.preventDefault();
    try {
      const projectId = new URLSearchParams(window.location.search).get("id");
      const token = localStorage.getItem("token");
      

      const taskData = {
        ...formData
      };

      const response = await axios.post(`http://localhost:8081/api/tasks/create?projectId=${projectId}&assignedUser=null`, taskData, {
        headers: {
          Authorization: `Bearer ${token}`,
          'Content-Type': 'application/json'
        }
      });

      console.log('Task created:', response.data);
      setShowForm(!showForm);
    } catch (error) {
      console.error('Error creating task:', error);
    }
  };

  const handleInputChange = (event) => {
    const { name, value } = event.target;
    setFormData({
      ...formData,
      [name]: value
    });
  };

  return (
    <div className='tasks'>
       {showForm ? (
        <div className='todoForm'>
          <div className='design'>
          <br/>
          <form onSubmit={handleFormSubmit}>
            <label htmlFor="taskDescription">Task Description:</label>
            <input type="text" id="taskDescription" name="taskDescription" value={formData.taskDescription} onChange={handleInputChange} required/>
            <br/><br/>
            <label htmlFor="taskDeadline">Task Deadline:</label>
            <input type="datetime-local" id="taskDeadline" name="taskDeadline" value={formData.taskDeadline} onChange={handleInputChange} required/>
            <br/><br/>
            <label htmlFor="taskPriority">Task Priority:</label>
            <select id="taskPriority" name="taskPriority" value={formData.taskPriority} onChange={handleInputChange}>
              <option value="Low">Low</option>
              <option value="Medium">Medium</option>
              <option value="High">High</option>
            </select>
            <br/><br/>
            <center>
              <button type="submit" className="custom-button">Submit</button>
            </center>
            <br/>
            <center>
              <button type="button" className="cancel-button" onClick={toggleForm}>Cancel</button>
            </center>
          </form>

      </div>
    </div>
  ) : (
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
                          onClick={() => handleCheckboxClick(task.taskId)}
                          disabled={task.checked}
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
        </div>
      </div>
      )}
        <div className='performers'>
          <TopPerformers />
        </div>
        {projectRole === 'Leader' && (
      <div className="add-item-button">
        <button onClick={toggleForm}>
          <svg xmlns="http://www.w3.org/2000/svg" width="22" height="22" fill="currentColor" class="bi bi-plus-circle-fill" viewBox="0 0 16 16">
            <path d="M16 8A8 8 0 1 1 0 8a8 8 0 0 1 16 0M8.5 4.5a.5.5 0 0 0-1 0v3h-3a.5.5 0 0 0 0 1h3v3a.5.5 0 0 0 1 0v-3h3a.5.5 0 0 0 0-1h-3z"/>
          </svg>
          Add Self-Task
        </button>
      </div>
    )}
    </div>
  );
};

export default UpcomingTasks;
