package ca.mcgill.ecse321.boardroom.integration;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import ca.mcgill.ecse321.boardroom.repositories.BoardGameRepository;
import ca.mcgill.ecse321.boardroom.repositories.EventRepository;
import ca.mcgill.ecse321.boardroom.repositories.LocationRepository;
import ca.mcgill.ecse321.boardroom.repositories.PersonRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import ca.mcgill.ecse321.boardroom.dtos.ErrorDto;
import ca.mcgill.ecse321.boardroom.dtos.EventCreationDto;
import ca.mcgill.ecse321.boardroom.dtos.EventResponseDto;
import ca.mcgill.ecse321.boardroom.model.Location;
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
    private LocationRepository locationRepository;

    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private BoardGameRepository boardGameRepository;
    @Autowired
    private EventRepository eventRepository;

    private static final String VALID_TITLE = "Board Game Night";
    private static final String VALID_DESCRIPTION = "A fun night with friends!";
    private static final LocalDateTime VALID_START_TIME = LocalDateTime.now().plusDays(1);
    private static final LocalDateTime VALID_END_TIME = LocalDateTime.now().plusDays(1).plusHours(2);
    private static final int VALID_MAX_PARTICIPANTS = 10;
    private static Location VALID_LOCATION;
    private static Person VALID_HOST;
    private static BoardGame VALID_BOARD_GAME;

    @BeforeEach
    public void setup() {
        VALID_LOCATION = new Location("McGill", "Montreal", "QC");
        locationRepository.save(VALID_LOCATION);
        VALID_HOST = new Person("Name", "name@mail.com", "securepass", false);
        personRepository.save(VALID_HOST);
        VALID_BOARD_GAME = new BoardGame("Uno", "A fun card game", 2, 54321);
        boardGameRepository.save(VALID_BOARD_GAME);
    }

    @AfterEach
    public void cleanup() {
        eventRepository.deleteAll();
        locationRepository.deleteAll();
        personRepository.deleteAll();
        boardGameRepository.deleteAll();
    }


    @Test
    @Order(0)
    public void testCreateValidEvent() {
        // Arrange
        EventCreationDto body = new EventCreationDto(
                VALID_TITLE, VALID_DESCRIPTION, VALID_START_TIME, VALID_END_TIME,
                VALID_MAX_PARTICIPANTS, VALID_LOCATION, VALID_HOST, VALID_BOARD_GAME
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

        Location responseLocation = response.getBody().getLocation();
        assertEquals(body.getLocation().getCity(), responseLocation.getCity());
        assertEquals(body.getLocation().getAddress(), responseLocation.getAddress());
        assertEquals(body.getLocation().getProvince(), responseLocation.getProvince());

        Person responsePerson = response.getBody().getHost();
        assertEquals(body.getHost().getEmail(), responsePerson.getEmail());
        assertEquals(body.getHost().getName(), responsePerson.getName());

        BoardGame responseGame = response.getBody().getBoardGame();
        assertEquals(body.getBoardGame().getDescription(), responseGame.getDescription());
        assertEquals(body.getBoardGame().getTitle(), responseGame.getTitle());
        assertEquals(body.getBoardGame().getPlayersNeeded(), responseGame.getPlayersNeeded());
        assertEquals(body.getBoardGame().getPicture(), responseGame.getPicture());
    }

    @Test
    @Order(1)
    public void testCreateEventWithPastStartTime() {
        // Arrange
        EventCreationDto body = new EventCreationDto(
                VALID_TITLE, VALID_DESCRIPTION, LocalDateTime.now().minusDays(1), VALID_END_TIME,
                VALID_MAX_PARTICIPANTS, VALID_LOCATION, VALID_HOST, VALID_BOARD_GAME
        );

        // Act
        ResponseEntity<ErrorDto> response = client.postForEntity("/events", body, ErrorDto.class);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
    }
}
