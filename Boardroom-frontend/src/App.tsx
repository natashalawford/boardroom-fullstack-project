import React from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Header from "./components/header";
import Home from "./pages/home/home";
import Games from "./pages/games/games";
import Events from "./pages/events/events";
import AccountDetails from "./pages/account/AccountDetails";
import { NewGameForm } from "./components/newGameFormButton";
import SpecificGames from "./pages/games/specificBoardGames";
import { NewSpecificGameForm } from "./components/newSpecificGameFormButton";

import { Toaster } from "sonner";

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
            <Route path="/games/new" element={<NewGameForm />} />
            <Route
              path="/specificboardgames/:title"
              element={<SpecificGames />}
            />
            <Route path="/events" element={<Events />} />
            <Route
              path="/games/new/specific"
              element={<NewSpecificGameForm />}
            />
            {/*<Route path="/login" element={<Login />} /> */}
            <Route path="/user" element={<AccountDetails />} />
          </Routes>
        </main>
      </div>
    </Router>
  );
};

export default App;
