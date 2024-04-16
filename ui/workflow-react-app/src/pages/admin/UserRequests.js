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
                <center><h2>Admin Panel</h2></center>
                <br></br>
                <div className="navigation-links">
                    <Link to='/chart'><a href="#" className={activeLink === 'Project Analytics' ? 'active' : ''} onClick={() => setActiveLink('Project Analytics')}>Project Analytics</a></Link>
                    <Link to='/user'><a href="#" className={activeLink === 'User Analytics' ? 'active' : ''} onClick={() => setActiveLink('User Analytics')}>User Analytics</a></Link>
                    <Link to='/request'><a href="#" className={activeLink === 'User Requests' ? 'active' : ''} onClick={() => setActiveLink('User Requests')}>User Requests</a></Link>
                    <div className="logout-button">
                        <Link to='/login' onClick={handleLogout}>Logout</Link>
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
