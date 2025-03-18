package ca.mcgill.ecse321.boardroom.integration;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import ca.mcgill.ecse321.boardroom.dtos.ErrorDto;
import ca.mcgill.ecse321.boardroom.dtos.creation.ReviewCreationDto;
import ca.mcgill.ecse321.boardroom.dtos.responses.ReviewResponseDto;
import ca.mcgill.ecse321.boardroom.model.BoardGame;
import ca.mcgill.ecse321.boardroom.model.Person;
import ca.mcgill.ecse321.boardroom.repositories.BoardGameRepository;
import ca.mcgill.ecse321.boardroom.repositories.PersonRepository;
import ca.mcgill.ecse321.boardroom.repositories.ReviewRepository;

import java.util.List;

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
    private static ReviewResponseDto VALID_REVIEW;
    private int authorId;
    private String boardGameName;

    @BeforeAll
    public void setup() {
        VALID_AUTHOR = new Person("validUser", "user@mail.com", "securepass", false);
        personRepository.save(VALID_AUTHOR);
        authorId = VALID_AUTHOR.getId();

        VALID_BOARD_GAME = new BoardGame("Uno", "A fun card game", 2, 54321);
        boardGameRepository.save(VALID_BOARD_GAME);
        boardGameName = VALID_BOARD_GAME.getTitle();
    }

    @AfterAll
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
                5, "Great game, had a lot of fun!", authorId, boardGameName
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

        int responsePersonId = response.getBody().getAuthorId();
        assertEquals(body.getAuthorId(), responsePersonId);

        String responseGame = response.getBody().getBoardGameName();
        assertEquals(body.getBoardGameName(), responseGame);
        VALID_REVIEW = response.getBody();
    }

    /**
     * Test creating a review with an invalid rating (below 1).
     */
    @Test
    @Order(1)
    public void testCreateReviewWithInvalidStars() {
        // Arrange
        ReviewCreationDto body = new ReviewCreationDto(
                0, "This review should fail.", authorId, boardGameName
        );

        // Act
        ResponseEntity<ErrorDto> response = client.postForEntity("/reviews", body, ErrorDto.class);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertIterableEquals(
                List.of("Rating must be at least 1 star."),
                response.getBody().getErrors());
    }

    /*
     * Test creating a review with invalid fields (non-existent board game).
     */
    @Test
    @Order(2)
    public void testCreateReviewWithInvalidFields() {
        // Arrange
        ReviewCreationDto body = new ReviewCreationDto(
                4, "this should fail", authorId, "NonExistentGame"
        );

        // Act
        ResponseEntity<ErrorDto> response = client.postForEntity("/reviews", body, ErrorDto.class);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertIterableEquals(
                List.of("A board game with this name does not exist"),
                response.getBody().getErrors());
    }

    /*
     * Test getting reviews for a specific board game.
     */
    @Test
    @Order(3)
    public void testGetReviewsForBoardGame() {
        // Arrange
        String url = "/reviews/" + boardGameName;

        // Act
        ResponseEntity<List<ReviewResponseDto>> reviewResponseBody = client.exchange(
            url,
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<ReviewResponseDto>>() {}
        );

        // Assert
        assertEquals(HttpStatus.OK, reviewResponseBody.getStatusCode());
        assertNotNull(reviewResponseBody.getBody());
        assertTrue(reviewResponseBody.getBody().size() > 0);
        ReviewResponseDto reviewResponse = reviewResponseBody.getBody().get(0);

        assertEquals(VALID_REVIEW.getAuthorId(), reviewResponse.getAuthorId());
        assertEquals(VALID_REVIEW.getStars(), reviewResponse.getStars());
        assertEquals(VALID_REVIEW.getComment(), reviewResponse.getComment());
        assertEquals(VALID_REVIEW.getBoardGameName(), reviewResponse.getBoardGameName());
    }

    /*
     * Test getting reviews for a board game with an invalid title.
     */
    @Test
    @Order(4)
    public void testGetReviewsForBoardGameWithInvalidTitle() {
        // Arrange
        String url = "/reviews/" + "Nonexistent-title";

        // Act
        ResponseEntity<List<ReviewResponseDto>> reviewResponseBody = client.exchange(
            url,
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<ReviewResponseDto>>() {}
        );

        assertEquals(HttpStatus.OK, reviewResponseBody.getStatusCode());
        assertNotNull(reviewResponseBody.getBody());
        assertTrue(reviewResponseBody.getBody().size() == 0);
    }

    @Test
    @Order(6)
    public void testDeleteReviewById_Success() {
        // Arrange
        String url = String.format("/reviews/%d", this.createdReviewId);

        // Act: Delete the event
        ResponseEntity<ErrorDto> response = client.exchange(url, HttpMethod.DELETE, null, ErrorDto.class);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    @Order(7)
    public void testDeleteReviewById_NotFound() {
        // Act: Try to delete a non-existent event
        int invalidReviewId = 99999;
        ResponseEntity<ErrorDto> response = client.exchange("/reviews/" + invalidReviewId, HttpMethod.DELETE, null, ErrorDto.class);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertIterableEquals(
                List.of("no review has ID " + invalidReviewId),
                response.getBody().getErrors()
        );
    }
}
