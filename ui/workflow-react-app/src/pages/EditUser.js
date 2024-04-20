import React, { useState, useEffect } from 'react';
import { Link, useLocation } from 'react-router-dom';
import axios from 'axios';
import './css/EditUser.css';
import './css/Home.css';
import ErrorMessagePopup from './error/ErrorMessagePopup';

const EditUser = () => {
  const location = useLocation();
  const [selectedImage, setSelectedImage] = useState(null);
  const [projects, setProjects] = useState([]);
  const [showProjects, setShowProjects] = useState(false);
  const [user, setUser] = useState({ firstName: '', lastName: '', imageUrl: '' });
  const [notificationNumber, setNotificationNumber] = useState(0);
  const [imagePreview, setImagePreview] = useState('');
  const [editImagePreview, setEditImagePreview] = useState('');
  const [firstName, setFirstName] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [errorMessage, setErrorMessage] = useState(null);

  useEffect(() => {
    const fetchProjects = async () => {
      try {
        const token = localStorage.getItem('token');

        if (!token) {
          console.log('No token found, unable to fetch projects.');
          return;
        }

        const response = await axios.get('http://localhost:8081/api/project/projects', {
          headers: {
            Authorization: `Bearer ${token}`
          }
        });
        setProjects(response.data);
      } catch (error) {
        setErrorMessage('Error fetching projects: ' + error.message);
      }
    };

    const fetchNotificationNumber = async () => {
      try {
        const token = localStorage.getItem('token');
        if (!token) {
          console.log('No token found, unable to fetch notification number.');
          return;
        }
        const response = await axios.get('http://localhost:8081/api/project/getNotificationNumber', {
          headers: {
            Authorization: `Bearer ${token}`
          }
        });
        setNotificationNumber(response.data);
      } catch (error) {
        setErrorMessage('Error fetching notification number: ' + error.message);
      }
    };

    const fetchUser = async () => {
      try {
        const token = localStorage.getItem('token');

        if (!token) {
          console.log('No token found, unable to fetch user.');
          return;
        }

        const response = await axios.get('http://localhost:8081/api/user/userDetails', {
          headers: {
            Authorization: `Bearer ${token}`
          }
        });
        setUser(response.data); // Set user data
        setFirstName(response.data.firstName);
        setImagePreview(`http://localhost:8081/${response.data.imageUrl}`);
        setEditImagePreview(`http://localhost:8081/${response.data.imageUrl}`);
      } catch (error) {
        console.error('Error fetching user:', error);
      }
    };

    fetchProjects();
    fetchUser();
    fetchNotificationNumber();
  }, []);

  const handleProjectClick = () => {
    setShowProjects(!showProjects); // toggling showProjects
  };

  const handleLogout = () => {
    localStorage.removeItem('token');
    window.location.href = '/login';
  };

    const handleSubmit = async (event) => {
    event.preventDefault();
    try {
      const token = localStorage.getItem('token');
      if (!token) {
        console.log('No token found, unable to submit form.');
        return;
      }
  
      // validates first name and last name
      if (!event.target.firstName.value.trim() || !event.target.lastName.value.trim()) {
        console.log('First name and last name cannot be empty.');
        return;
      }
  
      const formData = new FormData();
  
      // checks if image is selected and different from the current one
      if (selectedImage && selectedImage !== user.imageUrl) {
        formData.append('image', selectedImage); 
      }
  
      // makes API call to upload user data with image if image is selected
      if (formData.has('image')) {
        await axios.post('http://localhost:8081/api/user/editUser', formData, {
          headers: {
            Authorization: `Bearer ${token}`,
            'Content-Type': 'multipart/form-data',
          },
        });
      }
  
      // update user details only if provided
      const userDetails = {};
      if (event.target.firstName.value !== user.firstName) {
        userDetails.firstName = event.target.firstName.value;
      }
      if (event.target.lastName.value !== user.lastName) {
        userDetails.lastName = event.target.lastName.value;
      }
      const password = event.target.password.value;
      if (password) {
        if (password === confirmPassword) {
          userDetails.password = password;
        } else {
          setErrorMessage('Error! Passwords Mismatch!');
          return;
        }
      }
  
      // makes API call to update user details if there are changes
      if (Object.keys(userDetails).length > 0) {
        await axios.post('http://localhost:8081/api/user/editUserDetails', userDetails, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
      }
  
      // refreshes component
      window.location.reload();
    } catch (error) {
      setErrorMessage('Error submitting form: ' + error.message);
    }
  };
  

  const handleImageChange = event => {
    const file = event.target.files[0];
    setSelectedImage(file);
    const reader = new FileReader();

    reader.onloadend = () => {
      setEditImagePreview(reader.result);
      // setImagePreview(reader.result);
    };

    if (file) {
      reader.readAsDataURL(file);
    }
  };

  return (
    <div className="container">
      <div className="nav-panel">
        <div className="logo">
          <span>WORKFLOW MANAGEMENT TOOL</span>
        </div>
        <ul>
          <li>
            <Link to="/home">
              <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" fill="currentColor" className="bi bi-plus-circle" viewBox="0 0 16 16">
                <path d="M8 15A7 7 0 1 1 8 1a7 7 0 0 1 0 14m0 1A8 8 0 1 0 8 0a8 8 0 0 0 0 16"/>
                <path d="M8 4a.5.5 0 0 1 .5.5v3h3a.5.5 0 0 1 0 1h-3v3a.5.5 0 0 1-1 0v-3h-3a.5.5 0 0 1 0-1h3v-3A.5.5 0 0 1 8 4"/>
              </svg>
              Create a Project
            </Link>
          </li>

          <li className={location.pathname === '/edit-user-details' ? 'active' : ''}>
            <Link to="/edit-user-details">
              <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" fill="currentColor" className="bi bi-wrench-adjustable" viewBox="0 0 16 16">
                <path d="M16 4.5a4.5 4.5 0 0 1-1.703 3.526L13 5l2.959-1.11q.04.3.041.61"/>
                <path d="M11.5 9c.653 0 1.273-.139 1.833-.39L12 5.5 11 3l3.826-1.53A4.5 4.5 0 0 0 7.29 6.092l-6.116 5.096a2.583 2.583 0 1 0 3.638 3.638L9.908 8.71A4.5 4.5 0 0 0 11.5 9m-1.292-4.361-.596.893.809-.27a.25.25 0 0 1 .287.377l-.596.893.809-.27.158.475-1.5.5a.25.25 0 0 1-.287-.376l.596-.893-.809.27a.25.25 0 0 1-.287-.377l.596-.893-.809.27-.158-.475 1.5-.5a.25.25 0 0 1 .287.376M3 14a1 1 0 1 1 0-2 1 1 0 0 1 0 2"/>
                <path d="M.146 8.354a.5.5 0 0 1 0-.708l3-3a.5.5 0 1 1 .708.708L1.707 7.5H10.5a.5.5 0 0 1 0 1H1.707l2.147 2.146a.5.5 0 0 1-.708.708z"/>
              </svg>
              Edit User Details
            </Link>
          </li>
          <li>
            <span onClick={handleProjectClick}>
              <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" fill="currentColor" className="bi bi-folder" viewBox="0 0 16 16">
                <path d="M.54 3.87.5 3a2 2 0 0 1 2-2h3.672a2 2 0 0 1 1.414.586l.828.828A2 2 0 0 0 9.828 3h3.982a2 2 0 0 1 1.992 2.181l-.637 7A2 2 0 0 1 13.174 14H2.826a2 2 0 0 1-1.991-1.819l-.637-7a2 2 0 0 1 .342-1.31zM2.19 4a1 1 0 0 0-.996 1.09l.637 7a1 1 0 0 0 .995.91h10.348a1 1 0 0 0 .995-.91l.637-7A1 1 0 0 0 13.81 4zm4.69-1.707A1 1 0 0 0 6.172 2H2.5a1 1 0 0 0-1 .981l.006.139q.323-.119.684-.12h5.396z"/>
              </svg>
              Open Projects
            </span>
            {showProjects && (
              <ul className="sub-menu">
                {projects.map(project => (
                  <li key={project.projectId}>
                    <Link to={`/project?id=${project.projectId}`} onClick={() => handleProjectClick(project.projectId)}>
                      {project.projectName}
                    </Link>
                  </li>
                ))}
              </ul>
            )}
          </li>
        </ul>
        <Link to="/" className="logout" onClick={handleLogout}>
          <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" fill="currentColor" className="bi bi-box-arrow-left" viewBox="0 0 16 16">
            <path fillRule="evenodd" d="M6 12.5a.5.5 0 0 0 .5.5h8a.5.5 0 0 0 .5-.5v-9a.5.5 0 0 0-.5-.5h-8a.5.5 0 0 0-.5.5v2a.5.5 0 0 1-1 0v-2A1.5 1.5 0 0 1 6.5 2h8A1.5 1.5 0 0 1 16 3.5v9a1.5 1.5 0 0 1-1.5 1.5h-8A1.5 1.5 0 0 1 5 12.5v-2a.5.5 0 0 1 1 0z"/>
            <path fillRule="evenodd" d="M.146 8.354a.5.5 0 0 1 0-.708l3-3a.5.5 0 1 1 .708.708L1.707 7.5H10.5a.5.5 0 0 1 0 1H1.707l2.147 2.146a.5.5 0 0 1-.708.708z"/>
          </svg>
          Log Out
        </Link>
      </div>
      <div className="content">
        <div className="greeting">
          <div className='person'>
            <img src={imagePreview} className="user-image" alt="User"/> 
          </div>
          Name: {user.firstName} {user.lastName}  
          <Link to="/notifications">
            <div className='number'>
              <svg xmlns="http://www.w3.org/2000/svg" width="30" height="30" className="bi bi-bell" viewBox="0 0 16 16">
                <path d="M8 16a2 2 0 0 0 2-2H6a2 2 0 0 0 2 2M8 1.918l-.797.161A4 4 0 0 0 4 6c0 .628-.134 2.197-.459 3.742-.16.767-.376 1.566-.663 2.258h10.244c-.287-.692-.502-1.49-.663-2.258C12.134 8.197 12 6.628 12 6a4 4 0 0 0-3.203-3.92zM14.22 12c.223.447.481.801.78 1H1c.299-.199.557-.553.78-1C2.68 10.2 3 6.88 3 6c0-2.42 1.72-4.44 4.005-4.901a1 1 0 1 1 1.99 0A5 5 0 0 1 13 6c0 .88.32 4.2 1.22 6"/>
              </svg>
            </div>
            <div className='noti'>
              <span className="notification-badge">{notificationNumber}</span>
            </div>
          </Link>
        </div>
        <div className='page'>
          <div className='edit-user'>
            <form className='edit-form' onSubmit={handleSubmit} encType="multipart/form-data">
              <div>
                  <div className='images-stack'>
              <label htmlFor="imageInput">
                <img src={editImagePreview} className="edit-user-image" />
                <div className="overlay">
                      <span>Change image</span>
                    </div>
                  </label>
                  <input 
                    type="file" 
                    id="imageInput" 
                    name="image" 
                    className='edit-image' 
                    onChange={handleImageChange}
                    style={{ display: 'none' }}
                  />
                </div>
                <div className='left'>
                  <label htmlFor="firstName" children className='fname-label'>First Name:</label>
                  <input value={firstName} type="text" id="firstName" name="firstName" className='fname' onChange={(e) => setFirstName(e.target.value)} required/><br/>

                  <label htmlFor="password">Password:</label>
                  <input type="password" id="password" name="password" className='password'/><br/>
                </div>
               
                <div className='right'>
                  <label htmlFor="lastName" className='lname-label'>Last Name:</label>
                  <input type="text" id="lastName" name="lastName" className='lname' value={user.lastName}  onChange={(e) => setUser({ ...user, lastName: e.target.value })} required/><br/>

                  <label htmlFor="confirmPassword">Confirm Password:</label>
                  <input type="password" id="confirmPassword" name="confirmPassword" className='confirm' onChange={(e) => setConfirmPassword(e.target.value)} /><br/>
                </div>         
              </div>   
              <input type="submit" value="Submit" className='btn-submit'/>
            </form>
          </div>
        </div>
      </div>
      <br/><br/>
      {errorMessage && (
    <ErrorMessagePopup message={errorMessage} onClose={() => setErrorMessage(null)} />
  )}
    </div>
  );
};

export default EditUser;
