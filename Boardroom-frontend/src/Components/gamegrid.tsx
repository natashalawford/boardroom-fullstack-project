import React, { useEffect, useState } from 'react'
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
import monopoly from '../assets/monopoly.png'

import { Review } from './review'
import {
  fetchReviewsForBoardGame,
  ReviewResponse
} from '../services/reviewService'
import { toast } from 'sonner'
import { useAuth } from '@/auth/UserAuth'

// import dice from "../assets/dice.png";
// import user from "../assets/user.png";
import image1 from '../assets/games/image1.jpg'
import image2 from '../assets/games/image2.jpg'
import image3 from '../assets/games/image3.jpg'
import image4 from '../assets/games/image4.jpg'
import image5 from '../assets/games/image5.jpg'
import { getUserName } from '@/services/AccountDetailsService'

const gameImages: { [key: number]: string } = {
  1: image1,
  2: image2,
  3: image3,
  4: image4,
  5: image5
}

interface Game {
  title: string
  description: string
  playersNeeded: number
  picture: number
}

const GameGrid: React.FC<{ games: Game[] }> = ({ games }) => {
  const { userData } = useAuth()
  const [loading, setLoading] = useState<boolean>(false)
  const [error, setError] = useState<string>('')
  const [selectedGame, setSelectedGame] = useState<Game | null>(null)
  const [showReviewBox, setShowReviewBox] = useState<boolean>(false)
  const [reviewText, setReviewText] = useState<string>('')
  const [reviews, setReviews] = useState<ReviewResponse[]>([])
  const [stars, setStars] = useState<number>(0)

  // Load reviews + user names
  const loadReviewsWithUsernames = async () => {
    try {
      if (!selectedGame) return

      const fetchedReviews = await fetchReviewsForBoardGame(
        selectedGame.title
      )

      // For each review, fetch the user name
      const reviewsWithUsernames = await Promise.all(
        fetchedReviews.map(async rev => {
          const authorName = await getUserName(rev.authorId)
          return { ...rev, authorName }
        })
      )

      setReviews(reviewsWithUsernames)
      console.log("Reviews with usernames:", reviewsWithUsernames)
    } catch (err) {
      console.error('Error fetching reviews:', err)
      toast.error('Failed to fetch reviews for this game.')
      setReviews([])
    }
  }

  useEffect(() => {
    if (!selectedGame) return
    loadReviewsWithUsernames()
  }, [selectedGame])

  const submitReview = async () => {
    if (!reviewText.trim()) {
      toast.error('Review cannot be empty!')
      return
    }

    if (stars < 1 || stars > 5) {
      toast.error('Please select a star rating between 1 and 5!')
      return
    }

    try {
      const response = await fetch('http://localhost:8080/reviews', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({
          stars: stars,
          comment: reviewText,
          authorId: userData?.id,
          boardGameName: selectedGame?.title
        })
      })

      if (!response.ok) {
        const errorData = await response.json()
        console.error('Error submitting review:', errorData)
        const errorMessage = errorData.message || 'Failed to submit review'
        toast.error(errorMessage)
        return
      }
      toast.success('Review submitted successfully!')
      setShowReviewBox(false)
      setReviewText('')
      setStars(0)
      loadReviewsWithUsernames()
    } catch (err) {
      if (err instanceof Error) {
        toast.error(
          err.message || 'An error occurred while submitting the review'
        )
      } else {
        toast.error('An unknown error occurred while submitting the review')
      }
    }
  }

  if (loading) {
    return <p className='text-center text-gray-500'>Loading games...</p>
  }

  if (error) {
    return <p className='text-center text-red-500'>Error: {error}</p>
  }

  return (
    <div className='p-4'>
      {/* Game Grid */}
      <div className='grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-4'>
        {games.map((game, index) => (
          <div
            key={index}
            className='relative w-full pb-[100%] bg-cover bg-center rounded-lg overflow-hidden cursor-pointer group'
            style={{
              backgroundImage: `url(${gameImages[game.picture] || monopoly})`,
              backgroundSize: 'cover',
              backgroundPosition: 'center'
            }}
            onClick={() => setSelectedGame(game)}
          >
            {/* Title Overlay */}
            <div className='absolute bottom-0 w-full bg-black bg-opacity-60 text-white text-center p-2 text-sm'>
              {game.title}
            </div>

            {/* Translucent Effect */}
            <div className='absolute inset-0 bg-black bg-opacity-20 opacity-0 group-hover:opacity-50 transition-opacity'></div>

            {/* Centered More Info Button */}
            <div className='absolute inset-0 flex items-center justify-center opacity-0 group-hover:opacity-100 transition-opacity'>
              <Button
                className='bg-white text-black px-4 py-2 rounded-lg shadow-md hover:bg-gray-200'
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
          onOpenChange={() => {
            setSelectedGame(null)
            setShowReviewBox(false) // Reset showReviewBox to false when the dialog is closed
          }}
        >
          <DialogContent className='z-200'>
            <DialogHeader>
              <DialogTitle className='text-center font-bold pt-1'>
                {selectedGame.title}
              </DialogTitle>
            </DialogHeader>
            <div className='flex items-start space-x-2 p-4'>
              <div className='flex-1 space-y-1 self-center'>
                <p>
                  <strong>Description:</strong> {selectedGame.description}
                </p>
                <p>
                  <strong>Number of Players:</strong>{' '}
                  {selectedGame.playersNeeded}
                </p>
              </div>

              {/* Right Section: Image */}
              <div className='w-48 h-30 flex-shrink-0'>
                <img
                  alt='Board Game'
                  src={gameImages[selectedGame.picture] || monopoly}
                  className='w-full h-full object-cover rounded-lg shadow-md'
                />
              </div>
            </div>
            <div className='font-bold text-lg text-center'>Reviews</div>
            <ScrollArea className='max-h-40 overflow-y-auto rounded-lg ml-3 mr-3'>
              {reviews.length > 0 ? (
                reviews.map(review => (
                  <Review
                    key={review.id}
                    stars={review.stars}
                    comment={review.comment}
                    authorName={review.authorName}
                    timestamp={review.timestamp}
                  />
                ))
              ) : (
                <p className='text-center text-gray-500 p-3'>No reviews yet.</p>
              )}
            </ScrollArea>
            <DialogFooter>
              <div className='w-full flex flex-col space-y-2'>
                {/* Review Box */}
                {showReviewBox && (
                  <>
                    <select
                      value={stars}
                      onChange={e => setStars(Number(e.target.value))}
                      className='w-full border border-gray-300 rounded-lg p-2'
                    >
                      <option value={0}>Select Star Rating</option>
                      {[1, 2, 3, 4, 5].map(star => (
                        <option key={star} value={star}>
                          {star} Star{star > 1 ? 's' : ''}
                        </option>
                      ))}
                    </select>
                    <Textarea
                      placeholder='Write your review here...'
                      value={reviewText}
                      onChange={e => setReviewText(e.target.value)}
                      className='w-full border border-gray-300 rounded-lg p-2'
                    />
                    <Button
                      className='bg-blue-500 hover:bg-blue-600 text-white w-full'
                      onClick={submitReview}
                    >
                      Submit Review
                    </Button>
                  </>
                )}

                {/* Buttons Row */}
                <div className='flex justify-between w-full space-x-3'>
                  <Button
                    className='bg-green-500 hover:bg-green-600 text-white flex-1'
                    onClick={() =>
                      (window.location.href = `/specificboardgames/${selectedGame?.title}?pictureId=${selectedGame?.picture}`)
                    }
                  >
                    Borrow
                  </Button>
                  <Button
                    className={`flex-1 ${
                      showReviewBox
                        ? 'bg-red-500 hover:bg-red-600 text-white'
                        : 'bg-yellow-500 hover:bg-yellow-600 text-black'
                    }`}
                    onClick={() => setShowReviewBox(!showReviewBox)}
                  >
                    {showReviewBox ? 'Cancel Review' : 'Add Review'}
                  </Button>
                </div>
              </div>
            </DialogFooter>
          </DialogContent>
        </Dialog>
      )}
    </div>
  )
}

export default GameGrid
