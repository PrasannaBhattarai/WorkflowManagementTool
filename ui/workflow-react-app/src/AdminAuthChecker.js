import React, { useEffect, useState } from 'react';
import { Navigate } from 'react-router-dom';

const AdminAuthChecker = ({ children }) => {
  const [isAdminAuthenticated, setIsAdminAuthenticated] = useState(false);

  console.log('isAdminAuthenticated Initial:', isAdminAuthenticated);

  useEffect(() => {
    const customCookie = getCookie('custom_cookie');
    console.log('Custom Cookie:', customCookie);
    setIsAdminAuthenticated(!!customCookie); // Set isAdminAuthenticated to true if cookie exists
    console.log('isAdminAuthenticated After Setting:', isAdminAuthenticated);
  }, []);

  // Function to get cookie by name
  const getCookie = (name) => {
    const value = `; ${document.cookie}`;
    const parts = value.split(`; ${name}=`);
    if (parts.length === 2) return parts.pop().split(';').shift();
  };

  return isAdminAuthenticated ? children : <Navigate to="/login" />;
};

export default AdminAuthChecker;
