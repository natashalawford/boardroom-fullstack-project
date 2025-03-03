package ca.mcgill.ecse321.boardroom.integration;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import ca.mcgill.ecse321.boardroom.dtos.ErrorDto;
import ca.mcgill.ecse321.boardroom.dtos.ReviewCreationDto;
import ca.mcgill.ecse321.boardroom.dtos.ReviewResponseDto;
import ca.mcgill.ecse321.boardroom.model.BoardGame;
import ca.mcgill.ecse321.boardroom.model.Person;
import ca.mcgill.ecse321.boardroom.repositories.BoardGameRepository;
import ca.mcgill.ecse321.boardroom.repositories.PersonRepository;
import ca.mcgill.ecse321.boardroom.repositories.ReviewRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
public class ReviewIntegrationTests {

    @Autowired
    private TestRestTemplate client;
    private int createdReviewId;

    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private BoardGameRepository boardGameRepository;
    @Autowired
    private ReviewRepository reviewRepository;

    private static Person VALID_AUTHOR;
    private static BoardGame VALID_BOARD_GAME;

    @BeforeEach
    public void setup() {
        VALID_AUTHOR = new Person("Alice", "alice@mail.com", "securepass", false);
        personRepository.save(VALID_AUTHOR);

        VALID_BOARD_GAME = new BoardGame("Uno", "A fun card game", 2, 54321);
        boardGameRepository.save(VALID_BOARD_GAME);
    }

    @AfterEach
    public void cleanup() {
        reviewRepository.deleteAll();
        personRepository.deleteAll();
        boardGameRepository.deleteAll();
    }

    /**
     * Test creating a valid review.
     */
    @Test
    @Order(0)
    public void testCreateValidReview() {
        // Arrange
        ReviewCreationDto body = new ReviewCreationDto(
                5, "Great game, had a lot of fun!", VALID_AUTHOR, VALID_BOARD_GAME
        );

        // Act
        ResponseEntity<ReviewResponseDto> response = client.postForEntity("/reviews", body, ReviewResponseDto.class);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getId() > 0, "The ID should be a positive integer");
        this.createdReviewId = response.getBody().getId();
        assertEquals(body.getStars(), response.getBody().getStars());
        assertEquals(body.getComment(), response.getBody().getComment());

        assertEquals(body.getAuthor().getEmail(), response.getBody().getAuthor().getEmail());
        assertEquals(body.getAuthor().getName(), response.getBody().getAuthor().getName());

        assertEquals(body.getBoardGame().getTitle(), response.getBody().getBoardGame().getTitle());
        assertEquals(body.getBoardGame().getDescription(), response.getBody().getBoardGame().getDescription());
    }

    /**
     * Test creating a review with an invalid rating (below 1).
     */
    @Test
    @Order(1)
    public void testCreateReviewWithInvalidStars() {
        // Arrange
        ReviewCreationDto body = new ReviewCreationDto(
                0, "This review should fail.", VALID_AUTHOR, VALID_BOARD_GAME
        );

        // Act
        ResponseEntity<ErrorDto> response = client.postForEntity("/reviews", body, ErrorDto.class);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    /**
     * Test creating a review with a null comment.
     */
    @Test
    @Order(2)
    public void testCreateReviewWithNullComment() {
        // Arrange
        ReviewCreationDto body = new ReviewCreationDto(
                4, null, VALID_AUTHOR, VALID_BOARD_GAME
        );

        // Act
        ResponseEntity<ErrorDto> response = client.postForEntity("/reviews", body, ErrorDto.class);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
    }
}
