import React, { useState, useEffect } from 'react';
import { Link, useLocation } from 'react-router-dom';
import './css/Invitations.css';

const Invitations = () => {
  const [invitations, setInvitations] = useState([]);

  const fetchInvitations = async () => {
    try {
      const token = localStorage.getItem('token');
      const response = await fetch('http://localhost:8081/api/project/getInvitations', {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      if (response.ok) {
        const data = await response.json();
        setInvitations(data);
      } else {
        console.error('Failed to fetch invitations:', response.statusText);
      }
    } catch (error) {
      console.error('Error fetching invitations:', error);
    }
  };

  useEffect(() => {
    fetchInvitations();
  }, []);

  const handleAccept = async (projectId) => {
    try {
      const response = await fetch(`http://localhost:8081/api/project/acceptInvitation/${projectId}`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${localStorage.getItem('token')}`,
        },
      });
      if (response.ok) {
        console.log('Invitation accepted successfully');
        fetchInvitations();
      } else {
        console.error('Failed to accept invitation:', response.statusText);
      }
    } catch (error) {
      console.error('Error accepting invitation:', error);
    }
  };

  const handleReject = async (projectId) => {
    try {
      const response = await fetch(`http://localhost:8081/api/project/rejectInvitation/${projectId}`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${localStorage.getItem('token')}`,
        },
      });
      if (response.ok) {
        console.log('Invitation rejected successfully');
        fetchInvitations();
      } else {
        console.error('Failed to reject invitation:', response.statusText);
      }
    } catch (error) {
      console.error('Error rejecting invitation:', error);
    }
  };

  return (
    <div className="invitations">
    <h1>Invitations</h1>
    {invitations.length === 0 ? (
      <h3>No invitations</h3>
    ) : (
      invitations.map(invitation => (
        <div key={invitation.projectId} className="invitation-box">
          <p>{invitation.senderName} invited you to join project "{invitation.projectName}"</p>
          <p>User Type: {invitation.userType}</p>
          <p>User Role: {invitation.userRole}</p>
          <div className="button-container">
            <button className='accept' onClick={() => handleAccept(invitation.projectId)}>Accept</button>
            <button className='reject' onClick={() => handleReject(invitation.projectId)}>Reject</button>
          </div>
        </div>
      ))
    )}
  </div>
);
};

export default Invitations;
