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
        "Something went wrong when fetching the reviews"
    );
  }

  const reviews = await response.json();

  return reviews.map((review: ReviewResponse) => {
    let formattedDate = "Invalid Date"; // Default in case formatting fails

    if (review.timeStamp) {
      try {
        const parsedDate = new Date(review.timeStamp);
        if (!isNaN(parsedDate.getTime())) {
          formattedDate = format(parsedDate, "MMMM dd, yyyy");
        }
      } catch (error) {
        console.error("Date parsing error:", error);
      }
    }

    return { ...review, timeStamp: formattedDate };
  });
};
