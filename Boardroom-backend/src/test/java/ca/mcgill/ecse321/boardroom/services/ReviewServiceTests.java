package ca.mcgill.ecse321.boardroom.services;

import ca.mcgill.ecse321.boardroom.dtos.ReviewCreationDto;
import ca.mcgill.ecse321.boardroom.exceptions.BoardroomException;
import ca.mcgill.ecse321.boardroom.model.*;
import ca.mcgill.ecse321.boardroom.repositories.BoardGameRepository;
import ca.mcgill.ecse321.boardroom.repositories.PersonRepository;
import ca.mcgill.ecse321.boardroom.repositories.ReviewRepository;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.matchers.Null;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceTests {

    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private PersonRepository personRepository;
    @Mock
    private BoardGameRepository boardGameRepository;

    @InjectMocks
    private ReviewService reviewService;

    private static final int VALID_STARS = 5;
    private static final String VALID_COMMENT = "Great game!";
    private static Person VALID_AUTHOR;
    private static BoardGame VALID_BOARD_GAME;
    private int authorId;
    private String boardGameName;

    @BeforeEach
    public void setup() {
        VALID_AUTHOR = new Person("validUser", "valid@email.com", "password", false);
        int authorId = VALID_AUTHOR.getId();
        VALID_BOARD_GAME = new BoardGame("Uno", "A fun card game", 2, 54321);
        String boardGameName = VALID_BOARD_GAME.getTitle();

        this.authorId = authorId;
        this.boardGameName = boardGameName;
    }

    @Test
    public void testCreateValidReview() {
        // Arrange
        when(personRepository.findById(authorId)).thenReturn(Optional.of(VALID_AUTHOR));
        when(boardGameRepository.findById(boardGameName)).thenReturn(Optional.of(VALID_BOARD_GAME));
        when(reviewRepository.save(any(Review.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ReviewCreationDto newReviewDto = new ReviewCreationDto(
                VALID_STARS, VALID_COMMENT, authorId, boardGameName
        );

        // Act
        Review createdReview = reviewService.createReview(newReviewDto);

        // Assert
        assertNotNull(createdReview);
        assertEquals(VALID_STARS, createdReview.getStars());
        assertEquals(VALID_COMMENT, createdReview.getComment());
        assertEquals(VALID_AUTHOR, createdReview.getAuthor());
        assertEquals(VALID_BOARD_GAME, createdReview.getBoardGame());

        verify(reviewRepository, times(1)).save(any(Review.class));
    }

    @Test
    public void testCreateReviewWithInvalidStars() {
        // Arrange: Rating below 1
        ReviewCreationDto invalidReviewDto = new ReviewCreationDto(0, VALID_COMMENT, authorId, boardGameName);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> reviewService.createReview(invalidReviewDto)
        );

        assertEquals("Rating must be between 1 and 5 stars.", exception.getMessage());

        // Verify that save was never called
        verify(reviewRepository, never()).save(any(Review.class));
    }

    @Test
    public void testCreateEventWithNonexistentBoardGame() {
        when(personRepository.findById(authorId)).thenReturn(Optional.of(VALID_AUTHOR));
        when(boardGameRepository.findById("NonexistentGame")).thenReturn(Optional.empty());

        ReviewCreationDto invalidReviewDto = new ReviewCreationDto(
                VALID_STARS, VALID_COMMENT, authorId, "NonexistentGame"
        );

        BoardroomException exception = assertThrows(
                BoardroomException.class,
                () -> reviewService.createReview(invalidReviewDto)
        );

        assertEquals("A board game with this name does not exist", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());

        verify(reviewRepository, never()).save(any(Review.class));
    }

    @Test
    public void testCreateEventWithNonexistentAuthor() {
        when(personRepository.findById(999)).thenReturn(Optional.empty());

        ReviewCreationDto invalidReviewDto = new ReviewCreationDto(
                VALID_STARS, VALID_COMMENT, 999, boardGameName
        );

        BoardroomException exception = assertThrows(
                BoardroomException.class,
                () -> reviewService.createReview(invalidReviewDto)
        );

        assertEquals("A person with this id does not exist", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());

        verify(reviewRepository, never()).save(any(Review.class));
    }
}
