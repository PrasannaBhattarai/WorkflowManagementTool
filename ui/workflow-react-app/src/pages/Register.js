import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import axios from 'axios';
import './css/Register.css';
import ErrorMessagePopup from './error/ErrorMessagePopup';


function Register() {
  const [previewImage, setPreviewImage] = useState(null);
  const [errorMessage, setErrorMessage] = useState('');
  const [formData, setFormData] = useState({
    firstName: '',
    lastName: '',
    email: '',
    userName: '',
    password: '',
    confirmPassword: '',
  });

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    console.log('Submitting form...');
    setErrorMessage('');

    // Check if email is in the correct format
    if (!validateEmail(formData.email)) {
      setErrorMessage('Please enter a valid email address.');
      return;
    }

    if (formData.password !== formData.confirmPassword) {
      setErrorMessage('Provided passwords do not match');
      return;
    }

    try {
      const formDataWithImage = new FormData();
      formDataWithImage.append('firstName', formData.firstName);
      formDataWithImage.append('lastName', formData.lastName);
      formDataWithImage.append('email', formData.email);
      formDataWithImage.append('userName', formData.userName);
      formDataWithImage.append('password', formData.password);
      formDataWithImage.append('imageData', formData.imageData);

      const response = await axios.post(
        'http://localhost:8081/hello/registerUser',
        formDataWithImage,
        {
          headers: {
            'Content-Type': 'multipart/form-data',
          },
        }
      );

      console.log('User registered successfully:', response.data);
      setFormData({
        firstName: '',
        lastName: '',
        email: '',
        userName: '',
        password: '',
        confirmPassword: '',
      });
      setPreviewImage(null);
    } catch (error) {
      console.error('Error registering user:', error);
      if (error.response && error.response.status === 403) {
        setErrorMessage('Access denied. Please try again with different details.');
      }
    }
  };

  const validateEmail = (email) => {
    // Regular expression for email validation
    const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return regex.test(email);
  };

  const handleImageChange = (e) => {
    const image = e.target.files[0];
    setPreviewImage(URL.createObjectURL(image));
    setFormData({ ...formData, imageData: image });
  };

  const closeErrorMessage = () => {
    setErrorMessage('');
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
        <form className='registerForm' onSubmit={handleSubmit}>
        {errorMessage && <ErrorMessagePopup message={errorMessage} onClose={closeErrorMessage} />}
        <div className="Photo">
            <label htmlFor="upload" className="custom-file-upload">
              <i className="fas fa-cloud-upload-alt"></i> Choose an Image
            </label>
            <input
              id="upload"
              type="file"
              accept="image/*"
              className="hiddenInput"
              onChange={handleImageChange}
            />
            {previewImage && (
              <img className="uploaded-image" src={previewImage} alt="Uploaded Image" />
            )}
          </div>

        <div className='names'>
          <label className='registerLabel'>
            First Name:
            <input className='registerFname'
              type="text"
              name="firstName"
              value={formData.firstName}
              onChange={handleChange}
            />
          </label>
            <label className='registerLabel'>
              Last Name:
              <input className='registerLname'
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
            <input className='registerEmail'
              type="email"
              name="email"
              value={formData.email}
              onChange={handleChange}
            />
          </label>
          <label className='registerLabel'>
            Username:
            <input className='registerUname'
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
            <input className='registerPassword'
              type="password"
              name="password"
              value={formData.password}
              onChange={handleChange}
            />
          </label>
          <label className='registerLabel'>
            Confirm Password:
            <input className='registerConfirm'
              type="password"
              name="confirmPassword"
              value={formData.confirmPassword}
              onChange={handleChange}
            />
          </label>
        </div>
        <br></br><br></br>
        <button className="registerButton" type="submit">Register</button>
        <h5 className='registerH5'>Already Have an Account? <Link to="/login">Log In</Link></h5>
      </form>
      </div>
      
    </div>
  );
}

export default Register;
