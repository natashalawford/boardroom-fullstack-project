package ca.mcgill.ecse321.boardroom.services;

import ca.mcgill.ecse321.boardroom.dtos.ReviewCreationDto;
import ca.mcgill.ecse321.boardroom.exceptions.BoardroomException;
import ca.mcgill.ecse321.boardroom.model.BoardGame;
import ca.mcgill.ecse321.boardroom.model.Person;
import ca.mcgill.ecse321.boardroom.model.Review;
import ca.mcgill.ecse321.boardroom.repositories.BoardGameRepository;
import ca.mcgill.ecse321.boardroom.repositories.PersonRepository;
import ca.mcgill.ecse321.boardroom.repositories.ReviewRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Validated
public class ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private BoardGameRepository boardGameRepository;

    @Transactional
    public Review createReview(@Valid ReviewCreationDto reviewToCreate) {
        LocalTime today = LocalTime.now();
        validateReview(reviewToCreate);

        Person authorToFind = personRepository.findById(reviewToCreate.getAuthorId()).orElseThrow(
                () -> new BoardroomException(HttpStatus.NOT_FOUND, "A person with this id does not exist"));
        BoardGame boardGameToFind = boardGameRepository.findById(reviewToCreate.getBoardGameName()).orElseThrow(
                () -> new BoardroomException(HttpStatus.NOT_FOUND, "A board game with this name does not exist"));

        Review review = new Review(
                reviewToCreate.getStars(),
                reviewToCreate.getComment(),
                today,
                authorToFind,
                boardGameToFind);

        return reviewRepository.save(review);
    }

    @Transactional
    public List<Review> getReviewsForBoardGame(String title) {
        List<Review> reviews = new ArrayList<>();
        for (Review review : reviewRepository.findAll()) {
            if (review.getBoardGame().getTitle().equals(title)) {
                reviews.add(review);
            }
        }
        return reviews;
    }

    private void validateReview(ReviewCreationDto review) {
        if (review.getStars() < 1 || review.getStars() > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5 stars.");
        }
    }
}
