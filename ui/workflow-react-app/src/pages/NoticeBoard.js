import React, { useState, useEffect } from 'react';
import axios from 'axios';
import './../pages/css/NoticeBoard.css';

const NoticeBoard = () => {
  const [projectRole, setProjectRole] = useState('');
  const [announcements, setAnnouncements] = useState([]);
  const [editedAnnouncementId, setEditedAnnouncementId] = useState(null); // Track the announcement being edited
  const [editedAnnouncementText, setEditedAnnouncementText] = useState('');
  const [newAnnouncementText, setNewAnnouncementText] = useState('');
  const [isAddingAnnouncement, setIsAddingAnnouncement] = useState(false);

  const fetchAnnouncements = async () => {
    try {
      const projectId = new URLSearchParams(window.location.search).get("id");
      const token = localStorage.getItem("token");
      if (!token) {
        console.log('No token for announcements');
        return;
      }
      const response = await axios.get(`http://localhost:8081/api/project/getAnnouncements/${projectId}`, {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });
      setAnnouncements(response.data);
    } catch (error) {
      console.log(error);
    }
  };

  useEffect(() => {
    const fetchProjectUser = async () => {
      try {
        const projectId = new URLSearchParams(window.location.search).get("id");
        const token = localStorage.getItem("token");
        if (!token) {
          console.log('No token for projectUsers');
          return;
        }
        const response = await axios.get(`http://localhost:8081/api/project/projectUser/${projectId}`, {
          headers: {
            Authorization: `Bearer ${token}`
          }
        });
        setProjectRole(response.data.projectRole);
      } catch (error) {
        console.log(error);
      }
    };

    fetchProjectUser();
    fetchAnnouncements(); // Fetch announcements when component mounts
  }, []);

  const handleEdit = (announcementId, currentAnnouncement) => {
    // Set the edited announcement ID and text for the announcement being edited
    setEditedAnnouncementId(announcementId);
    setEditedAnnouncementText(currentAnnouncement);
  };

  const handleSave = async () => {
    try {
      const token = localStorage.getItem("token");
      if (!token) {
        console.log('No token for editing announcement');
        return;
      }
      await axios.put(`http://localhost:8081/api/project/edit-announcement/${editedAnnouncementId}`, {
        announcement: editedAnnouncementText // Send updated announcement text
      }, {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });
      // Refresh component after successful update
      fetchAnnouncements();
      // Reset edited announcement state
      setEditedAnnouncementId(null);
      setEditedAnnouncementText('');
    } catch (error) {
      console.log(error);
    }
  };

  const handleCancel = () => {
    // Reset edited announcement state when canceling
    setEditedAnnouncementId(null);
    setEditedAnnouncementText('');
  };

  const handleDelete = async (announcementId) => {
    try {
      const token = localStorage.getItem("token");
      if (!token) {
        console.log('No token for deleting announcement');
        return;
      }
      await axios.delete(`http://localhost:8081/api/project/delete-announcement/${announcementId}`, {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });
      // Refresh component after successful delete
      fetchAnnouncements();
    } catch (error) {
      console.log(error);
    }
  };

  const handleAddAnnouncement = async () => {
    setIsAddingAnnouncement(true);
  };

  const handleSaveNewAnnouncement = async () => {
    try {
      const token = localStorage.getItem("token");
      if (!token) {
        console.log('No token for creating announcement');
        return;
      }
      const projectId = new URLSearchParams(window.location.search).get("id");
      await axios.post(`http://localhost:8081/api/project/announcement/${projectId}`, {
        announcement: newAnnouncementText
      }, {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });
      setIsAddingAnnouncement(false);
      setNewAnnouncementText('');
      fetchAnnouncements();
    } catch (error) {
      console.log(error);
    }
  };

  const handleCancelNewAnnouncement = () => {
    setIsAddingAnnouncement(false);
    setNewAnnouncementText('');
  };

  return (
    <div className="notice-board-container">
      <h2>Notice Board</h2>
      {projectRole === 'Leader' && <p className="hi-leader">Hi Leader!</p>}
      {isAddingAnnouncement && (
        <div className="new-announcement-container">
          <textarea
            placeholder="Enter your announcement..."
            value={newAnnouncementText}
            onChange={(e) => setNewAnnouncementText(e.target.value)}
          />
          <div className="button-container">
            <button className="custom-save-button" onClick={handleSaveNewAnnouncement}>Save</button>
            <button className="custom-cancel-button" onClick={handleCancelNewAnnouncement}>Cancel</button>
          </div>
        </div>
      )}
      {!isAddingAnnouncement && (
        <div className="add-announcement-button-container">
          {projectRole === 'Leader' && (
            <button className="add-announcement-button" onClick={handleAddAnnouncement}>Add Announcement</button>
          )}
        </div>
      )}
      {announcements.map((announcement) => (
        <div key={announcement.announcementId} className="announcement-card">
          <div>
            <h3>{announcement.senderName} ({announcement.senderRole})</h3>
            <p>{announcement.announcement}</p>
            <p>{announcement.announcedAt}</p>
          </div>
          {editedAnnouncementId === announcement.announcementId ? (
            <div className='main-container'>
              <textarea
                value={editedAnnouncementText}
                onChange={(e) => setEditedAnnouncementText(e.target.value)}
              />
              <div className="button-container">
                <button className="custom-save-button" onClick={handleSave}>Save</button>
                <button className="custom-cancel-button" onClick={handleCancel}>Cancel</button>
              </div>
            </div>
          ) : (
            <div className='edit-button-container'>
              {projectRole === 'Leader' && (
                <div className='button-container'>
                  <button className="custom-edit-button" onClick={() => handleEdit(announcement.announcementId, announcement.announcement)}>
                    Edit
                  </button>
                  <button className="custom-delete-button" onClick={() => handleDelete(announcement.announcementId)}>
                    Delete
                  </button>
                </div>
              )}
            </div>
          )}
        </div>
      ))}
    </div>
  );
};

export default NoticeBoard;
