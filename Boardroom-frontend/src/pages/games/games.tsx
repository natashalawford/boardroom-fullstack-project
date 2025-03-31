import React from 'react';
import GameGrid from "../../Components/gamegrid";

const Games: React.FC = () => {
    return (
        <div>
          <h1>Available Games</h1>
          <GameGrid />
        </div>
      );
    };

export default Games;