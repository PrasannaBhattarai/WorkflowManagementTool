import React from 'react';
import { Link } from 'react-router-dom';

const Home = () => {
  return (
    <div>
      <h2>Hello User With Token</h2>
      <Link to="/hello">Go to Hello Page</Link>
    </div>
  );
};

export default Home;
