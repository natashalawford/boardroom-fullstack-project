import React from 'react';
import { useNavigate } from 'react-router-dom';
import userImage from '../assets/user.png';
import diceImage from '../assets/dice.png';

const Header: React.FC = () => {
  const navigate = useNavigate();

  return (
    <header
      style={{
        position: 'fixed',
        top: 0,
        left: 0,
        width: '100%',
        height: '70px',
        display: 'flex',
        justifyContent: 'space-between',
        alignItems: 'center',
        padding: '0 20px',
        backgroundColor: '#303036',
        zIndex: 1000,
        boxShadow: '0 2px 5px rgba(0, 0, 0, 0.2)',
      }}
    >
      <img
        src={diceImage}
        alt="Logo"
        style={{ width: '50px', height: '50px', cursor: 'pointer' }}
        onClick={() => navigate('/')}
      />
      <div style={{ display: 'flex', alignItems: 'center', gap: '25px' }}>
        <button onClick={() => navigate('/games')}>Games</button>
        <button onClick={() => navigate('/events')}>Events</button>
        <button onClick={() => navigate('/login')}>Login</button>
        <img
          src={userImage}
          alt="User Profile"
          style={{ width: '40px', height: '40px', cursor: 'pointer' }}
          onClick={() => navigate('/user')}
        />
      </div>
    </header>
  );
};

export default Header;
