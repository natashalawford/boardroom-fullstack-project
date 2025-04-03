import { format } from "date-fns";

export interface ReviewResponse {
  id: number;
  stars: number;
  comment: string;
  authorId: number;
  boardgameName: string;
  timestamp: string;
  authorName: string;
}

export interface ReviewCreation {
  id: number;
  stars: number;
  comment: string;
  authorId: number;
  boardgameName: string;
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
  console.log("Reviews", reviews);

  return reviews.map((review: ReviewResponse) => {
    try {
      console.log("Timestamp", review.timestamp);
      review.timestamp = format(new Date(review.timestamp), "yyyy-MM-dd HH:mm:ss");
    } catch (error) {
      console.error("Date parsing error:", error);
    }

    return { ...review };
  });
};

function formatLocalDateTimeWithDateFns(localDateTime: string): string {
  try {
    // Parse the local datetime string into a Date object
    const date = new Date(localDateTime);

    // Format the date and time using date-fns
    return format(date, "MMMM dd, yyyy hh:mm:ss a"); // Example: "April 02, 2025 01:26:42 AM"
  } catch (error) {
    console.error("Error formatting local datetime:", error);
    return "Invalid DateTime";
  }
}