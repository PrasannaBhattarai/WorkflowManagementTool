import React from 'react';
import './../css/AuthAlert.css'

const AuthAlert = ({ errorCode, onClose }) => {
    return (
      <div className="auth-alert">
        <p className='authText' >Warning! Invalid Credentials used! {errorCode}</p>
        <p className='authText'>Please retry with correct credentials!</p>
        <button class="authButton" onClick={onClose}>Retry</button>
      </div>
    );
  };

export default AuthAlert;
