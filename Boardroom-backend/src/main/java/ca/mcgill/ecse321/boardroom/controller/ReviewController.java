package ca.mcgill.ecse321.boardroom.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import ca.mcgill.ecse321.boardroom.dtos.ReviewCreationDto;
import ca.mcgill.ecse321.boardroom.dtos.responses.ReviewResponseDto;
import ca.mcgill.ecse321.boardroom.model.Review;
import ca.mcgill.ecse321.boardroom.services.ReviewService;

@RestController
@RequestMapping("/reviews")  // Base path for review endpoints
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    /**
     * Create a new review.
     *
     * @param reviewToCreate The review details to create
     * @return The created review
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReviewResponseDto createReview(@RequestBody ReviewCreationDto reviewToCreate) {
        Review createdReview = reviewService.createReview(reviewToCreate);
        return new ReviewResponseDto(createdReview);
    }

    /*
     * Get reviews for a specific board game.
     * 
     * @param title The title of the board game
     * @return The reviews for the board game
     */
    @GetMapping("/{title}")
    public List<ReviewResponseDto> getReviewsForBoardGame(@PathVariable String title) {
        List<Review> reviews = reviewService.getReviewsForBoardGame(title);
        return reviews.stream().map(ReviewResponseDto::new).toList();
    }

}
