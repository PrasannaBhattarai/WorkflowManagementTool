import React, { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

const AuthChecker = () => {
  const history = useNavigate();

  useEffect(() => {
    const token = localStorage.getItem('token');

    if (token) {
      // Token is present, navigate to the home page
      history.push('/home');
    } else {
      // Token is not present, handle accordingly (e.g., redirect to login)
      history.push('/login');
    }
  }, [history]);

  return <div>Checking Authentication...</div>;
};

export default AuthChecker;
