import React, { useState } from 'react';
import { Navigate, useNavigate } from 'react-router-dom';
import axios from 'axios';
import './css/Login.css';

const Login = ({ setShowAuthAlert, setErrorCode, setIsAdminAuthenticated, setIsAuthenticated }) => {
  const [formData, setFormData] = useState({
    email: '',
    password: '',
  });

  const navigate = useNavigate();

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    let response;

    try {
      // trying regular user authentication
      localStorage.removeItem('token');

      document.cookie = 'custom_cookie=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;';

      response = await axios.post(
        'http://localhost:8081/hello/authenticate',
        formData
      );

      // if regular user authentication succeeds, sets token to localStorage and navigates to home
      const token = response.data.token;
      localStorage.setItem('token', token);
      setIsAuthenticated(true);
      setIsAdminAuthenticated(false); 
      navigate('/home');

    } catch (error) {
      if (error.response && error.response.status === 403) {
        try {
          localStorage.removeItem('token');
          response = await axios.post(
            'http://localhost:8081/hello/authenticateAdmin',
            formData
          );
         
          // if admin authentication succeeds, sets token to cookie and navigates to home
          const token = response.data.token;
          document.cookie = `custom_cookie=${token}; path=/`;
          setIsAdminAuthenticated(true);
          setIsAuthenticated(false);
          navigate('/chart');
        } catch (adminError) {
          // if admin authentication also fails, shows authentication error
          setErrorCode(403);
          setShowAuthAlert(true);
        }
      } else {
        console.error('Error during login:', error);
      }
    }
  };

  return (
    <div className="body">
      <div className="left-container">
        <div className="top-div">
          <h1 className='mainText'>WORKFLOW</h1>
          <h1 className='mainText'>MANAGEMENT</h1>
          <h1 className='mainText'>TOOL</h1>
          <h2 className='supportText'>Track your workflow <br/>efficiently.</h2>
        </div>
        <div className="bottom-div">
          <div className='circle1'></div>
          <div className='circle2'></div>
          <div className='circle3'></div>
        </div>
      </div>
      <div className="right-container">
        <div className="login-form">
          <h2>Login</h2>
          <form onSubmit={handleSubmit}>
            <label>
              Email:
              <input
                type="email"
                name="email"
                value={formData.email}
                onChange={handleChange}
              />
            </label>
            <label>
              Password:
              <input
                type="password"
                name="password"
                value={formData.password}
                onChange={handleChange}
              />
            </label>
            <br />
            <br/>
            <button className="login"  type="submit">LOGIN</button>
            <h5>Don't Have Account? <a href="/register">Sign Up</a></h5>
          </form>
        </div>
      </div>
    </div>
  );
};

export default Login;
