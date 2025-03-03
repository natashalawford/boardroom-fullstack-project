package ca.mcgill.ecse321.boardroom.services;

import ca.mcgill.ecse321.boardroom.dtos.ReviewCreationDto;
import ca.mcgill.ecse321.boardroom.model.Review;
import ca.mcgill.ecse321.boardroom.repositories.ReviewRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.LocalTime;

@Service
@Validated
public class ReviewService {

    private final ReviewRepository reviewRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Transactional
    public Review createReview(@Valid ReviewCreationDto reviewToCreate) {
        LocalTime today = LocalTime.now();
        validateReview(reviewToCreate);

        Review review = new Review(
                reviewToCreate.getStars(),
                reviewToCreate.getComment(),
                today,
                reviewToCreate.getAuthor(),
                reviewToCreate.getBoardGame()
        );

        return reviewRepository.save(review);
    }

    private void validateReview(ReviewCreationDto review) {
        if (review.getStars() < 1 || review.getStars() > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5 stars.");
        }
    }


}
