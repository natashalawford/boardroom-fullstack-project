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
      review.timestamp = formatLocalTimeWithDateFns(review.timestamp);
    } catch (error) {
      console.error("Date parsing error:", error);
    }

    return { ...review };
  });
};

function formatLocalTimeWithDateFns(localTime: string): string {
  try {
    const [hours, minutes, secondsWithMilliseconds] = localTime.split(":");
    const [seconds, milliseconds] = secondsWithMilliseconds.split(".");

    const date = new Date();
    date.setHours(
      parseInt(hours, 10),
      parseInt(minutes, 10),
      parseInt(seconds, 10),
      parseInt(milliseconds, 10)
    );

    return format(date, "hh:mm:ss a"); // Format as "01:26:42 AM"
  } catch (error) {
    console.error("Error formatting local time:", error);
    return "Invalid Time";
  }
}
