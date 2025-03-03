package ca.mcgill.ecse321.boardroom.services;

import ca.mcgill.ecse321.boardroom.dtos.ReviewCreationDto;
import ca.mcgill.ecse321.boardroom.model.BoardGame;
import ca.mcgill.ecse321.boardroom.model.Person;
import ca.mcgill.ecse321.boardroom.model.Review;
import ca.mcgill.ecse321.boardroom.repositories.ReviewRepository;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.matchers.Null;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceTests {

    @Mock
    private ReviewRepository repo;

    @InjectMocks
    private ReviewService reviewService;

    private static final int VALID_STARS = 5;
    private static final String VALID_COMMENT = "Great game!";
    private static final Person VALID_AUTHOR = new Person("validUser", "valid@email.com", "password", false);
    private static final BoardGame VALID_BOARD_GAME = new BoardGame("Uno", "fun card game", 2, 12345);

    @Test
    public void testCreateValidReview() {
        // Arrange
        ReviewCreationDto newReviewDto = new ReviewCreationDto(
                VALID_STARS, VALID_COMMENT, VALID_AUTHOR, VALID_BOARD_GAME
        );

        when(repo.save(any(Review.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Review createdReview = reviewService.createReview(newReviewDto);

        // Assert
        assertNotNull(createdReview);
        assertEquals(VALID_STARS, createdReview.getStars());
        assertEquals(VALID_COMMENT, createdReview.getComment());
        assertEquals(VALID_AUTHOR, createdReview.getAuthor());
        assertEquals(VALID_BOARD_GAME, createdReview.getBoardGame());

        verify(repo, times(1)).save(any(Review.class));
    }

    @Test
    public void testCreateReviewWithInvalidStars() {
        // Arrange: Rating below 1
        ReviewCreationDto invalidReviewDto = new ReviewCreationDto(0, VALID_COMMENT, VALID_AUTHOR, VALID_BOARD_GAME);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> reviewService.createReview(invalidReviewDto)
        );

        assertEquals("Rating must be between 1 and 5 stars.", exception.getMessage());

        // Verify that save was never called
        verify(repo, never()).save(any(Review.class));
    }
}
