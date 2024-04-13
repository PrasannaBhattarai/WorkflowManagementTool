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
            <p>{notification.notificationMessage}</p>
            <p>Sender: {notification.senderName}</p>
            <p>Project: {notification.projectName}</p>
          </div>
        ))
      )}
    </div>
  );
};

export default ActiveNotifications;
