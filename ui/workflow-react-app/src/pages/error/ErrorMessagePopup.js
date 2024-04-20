import React from 'react';
import './../css/error/ErrorMessagePopup.css';

const ErrorMessagePopup = ({ message, onClose }) => {
  return (
    <div className="error-popup">
      <div className="error-content">
        <span className="close-btn" onClick={onClose}>&times;</span>
        <p>{message}</p>
      </div>
    </div>
  );
}

export default ErrorMessagePopup;
