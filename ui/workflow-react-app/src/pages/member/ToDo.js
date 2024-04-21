import React, { useState, useEffect } from 'react';
import './../css/ToDo.css';
import TopPerformers from '../TopPerformers.js';
import axios from 'axios';
import { useLocation } from 'react-router-dom';
import ErrorMessagePopup from './../../pages/error/ErrorMessagePopup.js';
import SuccessPopup from '../error/SuccessPopup.js';

const ToDo = () => {
  const [activeToDoLists, setActiveToDoLists] = useState([]);
  const [clickedItems, setClickedItems] = useState([]);
  const [completedToDoLists, setCompletedToDoLists] = useState([]);
  const [showForm, setShowForm] = useState(false);
  const location = useLocation();
  const [description, setDescription] = useState('');
  const [projectRole, setProjectRole] = useState('');
  const [clickedCompletedItems, setClickedCompletedItems] = useState([]);
  const [showSuccess, setShowSuccess] = useState(false);
  const [showError, setShowError] = useState(false);
  const [successMessage, setSuccessMessage] = useState('');
  const [errorMessage, setErrorMessage] = useState('');

  useEffect(() => {
    fetchCompletedToDoLists();
    fetchActiveToDoLists();
    const projectId = new URLSearchParams(location.search).get('id');
    fetchProjectUser(projectId);
  }, []);

  const fetchActiveToDoLists = async () => {
    try {
      const projectId = new URLSearchParams(location.search).get('id');
      const token = localStorage.getItem('token');
      const response = await axios.get(`http://localhost:8081/api/todo/non-active?projectId=${projectId}`,{
      headers:{
        Authorization: `Bearer ${token}`
      }
    });
      setActiveToDoLists(response.data);
    } catch (error) {
      console.error('Error fetching active ToDo lists:', error);
    }
  };

  const handleItemClick = async (toDoListId) => {
    try {
      setClickedItems([...clickedItems, toDoListId]);
      const token = localStorage.getItem('token');
      await axios.put(`http://localhost:8081/api/todo/disable?todoListId=${toDoListId}`, null, {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });
      fetchActiveToDoLists();
      fetchCompletedToDoLists();
    } catch (error) {
      console.error('Error disabling ToDo item:', error);
    }
  };
  

  const fetchCompletedToDoLists = async () => {
    try {
      const projectId = new URLSearchParams(location.search).get('id');
      const token = localStorage.getItem('token');
      const response = await axios.get(`http://localhost:8081/api/todo/active?projectId=${projectId}`, {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });
      setCompletedToDoLists(response.data);
    } catch (error) {
      console.error('Error fetching completed ToDo lists:', error);
    }
  };

  const toggleForm = () => {
    setShowForm(!showForm);
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    try {
      const projectId = new URLSearchParams(location.search).get('id');
      const token = localStorage.getItem('token');
      await axios.post(`http://localhost:8081/api/todo/create?projectId=${projectId}`, {
        toDoItemDescription: description
      }, {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });
      fetchActiveToDoLists();
      setShowForm(false);
      setDescription('');
    } catch (error) {
      console.error('Error creating ToDo item:', error);
    }
  };

  const fetchProjectUser = async (projectId) => {
    try{
      const token = localStorage.getItem("token");
      if(!token){
        console.log('No token for projectUsers');
        return
      }
      const response = await axios.get(`http://localhost:8081/api/project/projectUser/${projectId}`,{
        headers: {
          Authorization: `Bearer ${token}`
        }
    });
      setProjectRole(response.data.projectRole)
    } catch(error){
      console.log(error);
      return null;
    }
  };

  const handleCompletedItemClick = (toDoListId) => {
    console.log('Clicked item ID:', toDoListId);
    setClickedCompletedItems([...clickedCompletedItems, toDoListId]);
  };

  const handleRemove = async (item) => {
    try {
        if (projectRole !== 'Leader' && clickedCompletedItems.includes(item.toDoListId)) {
            setErrorMessage('Only Leader Can Remove To-Do Item!');
            setShowError(true);
            return;
        }
        const projectId = new URLSearchParams(window.location.search).get("id");
        const token = localStorage.getItem('token');
        console.log('Remove item:', item.toDoListId, projectId);
        await axios.delete(`http://localhost:8081/api/todo/project/${projectId}/${item.toDoListId}`, {
            headers: {
                Authorization: `Bearer ${token}`
            }
        });
        fetchActiveToDoLists();
        setSuccessMessage("To-Do Item Removed Successfully!");
        setShowSuccess(true);
        setTimeout(() => {
          window.location.reload();
        }, 2000);
    } catch (error) {
        setErrorMessage("Failed to delete to-do item");
        setShowError(true);
    }
};

  const handleSuccessClose = () => {
    setShowSuccess(false);
  };

  const handleErrorClose = () => {
    setShowError(false);
  };
  
  return (
    <div className='todo'>
      {showForm ? (
        <div className='todoForm'>
          <div className='design'>
          <br/>
          <form onSubmit={handleSubmit}>
          <label>To-Do Description:</label>
          <input type="text" id="todoDescription" value={description} onChange={(e) => setDescription(e.target.value)} required/>
          <br/>
          <br/>
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
    <div className='list'>
      <div className='design'>
        <h3>Group To-Do List:</h3>
        {activeToDoLists.length === 0 ? (
            <div>No To Do List item created</div>
          ) : (
            activeToDoLists.map(item => (
              <div key={item.toDoListId}>
                <label>
                  <input
                    type="checkbox"
                    checked={clickedItems.includes(item.toDoListId)}
                    onChange={() => projectRole === 'Leader' && handleItemClick(item.toDoListId)}
                    disabled={clickedItems.includes(item.toDoListId)}
                  />
                  <span className={clickedItems.includes(item.toDoListId) ? 'pending' : ''}>
                    {item.toDoItemDescription}
                  </span>
                </label>
              </div>
            ))
          )}
          <br/>
        {completedToDoLists.map(item => (
          <div key={item.toDoListId}>
            <label>
              <input
                type="checkbox"
                checked
                disabled
              />
          {projectRole === 'Leader' ? (
            <span className={clickedCompletedItems.includes(item.toDoListId) ? 'completed hovered' : 'completed'} onClick={() => handleRemove(item)}>
              {item.toDoItemDescription}
            </span>
          ) : (
            <span className="completed">
              {item.toDoItemDescription}
            </span>
          )}
            </label>
          </div>
        ))}
        <br/>
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
          Add To-Do Item
        </button>
      </div>
    )}
         {showSuccess && <SuccessPopup message={successMessage} onClose={handleSuccessClose} />}
      {showError && <ErrorMessagePopup message={errorMessage} onClose={handleErrorClose} />}
  </div>
);
};

export default ToDo;
