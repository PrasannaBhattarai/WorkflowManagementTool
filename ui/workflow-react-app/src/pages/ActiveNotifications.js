import React, { useState, useEffect } from 'react';
import { Link, useLocation } from 'react-router-dom';
import './css/Invitations.css';

const ActiveNotifications = () => {
  const [notifications, setNotifications] = useState([]);

  useEffect(() => {
    const fetchNotifications = async () => {
      try {
        const token = localStorage.getItem('token'); // Assuming you have stored token in localStorage
        const response = await fetch('http://localhost:8081/api/project/getNotifications', {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        if (response.ok) {
          const data = await response.json();
          setNotifications(data);
        } else {
          console.error('Failed to fetch notifications:', response.statusText);
        }
      } catch (error) {
        console.error('Error fetching notifications:', error);
      }
    };

    fetchNotifications();
  }, []);

  return (
    <div className="invitations">
      <h1>Notifications</h1>
      {notifications.length === 0 ? (
        <h3>No Notifications</h3>
      ) : (
        notifications.map(notification => (
          <div key={notification.notificationId} className="notification-box">
            <div>
              <svg xmlns="http://www.w3.org/2000/svg" width="26" height="26" fill="currentColor" class="bi bi-exclamation-diamond-fill" viewBox="0 0 16 16">
                <path d="M9.05.435c-.58-.58-1.52-.58-2.1 0L.436 6.95c-.58.58-.58 1.519 0 2.098l6.516 6.516c.58.58 1.519.58 2.098 0l6.516-6.516c.58-.58.58-1.519 0-2.098zM8 4c.535 0 .954.462.9.995l-.35 3.507a.552.552 0 0 1-1.1 0L7.1 4.995A.905.905 0 0 1 8 4m.002 6a1 1 0 1 1 0 2 1 1 0 0 1 0-2"/>
              </svg>
            </div>
            <div className='info'>
              <p>{notification.notificationMessage}</p>
              <p>Sender: {notification.senderName}</p>
              <p>Project: {notification.projectName}</p>
            </div>
          </div>
        ))
      )}
    </div>
  );
};

export default ActiveNotifications;
