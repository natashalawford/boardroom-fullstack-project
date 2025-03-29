import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Header from './Components/header';
import Home from './pages/home/home';
import Games from './pages/games/games';
import Events from './pages/events/events';
//import Login from './pages/login/login';
import User from './pages/user/user';

const App: React.FC = () => {
  return (
    <Router>
      <div style={{ display: 'flex', flexDirection: 'column', minHeight: '100vh' }}>
        <Header />
        <main style={{ flex: 1, paddingTop: '60px' }}>
          <Routes>
            <Route path="/" element={<Home />} />
            <Route path="/games" element={<Games />} />
            <Route path="/events" element={<Events />} />
            {/*<Route path="/login" element={<Login />} /> */}
            <Route path="/user" element={<User />} />
          </Routes>
        </main>
      </div>
    </Router>
  );
};
    <>
      <AccountDetails />
    </>
  )
}

export default App;
