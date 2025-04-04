import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import userImage from '../assets/user.png';
import diceImage from '../assets/dice.png';
import { Button } from '@/components/ui/button';
import { LoginPopup } from '@/pages/login/loginPopup'
import { useAuth } from '@/auth/UserAuth'

const Header: React.FC = () => {
  const navigate = useNavigate();
  const { userData } = useAuth()  // get global auth info
  const [showLoginPopup, setShowLoginPopup] = useState(false) //state for login pop open or not

  return (
    <header
      style={{
        position: "fixed",
        top: 0,
        left: 0,
        width: "100%",
        height: "70px",
        display: "flex",
        justifyContent: "space-between",
        alignItems: "center",
        padding: "0 20px",
        backgroundColor: "#303036",
        zIndex: 1000,
        boxShadow: "0 2px 5px rgba(0, 0, 0, 0.2)",
      }}
    >
      <img
        src={diceImage}
        alt="Logo"
        style={{ width: "50px", height: "50px", cursor: "pointer" }}
        onClick={() => navigate("/")}
      />
      <div style={{ display: 'flex', alignItems: 'center', gap: '25px' }}>
        <Button variant='default' onClick={() => navigate('/games')} className='mr-2'>Games</Button>
        <Button variant='default' onClick={() => navigate('/events')} className='mr-2'>Events</Button>
        {/* 
          Whether logged in or out, clicking the button opens the login popup.
          The button text changes based on whether the user is logged in or not.
        */}
        <Button variant="default" onClick={() => setShowLoginPopup(true)} className="mr-2">
          {userData ? 'Logout' : 'Login'}
        </Button>
        <img
          src={userImage}
          alt="User Profile"
          style={{ width: "40px", height: "40px", cursor: "pointer" }}
          onClick={() => navigate("/account")}
        />
      </div>

      {/* Render the popup. Pass isOpen and onClose props. */}
      <LoginPopup
        isOpen={showLoginPopup}
        onClose={() => setShowLoginPopup(false)}
      />
    </header>
  );
};

export default Header;
