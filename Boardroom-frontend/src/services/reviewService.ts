import { format } from "date-fns";

export interface ReviewResponse {
  id: number;
  stars: number;
  comment: string;
  authorId: number;
  boardgameName: string;
  timeStamp: string;
}

const API_BASE_URL = "http://localhost:8080";

export const fetchReviewsForBoardGame = async (
  title: string
): Promise<ReviewResponse[]> => {
  const response = await fetch(
    `${API_BASE_URL}/reviews/${encodeURIComponent(title)}`
  );
  if (!response.ok) {
    const errorResponse = await response.json();
    throw new Error(
      errorResponse.errors?.[0] ||
        "Something went wrong when fetching the board games"
    );
  }

  const reviews = await response.json();

  // Convert the timeStamp to a readable string
  return reviews.map((review: ReviewResponse) => ({
    ...review,
    timeStamp: format(new Date(review.timeStamp), "MMMM dd, yyyy"), // e.g., "March 31, 2025"
  }));
};
