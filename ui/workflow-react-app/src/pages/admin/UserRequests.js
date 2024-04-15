import React, { useState, useEffect } from 'react';
import { BrowserRouter as Router, Route, Routes, Navigate } from 'react-router-dom';
import { Link } from 'react-router-dom';

const UserRequests = () => {
    const [activeLink, setActiveLink] = useState('User Requests');


  useEffect(() => {
  }, []);

  const handleLogout = () => {
    //clears cookies and localstorage
    localStorage.clear();
    document.cookie = 'custom_cookie=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;';

};

  return (
    <div className='container-chart'> 
    <div className='navigation-pane'> 
            <center><h2>Admin Panel</h2></center>
            <br></br>
            <div className="navigation-links">
                <Link to = '/chart'><a href="#" className={activeLink === 'Project Analytics' ? 'active' : ''} onClick={() => setActiveLink('Project Analytics')}>Project Analytics</a></Link>
                <Link to = '/user'><a href="#" className={activeLink === 'User Analytics' ? 'active' : ''} onClick={() => setActiveLink('User Analytics')}>User Analytics</a></Link>
                <Link to = '/request'><a href="#" className={activeLink === 'User Requests' ? 'active' : ''} onClick={() => setActiveLink('User Requests')}>User Requests</a></Link>
                <div className="logout-button">
                    <Link to='/login' onClick={handleLogout}>Logout</Link>
                </div>
            </div>
        </div>
        <h1>Hi</h1>
      </div>
  );
};

export default UserRequests;
