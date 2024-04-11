import React, { useState } from 'react';
import './../css/UserInvitePopup.css';

const UserInvitePopup = ({ userDetails, onCancel, onInvite }) => {
    const [userType, setUserType] = useState('Guest');
    const [role, setRole] = useState('');

    const handleInvite = () => {
        // Call the onInvite function with the user details and selected userType and role
        onInvite({ ...userDetails, userType, projectRole: role });
    };

    return (
        <div className="popup-wrapper">
            <div className="popup">
                <h2>User Details</h2>
                <p>User Name: {userDetails.firstName} {userDetails.lastName}</p>
                <p>Overall Ratings: {userDetails.userRatings} <span className='star'>*</span></p>
                <label htmlFor="userType">User Type:</label>
                <select id="userType" value={userType} onChange={(e) => setUserType(e.target.value)}>
                    <option value="Guest">Guest</option>
                    <option value="Member">Member</option>
                    <option value="Leader">Leader</option>
                </select>
                <label htmlFor="role">Role:</label>
                <input type="text" id="role" value={role} onChange={(e) => setRole(e.target.value)} />
                <div className="button-group">
                    <center>
                        <button className='invite-button' onClick={handleInvite}>Invite</button>
                        <button className='cancel-button' onClick={onCancel}>Cancel</button>
                    </center>
                </div>
            </div>
        </div>
    );
};

export default UserInvitePopup;
