import React, { useState, useEffect } from 'react';
import axios from 'axios';
import './../css/AssignTasks.css';

const AssignTasks = () => {
    const [taskDescription, setTaskDescription] = useState('');
    const [showForm, setShowForm] = useState(false);
    const [formData, setFormData] = useState({
        taskDescription: '',
        taskDeadline: '',
        taskPriority: 'Medium', // default priority
        assignedUser: null // default assigned user
    });
    const [assignableUsers, setAssignableUsers] = useState([]);
    const [selectedUsers, setSelectedUsers] = useState([]);

    useEffect(() => {
        const fetchAssignableUsers = async () => {
            try {
                const projectId = new URLSearchParams(window.location.search).get("id");
                const token = localStorage.getItem("token");
                
                const response = await axios.get(`http://localhost:8081/api/tasks/assignableUsers/${projectId}`, {
                    headers: {
                        Authorization: `Bearer ${token}`,
                        'Content-Type': 'application/json'
                    }
                });

                setAssignableUsers(response.data);
            } catch (error) {
                console.error('Error fetching assignable users:', error);
            }
        };

        fetchAssignableUsers();
        const storedTaskDescription = localStorage.getItem('taskDescription');
        if (storedTaskDescription) {
            setTaskDescription(storedTaskDescription);
            setFormData({
                ...formData,
                taskDescription: storedTaskDescription // setting the task description in the form data
            });
            // clear the taskDescription from localStorage after it has been fetched
            localStorage.removeItem('taskDescription');
        }
    }, []);

    const handleFormSubmit = async (event) => {
        event.preventDefault();
        try {
            const projectId = new URLSearchParams(window.location.search).get("id");
            const token = localStorage.getItem("token");
            const taskData = {
                emails: selectedUsers.map(email => ({ email })),
                task: {
                    taskDescription: formData.taskDescription,
                    taskDeadline: formData.taskDeadline,
                    taskPriority: formData.taskPriority
                }
            };
            const response = await axios.post(`http://localhost:8081/api/tasks/assignTasks/${projectId}`, taskData, {
                headers: {
                    Authorization: `Bearer ${token}`,
                    'Content-Type': 'application/json'
                }
            });
            console.log('Task created:', response.data);
            setShowForm(!showForm);
            setFormData({
                taskDescription: '',
                taskDeadline: '',
                taskPriority: 'Medium',
                assignedUser: null
            });
            setSelectedUsers([]);
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

    const handleAssignUser = (email) => {
        setFormData({
            ...formData,
            assignedUser: email
        });
        setSelectedUsers([...selectedUsers, email]);
    };

    const handleCancel = () => {
        setFormData({
            taskDescription: '',
            taskDeadline: '',
            taskPriority: 'Medium',
            assignedUser: null
        });
        setSelectedUsers([]);
    };


    useEffect(() => {
        const storedTaskDescription = localStorage.getItem('taskDescription');
        if (storedTaskDescription) {
          setTaskDescription(storedTaskDescription);
        }
      }, []);

    return (
        <div className="assign-tasks">
            <form onSubmit={handleFormSubmit}>
                <label htmlFor="taskDescription">Task Description:</label>
                <input type="text" id="taskDescription" name="taskDescription" value={formData.taskDescription} onChange={handleInputChange} required/>
                <label htmlFor="taskDeadline">Task Deadline:</label>
                <input type="datetime-local" id="taskDeadline" name="taskDeadline" value={formData.taskDeadline} onChange={handleInputChange} required/>
                <br/>
                <label htmlFor="taskPriority">Task Priority:</label>
                <select id="taskPriority" name="taskPriority" value={formData.taskPriority} onChange={handleInputChange}>
                    <option value="Low">Low</option>
                    <option value="Medium">Medium</option>
                    <option value="High">High</option>
                </select>
                <br/>
                <label>Assign Team Members:</label>
                <div className="assignable-users">
                    <div className="assignable-users-list">
                        {assignableUsers.length === 0 ? (
                            <p>There are no members to assign tasks.</p>
                        ) : (
                            assignableUsers.map(user => (
                                <div key={user.email} className="user-card">
                                    {/* <img src={user.profileImage}/> */}
                                    {user.imageUrl && <img src={user.imageUrl} alt="Profile" width="50" height="50" />}
                                    <span>{user.firstName} {user.lastName}</span>
                                    <span className='ratings'> User Ratings: {user.userRatings}</span>
                                    <button onClick={() => handleAssignUser(user.email)} disabled={formData.assignedUser === user.email}>Assign</button>
                                </div>
                            ))
                        )}
                    </div>
                </div>
                <div className="selected-users">
                    <p>Selected Users:</p>
                        {selectedUsers.map((user, index) => (
                            <span key={index}>{user}</span>
                        ))}
                </div> 
                <br/><br/>
                <center>
                    <button type="submit" className="custom-button">Submit</button>
                </center>
                <br/>
                <center>
                    <button type="button" className="cancel-button" onClick={handleCancel}>Cancel</button>
                </center>
            </form>
        </div>
    );
}

export default AssignTasks;
