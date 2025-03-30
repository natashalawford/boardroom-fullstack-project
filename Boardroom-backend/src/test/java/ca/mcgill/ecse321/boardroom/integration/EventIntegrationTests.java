package ca.mcgill.ecse321.boardroom.integration;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import ca.mcgill.ecse321.boardroom.repositories.BoardGameRepository;
import ca.mcgill.ecse321.boardroom.repositories.EventRepository;
import ca.mcgill.ecse321.boardroom.repositories.PersonRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import ca.mcgill.ecse321.boardroom.dtos.ErrorDto;
import ca.mcgill.ecse321.boardroom.dtos.creation.EventCreationDto;
import ca.mcgill.ecse321.boardroom.dtos.responses.EventResponseDto;
import ca.mcgill.ecse321.boardroom.model.Person;
import ca.mcgill.ecse321.boardroom.model.BoardGame;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
public class EventIntegrationTests {
    @Autowired
    private TestRestTemplate client;
    private int createdEventId;

    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private BoardGameRepository boardGameRepository;
    @Autowired
    private EventRepository eventRepository;

    private static final String VALID_TITLE = "Ticket to Ride at Little Italy";
    private static final String VALID_DESCRIPTION = "build railways across the country";
    private static final LocalDateTime VALID_START_TIME = LocalDateTime.now().plusDays(7);
    private static final LocalDateTime VALID_END_TIME = LocalDateTime.now().plusDays(7).plusHours(3);
    private static final int VALID_MAX_PARTICIPANTS = 8;
    private static String VALID_LOCATION = "7890 Little Italy";
    private static Person VALID_HOST;
    private static BoardGame VALID_BOARD_GAME;
    private int hostId;
    private String boardGameName;

    @BeforeAll
    public void setup() {
        VALID_HOST = new Person("emma davis", "emma.davis@mail.com", "mysecurepass", true);
        personRepository.save(VALID_HOST);
        hostId = VALID_HOST.getId();

        VALID_BOARD_GAME = new BoardGame("Ticket to Ride", "a game of building railways", 5, 2345678);
        boardGameRepository.save(VALID_BOARD_GAME);
        boardGameName = VALID_BOARD_GAME.getTitle();
    }

    @AfterAll
    public void cleanup() {
        // eventRepository.deleteAll();
        // boardGameRepository.deleteAll();
        // personRepository.deleteAll();
    }

    @Test
    @Order(0)
    public void testCreateValidEvent() {
        // Arrange
        EventCreationDto body = new EventCreationDto(
                VALID_TITLE, VALID_DESCRIPTION, VALID_START_TIME, VALID_END_TIME,
                VALID_MAX_PARTICIPANTS, VALID_LOCATION, hostId, boardGameName
        );

        // Act
        ResponseEntity<EventResponseDto> response = client.postForEntity("/events", body, EventResponseDto.class);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getId() > 0, "The ID should be a positive integer");
        this.createdEventId = response.getBody().getId();
        assertEquals(body.getTitle(), response.getBody().getTitle());
        assertEquals(body.getDescription(), response.getBody().getDescription());
        assertEquals(body.getStartDateTime(), response.getBody().getStartDateTime());
        assertEquals(body.getEndDateTime(), response.getBody().getEndDateTime());
        assertEquals(body.getMaxParticipants(), response.getBody().getMaxParticipants());
        assertEquals(body.getLocation(), response.getBody().getLocation());

        int responsePersonId = response.getBody().getHostId();
        assertEquals(body.getHostId(), responsePersonId);

        String responseGame = response.getBody().getBoardGameName();
        assertEquals(body.getBoardGameName(), responseGame);
    }

    @Test
    @Order(1)
    public void testCreateEventWithPastStartTime() {
        // Arrange
        EventCreationDto body = new EventCreationDto(
                VALID_TITLE, VALID_DESCRIPTION, LocalDateTime.now().minusDays(1), VALID_END_TIME,
                VALID_MAX_PARTICIPANTS, VALID_LOCATION, hostId, boardGameName
        );

        // Act
        ResponseEntity<ErrorDto> response = client.postForEntity("/events", body, ErrorDto.class);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertIterableEquals(
                List.of("Start time cannot be in the past"),
                response.getBody().getErrors());
    }

    @Test
    @Order(2)
    public void testCreateEventWithNonExistentHost() {
        // Arrange: Using an invalid location ID (non-existent)
        EventCreationDto body = new EventCreationDto(
                VALID_TITLE, VALID_DESCRIPTION, VALID_START_TIME, VALID_END_TIME,
                VALID_MAX_PARTICIPANTS, VALID_LOCATION, 99999, boardGameName
        );

        // Act
        ResponseEntity<ErrorDto> response = client.postForEntity("/events", body, ErrorDto.class);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertIterableEquals(
                List.of("A person with this id does not exist"),
                response.getBody().getErrors());
    }

    @Test
    @Order(3)
    public void testGetValidEvent() {
        // Arrange
        String url = String.format("/events/%d", this.createdEventId);

        // Act
        ResponseEntity<EventResponseDto> getResponse = client.getForEntity(url, EventResponseDto.class);

        // Assert
        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        assertNotNull(getResponse.getBody());
        assertEquals(this.createdEventId, getResponse.getBody().getId());
        assertEquals(VALID_TITLE, getResponse.getBody().getTitle());
    }

    @Test
    @Order(4)
    public void testGetUnvalidEvent() {
        // Act
        int invalidEventId = 99999;
        ResponseEntity<ErrorDto> response = client.getForEntity("/events/" + invalidEventId, ErrorDto.class);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertIterableEquals(
                List.of("no event has ID " + invalidEventId),
                response.getBody().getErrors()
        );
    }

    @Test
    @Order(5)
    public void testGetAllEvents() {
        // Arrange

        // Act: Retrieve all events
        ResponseEntity<EventResponseDto[]> response = client.getForEntity("/events", EventResponseDto[].class);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().length > 0, "There should be at least one event in the list.");
        assertEquals(this.createdEventId, response.getBody()[0].getId());
    }

    @Test
    @Order(6)
    public void testDeleteValidEvent() {
        // Arrange
        String url = String.format("/events/%d", this.createdEventId);

        // Act: Delete the event
        client.delete(url);

        // Assert: Ensure the event is deleted by checking retrieval
        ResponseEntity<ErrorDto> getResponse = client.getForEntity(url, ErrorDto.class);
        assertEquals(HttpStatus.NOT_FOUND, getResponse.getStatusCode());
    }

    @Test
    @Order(7)
    public void testDeleteUnvalidEvent() {
        // Act: Try to delete a non-existent event
        int invalidEventId = 99999;
        ResponseEntity<ErrorDto> response = client.exchange("/events/" + invalidEventId, HttpMethod.DELETE, null, ErrorDto.class);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertIterableEquals(
                List.of("no event has ID " + invalidEventId),
                response.getBody().getErrors()
        );
    }

}
