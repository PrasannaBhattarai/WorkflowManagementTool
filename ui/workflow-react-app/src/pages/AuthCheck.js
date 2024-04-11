import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';

const AuthChecker = () => {
  const navigate = useNavigate();

  const [loading, setLoading] = useState(true);

useEffect(() => {
  const token = localStorage.getItem('token');

  if (token) {
    navigate('/home');
  } else {
    navigate('/login');
  }

  // After navigation setting loading to false
  setLoading(false);
}, [navigate]);


  return <div>Checking Authentication...</div>;
};

export default AuthChecker;
