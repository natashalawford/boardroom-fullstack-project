package ca.mcgill.ecse321.boardroom.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ca.mcgill.ecse321.boardroom.model.BoardGame;
import ca.mcgill.ecse321.boardroom.model.Person;
import ca.mcgill.ecse321.boardroom.model.Review;

@SpringBootTest
public class ReviewRepositoryTests {
    @Autowired
    private ReviewRepository reviewRepo;
    @Autowired
    private BoardGameRepository boardGameRepo;
    @Autowired
    private PersonRepository personRepo;

    @AfterEach
    public void clearDatabase() {
        reviewRepo.deleteAll();
        boardGameRepo.deleteAll();
        personRepo.deleteAll();
    }

    @Test
    public void testCreateAndReadReview() {
        // Arrange
        Person bob = new Person("Bob", "bob@mail.mcgill.ca", "1234", true);
        bob = personRepo.save(bob);

        BoardGame boardGame = new BoardGame("Monopoly", "A game about buying properties", 2, 1234);
        boardGame = boardGameRepo.save(boardGame);

        Review review = new Review(5, "We had a fun time", LocalDateTime.parse("2025-04-02T22:36:08"), bob, boardGame);
        review = reviewRepo.save(review);

        // Act
        Review reviewFromDB = reviewRepo.findReviewById(review.getId());

        // Assert
        assertNotNull(reviewFromDB);
        assertEquals(review.getId(), reviewFromDB.getId());
        assertEquals(review.getStars(), reviewFromDB.getStars());
        assertEquals(review.getComment(), reviewFromDB.getComment());
        assertEquals(review.getTimeStamp(), reviewFromDB.getTimeStamp());
        assertEquals(review.getAuthor().getId(), reviewFromDB.getAuthor().getId());
        assertEquals(review.getBoardGame().getTitle(), reviewFromDB.getBoardGame().getTitle());
    }
}
