import React, { useState, useEffect } from 'react';
import axios from 'axios';
import './../pages/css/NoticeBoard.css';
import ErrorMessagePopup from './error/ErrorMessagePopup';
import WarningPopup from './error/WarningPopup';
import SuccessPopup from './error/SuccessPopup';

const NoticeBoard = () => {
  const [projectRole, setProjectRole] = useState('');
  const [announcements, setAnnouncements] = useState([]);
  const [editedAnnouncementId, setEditedAnnouncementId] = useState(null); // tracks the announcement being edited
  const [editedAnnouncementText, setEditedAnnouncementText] = useState('');
  const [newAnnouncementText, setNewAnnouncementText] = useState('');
  const [isAddingAnnouncement, setIsAddingAnnouncement] = useState(false);
  const [showError, setShowError] = useState(false);
  const [errorMessage, setErrorMessage] = useState('');
  const [showWarning, setShowWarning] = useState(false);
  const [warningMessage, setWarningMessage] = useState('');
  const [showSuccess, setShowSuccess] = useState(false);
  const [successMessage, setSuccessMessage] = useState('');
  const [searchSettings, setSearchSettings] = useState({});

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

    const fetchSettings = async () => {
      try {
        const projectId = new URLSearchParams(window.location.search).get("id");
        const token = localStorage.getItem("token");
        if (!token) {
          console.log('No token for search settings');
          return;
        }
        const response = await axios.get(`http://localhost:8081/api/project/searchSettings/${projectId}`, {
          headers: {
            Authorization: `Bearer ${token}`
          }
        });
        setSearchSettings(response.data);
      } catch (error) {
        console.log(error);
      }
    };

    fetchSettings(); 
    fetchProjectUser();
    fetchAnnouncements(); // fetches announcements when component mounts
  }, []);

  const handleEdit = (announcementId, currentAnnouncement) => {
    // sets the edited announcement ID and text for the announcement being edited
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
        announcement: editedAnnouncementText // sends updated announcement text
      }, {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });
      // refreshes component after successful update
      fetchAnnouncements();
      // resets edited announcement state
      setEditedAnnouncementId(null);
      setEditedAnnouncementText('');
      setShowWarning(true);
    } catch (error) {
      console.log(error);
    }
  };

  const handleCancel = () => {
    // resets edited announcement state when canceling
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
      // refreshes component after successful delete
      fetchAnnouncements();
      setErrorMessage('Announcement Deleted Successfully!');
      setShowError(true);
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
      setSuccessMessage('Announcement added successfully');
      setShowSuccess(true);
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

  const handleErrorClose = () => {
    setShowError(false);
  };

  const handleWarningClose = () => {
    setShowWarning(false);
  };

  const handleSuccessClose = () => {
    setShowSuccess(false);
  };

  return (
    <div className="notice-board-container">
      <h2>Notice Board</h2>
      {projectRole === 'Leader' && <p className="hi-leader">Hello Leader!</p>}
      {projectRole === 'Guest' && <p className="hi-guest">Hello Guest!</p>}
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
        {(projectRole === 'Leader' || (projectRole === 'Guest' && searchSettings.allowGuestAnnouncements)) && (
          <button className="add-announcement-button" onClick={handleAddAnnouncement}>Add Announcement</button>
        )}
      </div>
    )}

      {announcements.map((announcement) => (
        <div key={announcement.announcementId} className="announcement-card">
          <div>
          <svg xmlns="http://www.w3.org/2000/svg" width="26" height="26" fill="currentColor" class="bi bi-envelope" viewBox="0 0 16 16">
            <path d="M0 4a2 2 0 0 1 2-2h12a2 2 0 0 1 2 2v8a2 2 0 0 1-2 2H2a2 2 0 0 1-2-2zm2-1a1 1 0 0 0-1 1v.217l7 4.2 7-4.2V4a1 1 0 0 0-1-1zm13 2.383-4.708 2.825L15 11.105zm-.034 6.876-5.64-3.471L8 9.583l-1.326-.795-5.64 3.47A1 1 0 0 0 2 13h12a1 1 0 0 0 .966-.741M1 11.105l4.708-2.897L1 5.383z"/>
          </svg>
            <h3>{announcement.senderName} ({announcement.senderRole})</h3>
            <p>{announcement.announcement}</p>
            <p className='datetime'>{announcement.announcedAt}</p>
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
      {showError && <ErrorMessagePopup message={errorMessage} onClose={handleErrorClose} />}
      {showWarning && (
          <WarningPopup
            message="Edited Announcement Successfully!"
            onClose={handleWarningClose}
          />
        )}
        {showSuccess && <SuccessPopup message={successMessage} onClose={handleSuccessClose} />}
    </div>
  );
};

export default NoticeBoard;
