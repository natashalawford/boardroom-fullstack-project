import React, { useEffect, useState } from "react";
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogFooter
} from '@/components/ui/dialog'
import { Button } from '@/components/ui/button'
import { Textarea } from '@/components/ui/textarea'
import { ScrollArea } from '@/components/ui/scroll-area'
import monopoly from '../assets/monopoly.png';

import { Review } from './review'
import { fetchReviewsForBoardGame, ReviewResponse } from '../services/reviewService';

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
  const [selectedGame, setSelectedGame] = useState<Game | null>(null);
  const [showReviewBox, setShowReviewBox] = useState<boolean>(false);
  const [reviewText, setReviewText] = useState<string>("");
  const [reviews, setReviews] = useState<ReviewResponse[]>([]);

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

  
  useEffect(() => {
    if (selectedGame) {
      const loadReviews = async () => {
        try {
          console.log("Fetching reviews for:", selectedGame.title);
          const fetchedReviews = await fetchReviewsForBoardGame(selectedGame.title);
          setReviews(fetchedReviews);
          console.log("Fetched reviews:", fetchedReviews);
        } catch (err) {
          console.error("Error fetching reviews:", err);
          setReviews([]);
        }
      };
      loadReviews();
    }
  }, [selectedGame]);

  const submitReview = async () => {
    if (!reviewText.trim()) {
      alert("Review cannot be empty!");
      return;
    }

    try {
      const response = await fetch("http://localhost:8080/reviews", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          gameTitle: selectedGame?.title,
          review: reviewText,
        }),
      });

      if (!response.ok) {
        throw new Error("Failed to submit review");
      }

      alert("Review submitted successfully!");
      setShowReviewBox(false);
      setReviewText("");
    } catch (err) {
      if (err instanceof Error) {
        alert(err.message || "An error occurred while submitting the review");
      } else {
        alert("An unknown error occurred while submitting the review");
      }
    }
  };

  if (loading) {
    return <p className="text-center text-gray-500">Loading games...</p>;
  }

  if (error) {
    return <p className="text-center text-red-500">Error: {error}</p>;
  }

  return (
    <div className="p-4">
      {/* Game Grid */}
      <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-4">
        {games.map((game, index) => (
          <div
            key={index}
            className="relative w-full pb-[100%] bg-cover bg-center rounded-lg overflow-hidden cursor-pointer"
            style={{
              backgroundImage: `url(${monopoly})`,
              backgroundSize: "cover",
              backgroundPosition: "center",
            }}
            onClick={() => setSelectedGame(game)}
          >
            {/* Title Overlay */}
            <div className="absolute bottom-0 w-full bg-black bg-opacity-60 text-white text-center p-2 text-sm">
              {game.title}
            </div>

            {/* Translucent Effect */}
            <div className="absolute inset-0 bg-black bg-opacity-20 opacity-0 group-hover:opacity-50 transition-opacity"></div>

            {/* Centered More Info Button */}
            <div className="absolute inset-0 flex items-center justify-center opacity-0 group-hover:opacity-100 transition-opacity">
              <Button
                className="bg-white text-black px-4 py-2 rounded-lg shadow-md hover:bg-gray-200"
                onClick={() => setSelectedGame(game)}
              >
                More Info
              </Button>
            </div>
          </div>
        ))}
      </div>

      {/* Popup for More Info */}
      {selectedGame && (
        <Dialog
          open={!!selectedGame}
          onOpenChange={() => setSelectedGame(null)}
        >
          <DialogContent className='z-200'>
            <DialogHeader>
              <DialogTitle className="text-center font-bold pt-1">
                {selectedGame.title}
              </DialogTitle>
            </DialogHeader>
            <div className="flex items-start space-x-2 p-4">
              <div className="flex-1 space-y-1 self-center">
                <p>
                  <strong>Description:</strong> {selectedGame.description}
                </p>
                <p>
                  <strong>Number of Players:</strong>{" "}
                  {selectedGame.playersNeeded}
                </p>
              </div>

              {/* Right Section: Image */}
              <div className="w-48 h-30 flex-shrink-0">
                <img
                  alt="Board Game"
                  src={monopoly}
                  className="w-full h-full object-cover rounded-lg shadow-md"
                />
              </div>
            </div>
            <div className="font-bold text-lg text-center">
              Reviews
            </div>
            <ScrollArea className='max-h-60 overflow-y-auto border border-gray-200 rounded-lg ml-3 mr-3'>
              {reviews.length > 0 ? (
                reviews.map((review) => (
                  <Review
                    key={review.id}
                    stars={review.stars}
                    comment={review.comment}
                    authorId={review.authorId}
                    timeStamp={review.timeStamp}
                   />
                ))
              ) : (
                <p className="text-center text-gray-500">No reviews yet.</p>
              )}
            </ScrollArea>
            <DialogFooter className='flex flex-col space-y-4'>
              {/* Borrow Button */}
              <Button
                className="bg-green-500 hover:bg-green-600 text-white"
                onClick={() =>
                  (window.location.href = `/specificboardgames/${selectedGame?.title}`)
                }
              >
                Borrow
              </Button>

              {/* Add Review Button */}
              <Button
                className="bg-yellow-500 hover:bg-yellow-600 text-black"
                onClick={() => setShowReviewBox(!showReviewBox)}
              >
                Add Review
              </Button>

              {/* Review Box */}
              {showReviewBox && (
                <div className="space-y-2">
                  <Textarea
                    placeholder="Write your review here..."
                    value={reviewText}
                    onChange={(e) => setReviewText(e.target.value)}
                  />
                  <Button
                    className="bg-blue-500 hover:bg-blue-600 text-white"
                    onClick={submitReview}
                  >
                    Submit Review
                  </Button>
                </div>
              )}

            </DialogFooter>
          </DialogContent>
        </Dialog>
      )}
    </div>
  );
};

export default GameGrid;
