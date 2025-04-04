import React, { useEffect, useState } from "react";
import { useParams, useLocation } from "react-router-dom";
import {
  Card,
  CardContent,
  CardTitle,
  CardDescription,
} from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogFooter,
} from "@/components/ui/dialog";
import { Input } from "@/components/ui/input";
import { useAuth } from "@/auth/UserAuth"; // Import the useAuth hook
import { toast } from "sonner";

import image1 from "../../assets/games/image1.jpg";
import image2 from "../../assets/games/image2.jpg";
import image3 from "../../assets/games/image3.jpg";
import image4 from "../../assets/games/image4.jpg";
import image5 from "../../assets/games/image5.jpg";

const gameImages: { [key: number]: string } = {
  1: image1,
  2: image2,
  3: image3,
  4: image4,
  5: image5
};

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
  const location = useLocation(); // for extracting pic
  const queryParams = new URLSearchParams(location.search);
  let pictureId = queryParams.get("pictureId");
  if (!pictureId) { pictureId = "1"; } // Default to 1 if not provided

  const [specificGames, setSpecificGames] = useState<SpecificGame[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string>("");
  const [selectedGame, setSelectedGame] = useState<SpecificGame | null>(null); // For the popup
  const [startDate, setStartDate] = useState<string>(""); // Start date input
  const [endDate, setEndDate] = useState<string>(""); // End date input
  const [isDialogOpen, setIsDialogOpen] = useState<boolean>(false); // Dialog state
  const { userData } = useAuth();
  const [pendingRequests, setPendingRequests] = useState<number[]>([]); // Array of game IDs with pending requests

  useEffect(() => {
    const fetchSpecificGames = async () => {
      try {
        const response = await fetch(`http://localhost:8080/specificboardgame`);
        if (!response.ok) {
          throw new Error("Failed to fetch specific games");
        }
        const data = await response.json();

        // Filter the specific games to match the selected board game title
        const filteredGames = data.filter(
          (game: SpecificGame) => game.boardGameTitle === title
        );

        setSpecificGames(filteredGames);
      } catch (err: any) {
        setError(err.message || "An error occurred");
      } finally {
        setLoading(false);
      }
    };

    fetchSpecificGames();
  }, [title]);

  const handleBorrowRequest = async () => {
    if (!userData || !userData.id) {
      toast.error("User is not authenticated or personId is missing.");
      return;
    }

    // Check if both start and end dates are provided
    if (!startDate || !endDate) {
      toast.error("Please select both start and end dates.");
      return;
    }

    // Check if the start date is in the past
    const now = new Date();
    const selectedStartDate = new Date(startDate);
    if (selectedStartDate.getTime() < now.setHours(0, 0, 0, 0)) {
      toast.error("Start date cannot be in the past.");
      return;
    }

    // Check if a game is selected
    if (!selectedGame) {
      toast.error("No game selected for borrowing.");
      return;
    }

    // Format the start and end dates
    const today = now.toISOString().split("T")[0]; // Format as YYYY-MM-DD
    const formattedStartDate =
      startDate === today
        ? `${startDate}T${now.getHours().toString().padStart(2, "0")}:${now
            .getMinutes()
            .toString()
            .padStart(2, "0")}:00`
        : `${startDate}T00:00:00`;
    const formattedEndDate = `${endDate}T00:00:00`;

    // Prepare the payload
    const payload = {
      status: "PENDING", // Default status
      requestStartDate: formattedStartDate,
      requestEndDate: formattedEndDate,
      //personId: 4752,
      personId: userData?.id || 0, // Dynamically get personId from AuthContext, default to 0 if null
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
        const errorJson = JSON.parse(errorText); // Parse the JSON error response
        const errorMessage =
          errorJson.errors?.join(", ") || "An unknown error occurred."; // Format the error message
        toast.error(`Failed to create borrow request: ${errorMessage}`);
        return;
      }

      toast.success("Borrow request submitted successfully!");

      // Add the game ID to the pendingRequests state
      setPendingRequests((prev) => [...prev, selectedGame.id]);

      setIsDialogOpen(false); // Close the dialog
    } catch (err: any) {
      console.error("Error:", err); // Log the error for debugging
      toast.error("An error occurred while submitting the borrow request.");
    }
  };
  if (loading) {
    return (
      <p className="text-center text-gray-500">Loading specific games...</p>
    );
  }

  if (error) {
    return <p className="text-center text-red-500">Error: {error}</p>;
  }

  return (
    <div className="p-4">
      <h1 className="text-2xl font-bold mb-6">Specific Games for "{title}"</h1>
      <div className="grid md:grid-cols-3 gap-6">
        {specificGames.map((game) => (
          <Card
            key={game.id}
            className="flex flex-col items-stretch p-4 hover:shadow-lg transition-shadow"
          >
            {/* Image Section */}
            <div className="aspect-w-1 aspect-h-1">
              <img
                src={gameImages[Number(pictureId)]}
                alt={game.boardGameTitle}
                className="rounded-lg object-cover w-full h-40"
              />
            </div>

            {/* Details Section */}
            <CardContent className="flex flex-col flex-grow justify-between space-y-4">
              <div>
                <CardTitle className="text-xl font-bold text-left">
                  {game.boardGameTitle}
                </CardTitle>
                <CardDescription className="text-gray-700">
                  {game.description}
                </CardDescription>
              </div>
              <div className="mt-2">
                <p className="text-sm text-gray-600">
                  <strong>Status:</strong>{" "}
                  {game.status.charAt(0).toUpperCase() +
                    game.status.slice(1).toLowerCase()}
                </p>
              </div>
            </CardContent>

            {/* Borrow Button (Full Width of Grid Item) */}
            <div className="w-full mt-4">
              {game.status.toLowerCase() !== "available" ? (
                <Button
                  className="bg-gray-500 text-white w-full cursor-not-allowed"
                  disabled
                >
                  Not Available
                </Button>
              ) : pendingRequests.includes(game.id) ? (
                <Button
                  className="bg-gray-500 text-white w-full cursor-not-allowed"
                  disabled
                >
                  Pending
                </Button>
              ) : (
                <Button
                  className="bg-green-500 hover:bg-green-600 text-white w-full"
                  onClick={() => {
                    setSelectedGame(game);
                    setIsDialogOpen(true);
                  }}
                >
                  Borrow
                </Button>
              )}
            </div>
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
              <Button
                className="bg-blue-500 hover:bg-blue-600 text-white"
                onClick={handleBorrowRequest}
              >
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
