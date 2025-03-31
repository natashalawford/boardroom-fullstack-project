import React, { useEffect, useState } from "react";

interface Game {
  title: string;
  description: string;
  playersNeeded: number; 
  picture: number; 
}

const GameGrid: React.FC = () => {
  const [games, setGames] = useState<Game[]>([]);
  const [loading, setLoading] = useState<boolean>(false);
  const [error, setError] = useState<string>("");
  const [selectedGame, setSelectedGame] = useState<Game | null>(null); // For the popup

  useEffect(() => {
    const loadGames = async () => {
      setLoading(true);
      setError("");
      try {
        const response = await fetch("http://localhost:8080/boardgame");
        if (!response.ok) {
          throw new Error("Failed to fetch games");
        }
        const data = await response.json();
        setGames(data);
      } catch (err: any) {
        setError(err.message || "An error occurred");
      } finally {
        setLoading(false);
      }
    };

    loadGames();
  }, []);

  if (loading) {
    return <p>Loading games...</p>;
  }

  if (error) {
    return <p>Error: {error}</p>;
  }

  return (
    <div>
      {/* Game Grid */}
      <div style={{ display: "grid", gridTemplateColumns: "repeat(auto-fill, minmax(200px, 1fr))", gap: "16px" }}>
        {games.map((game, index) => (
          <div
            key={index}
            style={{
              position: "relative",
              width: "100%",
              paddingBottom: "100%", // Makes the div a square
              backgroundImage: `url(http://localhost:8080/images/${game.picture})`,
              backgroundSize: "cover",
              backgroundPosition: "center",
              borderRadius: "8px",
              overflow: "hidden",
              cursor: "pointer",
            }}
          >
            {/* Title Overlay */}
            <div
              style={{
                position: "absolute",
                bottom: "0",
                width: "100%",
                background: "rgba(0, 0, 0, 0.6)",
                color: "white",
                textAlign: "center",
                padding: "8px",
                fontSize: "16px",
              }}
            >
              {game.title}
            </div>

            {/* Hover Effect */}
            <div
              style={{
                position: "absolute",
                top: "0",
                left: "0",
                width: "100%",
                height: "100%",
                display: "flex",
                justifyContent: "center",
                alignItems: "center",
                background: "rgba(0, 0, 0, 0.5)",
                opacity: "0",
                transition: "opacity 0.3s",
              }}
              onMouseEnter={(e) => (e.currentTarget.style.opacity = "1")}
              onMouseLeave={(e) => (e.currentTarget.style.opacity = "0")}
            >
              <button
                style={{
                  padding: "8px 16px",
                  background: "orange",
                  border: "none",
                  borderRadius: "4px",
                  cursor: "pointer",
                }}
                onClick={() => setSelectedGame(game)}
              >
                More Info
              </button>
            </div>
          </div>
        ))}
      </div>

      {/* Popup for More Info */}
      {selectedGame && (
        <div
          style={{
            position: "fixed",
            top: "0",
            left: "0",
            width: "100%",
            height: "100%",
            background: "rgba(0, 0, 0, 0.5)",
            display: "flex",
            justifyContent: "center",
            alignItems: "center",
          }}
          onClick={() => setSelectedGame(null)} // Close popup on background click
        >
          <div
            style={{
              background: "white",
              padding: "24px",
              borderRadius: "8px",
              maxWidth: "400px",
              width: "90%",
              textAlign: "center",
            }}
            onClick={(e) => e.stopPropagation()} // Prevent closing when clicking inside the popup
          >
            <h2>{selectedGame.title}</h2>
            <p><strong>Description:</strong> {selectedGame.description}</p>
            <p><strong>Number of Players:</strong> {selectedGame.playersNeeded}</p>
            <button
              style={{
                marginTop: "16px",
                padding: "8px 16px",
                background: "#007BFF",
                color: "white",
                border: "none",
                borderRadius: "4px",
                cursor: "pointer",
              }}
              onClick={() => setSelectedGame(null)}
            >
              Close
            </button>
          </div>
        </div>
      )}
    </div>
  );
};

export default GameGrid;