package ca.mcgill.ecse321.boardroom.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import ca.mcgill.ecse321.boardroom.model.BoardGame;
import ca.mcgill.ecse321.boardroom.model.BorrowRequest;
import ca.mcgill.ecse321.boardroom.model.Person;
import ca.mcgill.ecse321.boardroom.model.SpecificBoardGame;
import ca.mcgill.ecse321.boardroom.model.enums.GameStatus;
import ca.mcgill.ecse321.boardroom.model.enums.RequestStatus;
import ca.mcgill.ecse321.boardroom.repositories.BoardGameRepository;
import ca.mcgill.ecse321.boardroom.repositories.BorrowRequestRepository;
import ca.mcgill.ecse321.boardroom.repositories.PersonRepository;
import ca.mcgill.ecse321.boardroom.repositories.SpecificBoardGameRepository;
import ca.mcgill.ecse321.boardroom.dtos.BorrowRequestDtoCreation;
import ca.mcgill.ecse321.boardroom.dtos.BorrowRequestDtoSpecific;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
public class BorrowIntegrationTests {
    
    @Autowired
    private TestRestTemplate client;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private BoardGameRepository boardGameRepository;
    
    @Autowired
    private SpecificBoardGameRepository specificBoardGameRepository;
    
    @Autowired
    private BorrowRequestRepository borrowRequestRepository;

    private Person person;
    private BoardGame boardGame;
    private SpecificBoardGame specificBoardGame;
    private BorrowRequest borrowRequest;

    private final RequestStatus VALID_STATUS = RequestStatus.PENDING;
    private final LocalDateTime VALID_REQUEST_START = LocalDateTime.now();
    private final LocalDateTime VALID_REQUEST_END = LocalDateTime.now().plusDays(7);
    private int validPersonId;
    private int validSpecificGameId;
    private int validBorrowRequestId;


    @BeforeEach
    public void setup() {
        person = new Person("John Doe", "john.doe@gmail.com", "password", false);
        person = personRepository.save(person);
       
        boardGame = new BoardGame("Monopoly", "A game about buying properties", 2, 1234);
        boardGame = boardGameRepository.save(boardGame);

        specificBoardGame = new SpecificBoardGame(12345, "Good quality no rips",GameStatus.AVAILABLE, boardGame, person);
        specificBoardGame = specificBoardGameRepository.save(specificBoardGame);

        borrowRequest = new BorrowRequest(VALID_STATUS, VALID_REQUEST_START, VALID_REQUEST_END, person, specificBoardGame);
        borrowRequest = borrowRequestRepository.save(borrowRequest);

        validPersonId = person.getId();
        validSpecificGameId = specificBoardGame.getId();
        validBorrowRequestId = borrowRequest.getId();
    }

    @AfterEach
    public void cleanup() {
        borrowRequestRepository.deleteAll();
        specificBoardGameRepository.deleteAll();
        boardGameRepository.deleteAll();
        personRepository.deleteAll();
    }

    @Test
    @Order(0)
    public void testCreateBorrowRequest() {
        // Arrange
        BorrowRequestDtoCreation borrowRequestCreationDto = new BorrowRequestDtoCreation(
                VALID_STATUS,
                VALID_REQUEST_START,
                VALID_REQUEST_END,
                person.getId(),
                specificBoardGame.getId()
        );

        // Act
        ResponseEntity<BorrowRequestDtoSpecific> response = client.postForEntity(
                "/borrowRequests",
                borrowRequestCreationDto,
                BorrowRequestDtoSpecific.class
        );

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        BorrowRequestDtoSpecific createdBorrowRequest = response.getBody();
        assertNotNull(createdBorrowRequest);
        assertNotNull(createdBorrowRequest.getId());
        assertTrue(createdBorrowRequest.getId() > 0, "Response should have a positive ID.");
        assertEquals(VALID_STATUS, createdBorrowRequest.getStatus());
        assertEquals(VALID_REQUEST_START, createdBorrowRequest.getRequestStartDate());
        assertEquals(VALID_REQUEST_END, createdBorrowRequest.getRequestEndDate());
        assertEquals(validPersonId, createdBorrowRequest.getPersonId());
        assertEquals(validSpecificGameId, createdBorrowRequest.getSpecificBoardGameId());
    }

    @Test
    @Order(1)
    public void testUpdateBorrowRequest() {
        // Arrange
        RequestStatus newStatus = RequestStatus.ACCEPTED;
        String url = "/borrowRequests/" + validBorrowRequestId;

        // Act
        ResponseEntity<BorrowRequestDtoSpecific> response = client.exchange(
                url,
                HttpMethod.PUT,
                new HttpEntity<>(newStatus),
                BorrowRequestDtoSpecific.class
        );

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(newStatus, response.getBody().getStatus());
    }

    @Test
    @Order(2)
    public void testViewPendingBorrowRequests() {
        // Arrange
        String url = "/borrowRequests";

        // Act
        ResponseEntity<List<BorrowRequestDtoSpecific>> response = client.exchange(
            url,
            HttpMethod.GET,
            null, 
            new ParameterizedTypeReference<List<BorrowRequestDtoSpecific>>() {}
        );

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode(), "HTTP status should be 200 OK");
        List<BorrowRequestDtoSpecific> pendingRequests = response.getBody();
        assertNotNull(pendingRequests, "The list of pending requests must not be null");
        
        assertEquals(1, pendingRequests.size(), "There should be exactly 1 pending request in the repository");

        for (BorrowRequestDtoSpecific dto : pendingRequests) {
            assertEquals(validBorrowRequestId, dto.getId(), "BorrowRequest ID should match");
            assertEquals(VALID_STATUS, dto.getStatus(), "BorrowRequest should have the PENDING status");
            
            // Truncate both expected and actual to milliseconds due to truncation mistmatches:
            LocalDateTime expectedStart = VALID_REQUEST_START.truncatedTo(ChronoUnit.MILLIS);
            LocalDateTime actualStart   = dto.getRequestStartDate().truncatedTo(ChronoUnit.MILLIS);
            assertEquals(expectedStart, actualStart, "Request start date/time (truncated to millis) should match");

            LocalDateTime expectedEnd = VALID_REQUEST_END.truncatedTo(ChronoUnit.MILLIS);
            LocalDateTime actualEnd   = dto.getRequestEndDate().truncatedTo(ChronoUnit.MILLIS);
            assertEquals(expectedEnd, actualEnd, "Request end date/time (truncated to millis) should match");

            assertEquals(validPersonId, dto.getPersonId(), "Person ID should match");
            assertEquals(validSpecificGameId, dto.getSpecificBoardGameId(), "SpecificBoardGame ID should match");
        }

    } 

    @Test
    @Order(3)
    public void testUpdateInvalidBorrowRequestStatus() {
        // Arrange
        int invalidId = 999999; 
        RequestStatus newStatus = RequestStatus.ACCEPTED;
        String url = "/borrowRequests/" + invalidId;

        // Wrap the new status in HttpEntity
        HttpEntity<RequestStatus> requestEntity = new HttpEntity<>(newStatus);

        // Act
        // Capture the response as a string to parse the error message
        ResponseEntity<String> response = client.exchange(
                url,
                HttpMethod.PUT,
                requestEntity,
                String.class
        );

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(), 
                    "Should return 404 if the borrow request ID does not exist.");

        String responseBody = response.getBody();
        assertNotNull(responseBody, "Response body must not be null for an error.");

        assertTrue(
            responseBody.contains("A borrow request with this id does not exist"),
            "Expected error message to contain 'A borrow request with this id does not exist'"
        );
    }

    
}
