import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import User from './pages/user/user';
import './App.css'
import Header from "./components/header";
import Home from "./pages/home/home";
import Games from "./pages/games/games";
import Events from "./pages/events/events";
import AccountDetails from "./pages/account/AccountDetails";
import { NewGameForm } from "./components/newGameFormButton";
import SpecificGames from "./pages/games/specificBoardGames";
import { NewSpecificGameForm } from "./components/newSpecificGameFormButton";

import { Toaster } from "sonner";
//import Login from './pages/login/login';

const App: React.FC = () => {
  return (
    <Router>
      <Toaster
        position="top-center"
        richColors
        toastOptions={{
          className: "bg-white text-black",
          style: {
            background: "#FFFFFF",
            color: "#000000",
          },
        }}
      />
      <div
        style={{ display: "flex", flexDirection: "column", minHeight: "100vh" }}
      >
        <Header />
        <main style={{ flex: 1, paddingTop: "60px"}}>
          <Routes>
            <Route path="/" element={<Home />} />
            <Route path="/games" element={<Games />} />
            <Route
              path="/specificboardgames/:title"
              element={<SpecificGames />}
            />
            <Route path="/events" element={<Events />} />
            {/*<Route path="/login" element={<Login />} /> */}
            <Route path="/user" element={<User />} />
            <Route path="/account" element={<AccountDetails />} />
          </Routes>
        </main>
      </div>
    </Router>
  );
};

export default App;
