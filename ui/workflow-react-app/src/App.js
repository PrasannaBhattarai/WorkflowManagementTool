import React, { useState, useEffect } from 'react';
import { BrowserRouter as Router, Route, Routes, Navigate } from 'react-router-dom';
import Login from './pages/Login';
import Register from './pages/Register';
import Home from './pages/Home';
import AuthChecker from './pages/AuthCheck';
import AuthAlert from './pages/alert/AuthAlert';
import Hello from './hello'; 


const App = () => {
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [showAuthAlert, setShowAuthAlert] = useState(false);
  const [errorCode, setErrorCode] = useState(null);

  useEffect(() => {
    const token = localStorage.getItem('token');

    if (token) {
      setIsAuthenticated(true);
    } else {
      setIsAuthenticated(false);
    }
  }, []);

  const closeAuthAlert = () => {
    setShowAuthAlert(false);
  };

  return (
    <Router>
      <div className="App">
      {showAuthAlert && <AuthAlert errorCode={errorCode} onClose={closeAuthAlert} />}
      <Routes>
          <Route path="/home" element={isAuthenticated ? <Home /> : <Navigate to="/login" />} />
          <Route path="/login" element={<Login setShowAuthAlert={setShowAuthAlert} setErrorCode={setErrorCode} />} />
          <Route path="/register" element={<Register />} />
          <Route path="/auth-check" element={<AuthChecker />} />
          <Route path="/hello" element={isAuthenticated ? <Hello /> : <Navigate to="/login" />} />
        </Routes>
      </div>
    </Router>
  );
};

export default App;
