import React, { useState } from 'react';
import { Navigate, useNavigate, Link } from 'react-router-dom';
import axios from 'axios';
import './css/Login.css';


const Login = ({ setShowAuthAlert, setErrorCode }) => {
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

    try {
        const response = await axios.post(
            'http://localhost:8081/hello/authenticate',
            formData
        );

        const token = response.data.token;

        localStorage.setItem('token', token);

        navigate('/home');

    } catch (error) {
      if (error.response && error.response.status === 403) {
        setErrorCode(403);
        setShowAuthAlert(true);
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
            <button type="submit">LOGIN</button>
            <h5>Don't Have Account? <a href="/register">Sign Up</a></h5>
          </form>
        </div>
      </div>
    </div>
  );
};

export default Login;