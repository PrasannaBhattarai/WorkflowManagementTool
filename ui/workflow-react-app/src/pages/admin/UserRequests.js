import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import './../css/UserRequests.css';

const UserRequests = () => {
    const [activeLink, setActiveLink] = useState('User Requests');
    const [users, setUsers] = useState([]);

    useEffect(() => {
      fetchUsers();
  }, []);

  const fetchUsers = async () => {
      try {
            const response = await fetch('http://localhost:8081/admin/getUsers',{
              credentials: 'include'
            });
            console.log(response);
            if (response.ok) {
                const userData = await response.json();
                setUsers(userData);
            } else {
                const errorData = await response.json();
                console.error(`Failed to fetch user data: ${response.status} - ${errorData.message}`);
            }
        } catch (error) {
            console.error('Error fetching user data:', error);
        }
    };

    const handleDecline = async (id) => {
      try {
          const response = await fetch(`http://localhost:8081/admin/declineRegister/${id}`, {
              method: 'POST',
              credentials: 'include'
          });
          if (response.ok) {
            const user = users.find(user => user.id === id);
            fetch(`http://127.0.0.1:8000/send-rejection-email/${user.email}/`)
                .then(emailResponse => {
                    if (emailResponse.ok) {
                        console.log('Rejection email sent successfully');
                    } else {
                        console.error('Failed to send rejection email');
                    }
                })
                .catch(error => {
                    console.error('Error sending rejection email:', error);
                });
              fetchUsers();
          } else {
              const errorData = await response.json();
              console.error(`Failed to decline user: ${response.status} - ${errorData.message}`);
          }
      } catch (error) {
          console.error('Error declining user:', error);
      }
    };
  

    const handleLogout = () => {
        //clears cookies and localstorage
        localStorage.clear();
        document.cookie = 'custom_cookie=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;';
    };

    const handleApprove = async (id, callback) => {
      try {
          const response = await fetch(`http://localhost:8081/admin/userRegister/${id}`, {
              method: 'POST',
              credentials: 'include'
          });
          if (response.ok) {
            const user = users.find(user => user.id === id);
            const emailResponse = await fetch(`http://127.0.0.1:8000/send-email/${user.email}/`);
            if (emailResponse.ok) {
                console.log('Approval email sent successfully');
            } else {
                console.error('Failed to send approval email');
            }

              //executing the callback to refresh the users data
              fetchUsers();
          } else {
              const errorData = await response.json();
              console.error(`Failed to approve user: ${response.status} - ${errorData.message}`);
          }
      } catch (error) {
          console.error('Error approving user:', error);
      }
  };

    return (
        <div className='container-chart'>
            <div className='navigation-pane'> 
                <center>
                    <h2>
                    <div className='main-svg'>
                        <svg xmlns="http://www.w3.org/2000/svg" width="26" height="26" fill="currentColor" class="bi bi-clipboard-data" viewBox="0 0 16 16">
                            <path d="M4 11a1 1 0 1 1 2 0v1a1 1 0 1 1-2 0zm6-4a1 1 0 1 1 2 0v5a1 1 0 1 1-2 0zM7 9a1 1 0 0 1 2 0v3a1 1 0 1 1-2 0z"/>
                            <path d="M4 1.5H3a2 2 0 0 0-2 2V14a2 2 0 0 0 2 2h10a2 2 0 0 0 2-2V3.5a2 2 0 0 0-2-2h-1v1h1a1 1 0 0 1 1 1V14a1 1 0 0 1-1 1H3a1 1 0 0 1-1-1V3.5a1 1 0 0 1 1-1h1z"/>
                            <path d="M9.5 1a.5.5 0 0 1 .5.5v1a.5.5 0 0 1-.5.5h-3a.5.5 0 0 1-.5-.5v-1a.5.5 0 0 1 .5-.5zm-3-1A1.5 1.5 0 0 0 5 1.5v1A1.5 1.5 0 0 0 6.5 4h3A1.5 1.5 0 0 0 11 2.5v-1A1.5 1.5 0 0 0 9.5 0z"/>
                        </svg>
                    </div>
                    Admin Panel</h2></center>
                <br></br>
                <div className="navigation-links">
                    <Link to = '/chart'><a href="#" className={activeLink === 'Project Analytics' ? 'active' : ''} onClick={() => setActiveLink('Project Analytics')}>
                    <svg xmlns="http://www.w3.org/2000/svg" width="26" height="26" fill="currentColor" class="bi bi-graph-up" viewBox="0 0 16 16">
                        <path fill-rule="evenodd" d="M0 0h1v15h15v1H0zm14.817 3.113a.5.5 0 0 1 .07.704l-4.5 5.5a.5.5 0 0 1-.74.037L7.06 6.767l-3.656 5.027a.5.5 0 0 1-.808-.588l4-5.5a.5.5 0 0 1 .758-.06l2.609 2.61 4.15-5.073a.5.5 0 0 1 .704-.07"/>
                    </svg>
                        Project Analytics
                    </a></Link>
                    <Link to = '/user'><a href="#" className={activeLink === 'User Analytics' ? 'active' : ''} onClick={() => setActiveLink('User Analytics')}>
                    <svg xmlns="http://www.w3.org/2000/svg" width="26" height="26" fill="currentColor" class="bi bi-bar-chart-fill" viewBox="0 0 16 16">
                        <path d="M1 11a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1v3a1 1 0 0 1-1 1H2a1 1 0 0 1-1-1zm5-4a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1v7a1 1 0 0 1-1 1H7a1 1 0 0 1-1-1zm5-5a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1v12a1 1 0 0 1-1 1h-2a1 1 0 0 1-1-1z"/>
                    </svg>
                        User Analytics
                        </a></Link>
                    <Link to = '/request'><a href="#" className={activeLink === 'User Requests' ? 'active' : ''} onClick={() => setActiveLink('User Requests')}>
                    <svg xmlns="http://www.w3.org/2000/svg" width="26" height="26" fill="currentColor" class="bi bi-person-fill-add" viewBox="0 0 16 16">
                        <path d="M12.5 16a3.5 3.5 0 1 0 0-7 3.5 3.5 0 0 0 0 7m.5-5v1h1a.5.5 0 0 1 0 1h-1v1a.5.5 0 0 1-1 0v-1h-1a.5.5 0 0 1 0-1h1v-1a.5.5 0 0 1 1 0m-2-6a3 3 0 1 1-6 0 3 3 0 0 1 6 0"/>
                        <path d="M2 13c0 1 1 1 1 1h5.256A4.5 4.5 0 0 1 8 12.5a4.5 4.5 0 0 1 1.544-3.393Q8.844 9.002 8 9c-5 0-6 3-6 4"/>
                    </svg>
                        User Requests</a>
                        </Link>
                    <div className="logout-button">
                        <Link to='/login' onClick={handleLogout}>
                        <svg xmlns="http://www.w3.org/2000/svg" width="26" height="26" fill="currentColor" class="bi bi-box-arrow-left" viewBox="0 0 16 16">
                            <path fill-rule="evenodd" d="M6 12.5a.5.5 0 0 0 .5.5h8a.5.5 0 0 0 .5-.5v-9a.5.5 0 0 0-.5-.5h-8a.5.5 0 0 0-.5.5v2a.5.5 0 0 1-1 0v-2A1.5 1.5 0 0 1 6.5 2h8A1.5 1.5 0 0 1 16 3.5v9a1.5 1.5 0 0 1-1.5 1.5h-8A1.5 1.5 0 0 1 5 12.5v-2a.5.5 0 0 1 1 0z"/>
                            <path fill-rule="evenodd" d="M.146 8.354a.5.5 0 0 1 0-.708l3-3a.5.5 0 1 1 .708.708L1.707 7.5H10.5a.5.5 0 0 1 0 1H1.707l2.147 2.146a.5.5 0 0 1-.708.708z"/>
                        </svg>
                            Logout</Link>
                    </div>
                </div>
            </div>
            <div className="users-container">
              <h1><center>Registration Requests</center></h1>
              <div className="users-list">
                {users.length === 0 ? (
                    <h2 className='no-req'>
                      <hr className='req-line'/>No users requests..</h2>
                ) : (
                    users.map(user => (
                        <div className="users-card" key={user.id}>
                            <div className="user-details">
                                <div className="user-images">
                                    <img src={user.imageUrl ? `/images/${user.imageUrl}.jpg` : '/images/default.jpg'} alt="" />
                                </div>
                                <div className='user-text'>
                                    <div className="name-row">
                                        <h3>First Name: {user.firstName}</h3>
                                        <h3>Email: {user.email}</h3>
                                    </div>
                                    <div className="info-row">
                                        <h3>Last Name: {user.lastName}</h3>
                                        <h3>Username: {user.userName}</h3>
                                    </div>
                                </div>
                            </div>
                              <div className="users-action-buttons">
                                  <button className="approve-button" onClick={() => handleApprove(user.id)}>Approve</button>
                                  <button className="decline-button" onClick={() => handleDecline(user.id)}>Decline</button>
                              </div>
                          </div>
                      ))
                  )}
              </div>
          </div>

        </div>
    );
};

export default UserRequests;
