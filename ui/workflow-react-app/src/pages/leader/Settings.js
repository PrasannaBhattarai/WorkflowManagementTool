import React, { useState, useEffect } from 'react';
import './../css/Settings.css';
import UserInvitePopup from './UserInvitePopup'; 
import { Link, useLocation } from 'react-router-dom';

const Settings = () => {
    const [formData, setFormData] = useState({
        projectType: 'Group',
        allowGuestAnnouncements: true,
        allowSelfTaskAssignments: false,
    });
    const [users, setUsers] = useState([]);
    const [searchText, setSearchText] = useState('');
    const [selectedUsers, setSelectedUsers] = useState([]);
    const [showPopup, setShowPopup] = useState(false);
    const [selectedUser, setSelectedUser] = useState(null);
    const [submitSuccess, setSubmitSuccess] = useState(false); // State variable to trigger component refresh
    const location = useLocation();

    useEffect(() => {
        if (searchText) {
            fetchUsersByEmail(searchText);
        }
    }, [searchText]);

    useEffect(() => {
        fetchProjectSettings();
    }, [submitSuccess]);

    const fetchUsersByEmail = async (text) => {
        try {
            const response = await fetch(`http://localhost:8081/api/project/search/${text}`, {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${localStorage.getItem('token')}`,
                    'Content-Type': 'application/json'
                }
            });
            if (response.ok) {
                const data = await response.json();
                setUsers(data);
            } else {
                console.error('Failed to fetch users:', response.statusText);
            }
        } catch (error) {
            console.error('Error fetching users:', error);
        }
    };

    const fetchProjectSettings = async () => {
        try { 
            const projectId = new URLSearchParams(location.search).get('id');
            const response = await fetch(`http://localhost:8081/api/project/searchSettings/${projectId}`, {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${localStorage.getItem('token')}`,
                    'Content-Type': 'application/json'
                }
            });
            if (response.ok) {
                const data = await response.json();
                setFormData({
                    projectType: data.groupProject ? 'group' : 'solo',
                    allowGuestAnnouncements: data.allowGuestAnnouncements,
                    allowSelfTaskAssignments: data.allowSelfAssignment,
                });
            } else {
                console.error('Failed to fetch project settings:', response.statusText);
            }
        } catch (error) {
            console.error('Error fetching project settings:', error);
        }
    };

    const handleInputChange = (event) => {
        const { name, value, type, checked } = event.target;
        
        // If projectType is group, prevent changing to solo
        if (name === 'projectType' && value === 'solo' && formData.projectType === 'group') {
            return;
        }
        
        const newValue = type === 'checkbox' ? checked : value;
        
        setFormData({
            ...formData,
            [name]: newValue
        });
    };

    const handleSearch = () => {
        if (searchText.trim() !== '') {
            fetchUsersByEmail(searchText.trim());
        }
    };

    const handleKeyPress = (event) => {
        if (event.key === 'Enter') {
            handleSearch();
        }
    };

    const handleAddUser = (user) => {
        setSelectedUser(user);
        setShowPopup(true);
    };

    const handlePopupClose = () => {
        setShowPopup(false);
        setSelectedUser(null);
    };

    const handleInviteUser = (payload) => {
        setSelectedUsers((prevUsers) => [...prevUsers, payload]);
        setShowPopup(false);
    };
    

    const handleSubmit = async (event) => {
        event.preventDefault();
        try {
            const projectId = new URLSearchParams(location.search).get('id');
            const response = await fetch(`http://localhost:8081/api/project/edit/${projectId}`, {
                method: 'POST',
                headers: {
                    'Authorization': `Bearer ${localStorage.getItem('token')}`,
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    groupProject: formData.projectType === 'group',
                    allowGuestAnnouncements: formData.allowGuestAnnouncements,
                    allowSelfAssignment: formData.allowSelfTaskAssignments,
                    emails: selectedUsers.map(user => ({ userId: user.email, projectId, userType: user.projectRole, projectRole: user.userType }))
                })
            });
            if (response.ok) {
                console.log('Settings saved successfully');
                setSubmitSuccess(true); // Update state variable to trigger component refresh
            } else {
                console.error('Failed to save settings:', response.statusText);
            }
        } catch (error) {
            console.error('Error saving settings:', error);
        }
    };

    return (
        <div className="edit-project">
            <form onSubmit={handleSubmit}>
                <label htmlFor="setProjectType">Set Project Type:</label>
                <select
                    id="setProjectType"
                    name="projectType"
                    value={formData.projectType}
                    onChange={handleInputChange}
                >
                    <option value="group">Group</option>
                    <option value="solo" disabled={formData.projectType === 'group'}>Solo</option>
                </select>
                <br />
                {formData.projectType === 'group' && (
                    <>
                        <label>
                            Allow Guest Announcements?
                            <input
                                type="checkbox"
                                name="allowGuestAnnouncements"
                                checked={formData.allowGuestAnnouncements}
                                onChange={handleInputChange}
                            />
                        </label>
                        <br />
                        <label>
                            Allow Self-Task Assignments?
                            <input
                                type="checkbox"
                                name="allowSelfTaskAssignments"
                                checked={formData.allowSelfTaskAssignments}
                                onChange={handleInputChange}
                            />
                        </label>
                        <br/>
                        <br/>
                        <label>Add Team Members:</label>
                        <div className="assignable-users">
                            <div className="assignable-users-list">
                                <div className="search-bar">
                                    <textarea
                                        placeholder="Search User By Email"
                                        rows="1"
                                        value={searchText}
                                        onChange={(e) => setSearchText(e.target.value)}
                                        onKeyPress={handleKeyPress}
                                    ></textarea>
                                    <svg
                                        xmlns="http://www.w3.org/2000/svg"
                                        width="20"
                                        height="25"
                                        fill="currentColor"
                                        className="bi bi-search"
                                        viewBox="0 0 16 16"
                                        onClick={handleSearch}
                                    >
                                        <path d="M11.742 10.344a6.5 6.5 0 1 0-1.397 1.398h-.001q.044.06.098.115l3.85 3.85a1 1 0 0 0 1.415-1.414l-3.85-3.85a1 1 0 0 0-.115-.1zM12 6.5a5.5 5.5 0 1 1-11 0 5.5 5.5 0 0 1 11 0"/>
                                    </svg>
                                </div>
                                {users.map((user, index) => (
                                    <div className="user-card" key={index}>
                                        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" className="bi bi-person-fill" viewBox="0 0 16 16">
                                            <path d="M3 14s-1 0-1-1 1-4 6-4 6 3 6 4-1 1-1 1zm5-6a3 3 0 1 0 0-6 3 3 0 0 0 0 6"/>
                                        </svg>
                                        <span className='name'>{user.firstName} {user.lastName}</span>
                                        <span className='ratings'> User Ratings: {user.userRatings}</span>
                                        <button onClick={() => handleAddUser(user)}>Add User</button>
                                    </div>
                                ))}
                            </div>
                        </div>
                    </>
                )}
                <br/><br/>
                <center>
                    <button type="submit" className="custom-button">Save Changes</button>
                </center>
                <br/>
                <br/>
                <h2>Terminate Project?</h2>
                <br/>
                <br/>
                <br/>
                <center>
                    <button type="submit" className="custom-button">Close Project</button>
                </center>
            </form>
            {showPopup && (
                <UserInvitePopup
                    userDetails={selectedUser}
                    onCancel={handlePopupClose}
                    onInvite={handleInviteUser}
                />
            )}
        </div>
    );
};

export default Settings;
