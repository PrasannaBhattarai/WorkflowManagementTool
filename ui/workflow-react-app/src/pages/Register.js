import React, { useState } from 'react';
import axios from 'axios';
import { Link } from 'react-router-dom';
import './css/Register.css'

function App() {
  const [formData, setFormData] = useState({
    firstName: '',
    lastName: '',
    email: '',
    userName: '',
    password: '',
  });

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    console.log('Submitting form...');

    try {
      const response = await axios.post(
        'http://localhost:8081/hello/registerUser',
        formData
      );

      console.log('User registered successfully:', response.data);
    } catch (error) {
      console.error('Error registering user:', error);
    }
  };

  return (
    <div className="body">
      <div className='leftPanel'>
      <div className="top-register">
          <h1 className='registerText'>Let's</h1>
          <h1 className='registerText2'>Begin</h1>
          <h1 className='registerText3'>Our</h1>
          <h1 className='registerText4'>Journey!</h1>
        </div>
        <div className="bottom-div">
          <div className='circle1'></div>
          <div className='circle2'></div>
          <div className='circle3'></div>
        </div>
      </div>
      <div className='rightPanel'>
        <form onSubmit={handleSubmit}>
        <div>
          <label className='registerLabel'>
            First Name:
            <input className='register'
              type="text"
              name="firstName"
              value={formData.firstName}
              onChange={handleChange}
            />
          </label>
          <label className='registerLabel'>
            Last Name:
            <input className='register'
              type="text"
              name="lastName"
              value={formData.lastName}
              onChange={handleChange}
            />
          </label>
        </div>
        <div>
          <label className='registerLabel'>
            Email:
            <input className='register'
              type="email"
              name="email"
              value={formData.email}
              onChange={handleChange}
            />
          </label>
          <label className='registerLabel'>
            Username:
            <input className='register'
              type="text"
              name="userName"
              value={formData.userName}
              onChange={handleChange}
            />
          </label>
        </div>
        <div>
          <label className='registerLabel'>
            Password:
            <input className='register'
              type="password"
              name="password"
              value={formData.password}
              onChange={handleChange}
            />
          </label>
          <label className='registerLabel'>
            Confirm Password:
            <input className='register'
              type="password"
              name="password"
            />
          </label>
        </div>
        <br></br><br></br>
        <button className="registerButton" type="button" onClick={handleSubmit}>Register</button>
        <h5 className='registerH5'>Already Have an Account? <a href="/login">Log In</a></h5>
      </form>
      </div>
      
    </div>
  );
}

export default App;
