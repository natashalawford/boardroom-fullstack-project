import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { Card, CardContent, CardHeader, CardTitle, CardDescription } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogFooter } from "@/components/ui/dialog";
import { Input } from "@/components/ui/input";

interface SpecificGame {
  id: number;
  description: string;
  picture: number;
  status: string;
  boardGameTitle: string;
  ownerId: number;
}

const SpecificGames: React.FC = () => {
  const { title } = useParams<{ title: string }>(); // Extract the game title from the URL
  const [specificGames, setSpecificGames] = useState<SpecificGame[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string>("");
  const [selectedGame, setSelectedGame] = useState<SpecificGame | null>(null); // For the popup
  const [startDate, setStartDate] = useState<string>(""); // Start date input
  const [endDate, setEndDate] = useState<string>(""); // End date input
  const [isDialogOpen, setIsDialogOpen] = useState<boolean>(false); // Dialog state

  useEffect(() => {
    const fetchSpecificGames = async () => {
      try {
        const response = await fetch(`http://localhost:8080/specificboardgame?title=${title}`);
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

    const handleBorrowRequest = async () => {
    if (!startDate || !endDate) {
      alert("Please specify both start and end dates.");
      return;
    }
  
    if (!selectedGame) {
      alert("No game selected for borrowing.");
      return;
    }
  
    // Get the current date and time
    const now = new Date();
    const today = now.toISOString().split("T")[0]; // Format as YYYY-MM-DD
  
    // Append time to the start date
    const formattedStartDate =
      startDate === today
        ? `${startDate}T${now.getHours().toString().padStart(2, "0")}:${now.getMinutes().toString().padStart(2, "0")}:00` // Use current time if start date is today
        : `${startDate}T00:00:00`;
  
    // Append time to the end date
    const formattedEndDate = `${endDate}T00:00:00`;
  
    const payload = {
      status: "PENDING", // Default status
      requestStartDate: formattedStartDate,
      requestEndDate: formattedEndDate,
      personId: 4752, // Replace with the actual person ID
      specificBoardGameId: selectedGame.id, // Extract the ID from the selected game
    };
  
    console.log("Payload being sent:", payload); // Log the payload for debugging
  
    try {
      const response = await fetch("http://localhost:8080/borrowRequests", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(payload),
      });
  
      if (!response.ok) {
        const errorText = await response.text(); // Get error details from the backend
        throw new Error(`Failed to create borrow request: ${errorText}`);
      }
  
      alert("Borrow request submitted successfully!");
      setIsDialogOpen(false); // Close the dialog
    } catch (err: any) {
      console.error("Error:", err); // Log the error for debugging
      alert(err.message || "An error occurred while submitting the borrow request");
    }
  };

  if (loading) {
    return <p className="text-center text-gray-500">Loading specific games...</p>;
  }

  if (error) {
    return <p className="text-center text-red-500">Error: {error}</p>;
  }

  return (
    <div className="p-4">
      <h1 className="text-2xl font-bold mb-6">Specific Games for "{title}"</h1>
      <div className="space-y-6">
        {specificGames.map((game) => (
          <Card key={game.id} className="flex flex-row items-center space-x-4 p-4 hover:shadow-lg transition-shadow">
            {/* Image Section */}
            <div className="w-1/3">
              <img
                src={`http://localhost:8080/images/${game.picture}`}
                alt={game.boardGameTitle}
                className="rounded-lg object-cover w-full h-40"
              />
            </div>

            {/* Details Section */}
            <CardContent className="w-2/3">
              <CardHeader>
                <CardTitle className="text-xl font-bold">{game.boardGameTitle}</CardTitle>
                <CardDescription className="text-gray-700">{game.description}</CardDescription>
              </CardHeader>
              <div className="mt-2">
                <p className="text-sm">
                  <strong>Status:</strong> {game.status}
                </p>
                <p className="text-sm">
                  <strong>Owner ID:</strong> {game.ownerId}
                </p>
              </div>

              {/* Borrow Button */}
              <div className="mt-4">
                <Button
                  className="bg-green-500 hover:bg-green-600 text-white"
                  onClick={() => {
                    setSelectedGame(game);
                    setIsDialogOpen(true); // Open the dialog
                  }}
                >
                  Borrow
                </Button>
              </div>
            </CardContent>
          </Card>
        ))}
      </div>

      {/* Borrow Request Dialog */}
      {isDialogOpen && selectedGame && (
        <Dialog open={isDialogOpen} onOpenChange={setIsDialogOpen}>
          <DialogContent>
            <DialogHeader>
              <DialogTitle>Borrow "{selectedGame.boardGameTitle}"</DialogTitle>
            </DialogHeader>
            <div className="space-y-4">
              <Input
                type="date"
                placeholder="Start Date"
                value={startDate}
                onChange={(e) => setStartDate(e.target.value)}
              />
              <Input
                type="date"
                placeholder="End Date"
                value={endDate}
                onChange={(e) => setEndDate(e.target.value)}
              />
            </div>
            <DialogFooter className="mt-4">
              <Button className="bg-blue-500 hover:bg-blue-600 text-white" onClick={handleBorrowRequest}>
                Submit Request
              </Button>
              <Button variant="outline" onClick={() => setIsDialogOpen(false)}>
                Cancel
              </Button>
            </DialogFooter>
          </DialogContent>
        </Dialog>
      )}
    </div>
  );
};

export default SpecificGames;