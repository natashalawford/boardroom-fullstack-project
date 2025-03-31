import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';

interface SpecificGame {
  id: number;
  location: string;
  condition: string;
  availability: boolean;
}

const SpecificGames: React.FC = () => {
  const { title } = useParams<{ title: string }>(); // Extract the game title from the URL
  const [specificGames, setSpecificGames] = useState<SpecificGame[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string>("");

  useEffect(() => {
    const fetchSpecificGames = async () => {
      try {
        const response = await fetch(`http://localhost:8080/specificgames`);
        if (!response.ok) {
          throw new Error("Failed to fetch specific games");
        }
        const data = await response.json();
        setSpecificGames(data);
      } catch (err: any) {
        setError(err.message || "An error occurred");
      } finally {
        setLoading(false);
      }
    };

    fetchSpecificGames();
  }, [title]);

  if (loading) {
    return <p className="text-center text-gray-500">Loading specific games...</p>;
  }

  if (error) {
    return <p className="text-center text-red-500">Error: {error}</p>;
  }

  return (
    <div className="p-4">
      <h1 className="text-2xl font-bold">Specific Games for "{title}"</h1>
      <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-4 mt-4">
        {specificGames.map((game) => (
          <div
            key={game.id}
            className="border rounded-lg p-4 shadow-md"
          >
            <p><strong>Location:</strong> {game.location}</p>
            <p><strong>Condition:</strong> {game.condition}</p>
            <p><strong>Availability:</strong> {game.availability ? "Available" : "Not Available"}</p>
          </div>
        ))}
      </div>
    </div>
  );
};

export default SpecificGames;