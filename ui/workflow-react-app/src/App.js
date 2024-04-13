import React, { useState, useEffect } from 'react';
import { BrowserRouter as Router, Route, Routes, Navigate } from 'react-router-dom';
import Login from './pages/Login';
import Register from './pages/Register';
import Home from './pages/Home';
import AuthChecker from './pages/AuthCheck';
import AuthAlert from './pages/alert/AuthAlert';
import Hello from './hello';
import EditUser from './pages/EditUser';
import Project from './pages/Project';
import ChartComponent from './pages/admin/ChartComponent';
import UserAnalytics from './pages/admin/ProjectMonthlyGraph';
import Notifications from './pages/Notifications';

const App = () => {
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [showAuthAlert, setShowAuthAlert] = useState(false);
  const [errorCode, setErrorCode] = useState(null);

  useEffect(() => {
    const token = localStorage.getItem('token');
    console.log('Token from localStorage:', token);

    if (token) {
      setIsAuthenticated(true);
      localStorage.setItem('token', token);
    } else {
      setIsAuthenticated(false);
    }
  }, []);

  console.log('isAuthenticated:', isAuthenticated);

  const closeAuthAlert = () => {
    setShowAuthAlert(false);
  };

  return (
    <Router>
      <div className="App">
        {showAuthAlert && <AuthAlert errorCode={errorCode} onClose={closeAuthAlert} />}
        <Routes>
          <Route path="/home" element={isAuthenticated ? <Home /> : <Navigate to="/login" />} />
          <Route path="/edit-user-details" element={isAuthenticated ? <EditUser /> : <Navigate to="/login" />} />
          <Route path="/project" element={isAuthenticated ? <Project /> : <Navigate to="/login" />} />
          <Route path="/notifications" element={isAuthenticated ? <Notifications /> : <Navigate to="/notifications" />} />
          <Route path="/login" element={<Login setShowAuthAlert={setShowAuthAlert} setErrorCode={setErrorCode} />} />
          <Route path="/register" element={<Register />} />
          <Route path="/auth-check" element={<AuthChecker />} />
          <Route path="/hello" element={isAuthenticated ? <Hello /> : <Navigate to="/login" />} />
          {/* Redirect base URL to /login */}
          <Route path="/" element={<Navigate to="/login" />} />
          {/* <Route path="*" element={<Navigate to="/login" />} /> */}

          <Route path="/chart" element={<ChartComponent />} />
          <Route path="/user" element={<UserAnalytics />} />
        </Routes>
      </div>
    </Router>
  );
};

export default App;
