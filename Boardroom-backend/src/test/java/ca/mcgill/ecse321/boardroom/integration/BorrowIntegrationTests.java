package ca.mcgill.ecse321.boardroom.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
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
import ca.mcgill.ecse321.boardroom.dtos.responses.BorrowRequestResponseDto;
import ca.mcgill.ecse321.boardroom.dtos.ErrorDto;
import ca.mcgill.ecse321.boardroom.dtos.creation.BorrowRequestDtoCreation;

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
    private final LocalDateTime VALID_REQUEST_START = LocalDateTime.now().plusDays(1);
    private final LocalDateTime VALID_REQUEST_END = LocalDateTime.now().plusDays(7);
    private int validPersonId;
    private int validSpecificGameId;
    private int validBorrowRequestId;

    @BeforeAll
    public void setup() {
        person = new Person("John Doe", "john.doe@gmail.com", "password", false);
        person = personRepository.save(person);

        boardGame = new BoardGame("Monopoly", "A game about buying properties", 2, 1234);
        boardGame = boardGameRepository.save(boardGame);

        specificBoardGame = new SpecificBoardGame(12345, "Good quality no rips", GameStatus.AVAILABLE, boardGame,
                person);
        specificBoardGame = specificBoardGameRepository.save(specificBoardGame);

        borrowRequest = new BorrowRequest(VALID_STATUS, VALID_REQUEST_START, VALID_REQUEST_END, person,
                specificBoardGame);
        borrowRequest = borrowRequestRepository.save(borrowRequest);

        validPersonId = person.getId();
        validSpecificGameId = specificBoardGame.getId();
        validBorrowRequestId = borrowRequest.getId();
    }

    @AfterAll
    public void cleanup() {
        borrowRequestRepository.deleteAll();
        specificBoardGameRepository.deleteAll();
        boardGameRepository.deleteAll();
        personRepository.deleteAll();
    }

    @Test
    @Order(0)
    public void testViewPendingBorrowRequests() {
        // Arrange
        String url = "/borrowRequests";

        // Act
        ResponseEntity<List<BorrowRequestResponseDto>> response = client.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<BorrowRequestResponseDto>>() {
                });

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode(), "HTTP status should be 200 OK");
        List<BorrowRequestResponseDto> pendingRequests = response.getBody();
        assertNotNull(pendingRequests, "The list of pending requests must not be null");

        assertEquals(1, pendingRequests.size(), "There should be exactly 1 pending request in the repository");

        for (BorrowRequestResponseDto dto : pendingRequests) {
            assertEquals(validBorrowRequestId, dto.getId(), "BorrowRequest ID should match");
            assertEquals(VALID_STATUS, dto.getStatus(), "BorrowRequest should have the PENDING status");

            // Truncate both expected and actual to milliseconds due to truncation
            // mistmatches:
            LocalDateTime expectedStart = VALID_REQUEST_START.truncatedTo(ChronoUnit.MILLIS);
            LocalDateTime actualStart = dto.getRequestStartDate().truncatedTo(ChronoUnit.MILLIS);
            assertEquals(expectedStart, actualStart, "Request start date/time (truncated to millis) should match");

            LocalDateTime expectedEnd = VALID_REQUEST_END.truncatedTo(ChronoUnit.MILLIS);
            LocalDateTime actualEnd = dto.getRequestEndDate().truncatedTo(ChronoUnit.MILLIS);
            assertEquals(expectedEnd, actualEnd, "Request end date/time (truncated to millis) should match");

            assertEquals(validPersonId, dto.getPersonId(), "Person ID should match");
            assertEquals(validSpecificGameId, dto.getSpecificBoardGameId(), "SpecificBoardGame ID should match");
        }

    }

        @Test
        @Order(1)
        public void testCreateBorrowRequestWithPastStartTime() {
        // Arrange
        BorrowRequestDtoCreation borrowRequestDtoCreation = new BorrowRequestDtoCreation(
                VALID_STATUS,
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now().plusDays(5),
                validPersonId,
                validSpecificGameId
        );

        // Act
        ResponseEntity<ErrorDto> response = client.postForEntity("/borrowRequests", borrowRequestDtoCreation, ErrorDto.class);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertIterableEquals(
                List.of("Start time cannot be in the past"),
                response.getBody().getErrors());
        }

        @Test
        @Order(2)
        public void testCreateBorrowRequestWithEndTimeBeforeStartTime() {
        // Arrange
        BorrowRequestDtoCreation borrowRequestDtoCreation = new BorrowRequestDtoCreation(
                VALID_STATUS,
                LocalDateTime.now().plusDays(5), 
                LocalDateTime.now().plusDays(1), 
                validPersonId,
                validSpecificGameId
        );

        // Act
        ResponseEntity<ErrorDto> response = client.postForEntity("/borrowRequests", borrowRequestDtoCreation, ErrorDto.class);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertIterableEquals(
                List.of("End time must be after start time"),
                response.getBody().getErrors());
    }


    @Test
    @Order(3)
    public void testCreateBorrowRequest() {
        // Arrange
        BorrowRequestDtoCreation borrowRequestCreationDto = new BorrowRequestDtoCreation(
                VALID_STATUS,
                VALID_REQUEST_START,
                VALID_REQUEST_END,
                validPersonId,
                validSpecificGameId);

        // Act
        ResponseEntity<BorrowRequestResponseDto> response = client.postForEntity(
                "/borrowRequests",
                borrowRequestCreationDto,
                BorrowRequestResponseDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        BorrowRequestResponseDto createdBorrowRequest = response.getBody();
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
    @Order(4)
    public void testUpdateBorrowRequest() {
        // Arrange
        RequestStatus newStatus = RequestStatus.ACCEPTED;
        String url = "/borrowRequests/" + validBorrowRequestId;

        // Act
        ResponseEntity<BorrowRequestResponseDto> response = client.exchange(
                url,
                HttpMethod.PUT,
                new HttpEntity<>(newStatus),
                BorrowRequestResponseDto.class);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(newStatus, response.getBody().getStatus());
    }

    @Test
    @Order(5)
    public void testUpdateInvalidBorrowRequestStatus() {
        // Arrange
        int invalidId = 999999;
        RequestStatus newStatus = RequestStatus.ACCEPTED;
        String url = "/borrowRequests/" + invalidId;

        HttpEntity<RequestStatus> requestEntity = new HttpEntity<>(newStatus);

        // Act
        ResponseEntity<ErrorDto> response = client.exchange(
                url,
                HttpMethod.PUT,
                requestEntity,
                ErrorDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(),
                "Should return 404 if the borrow request ID does not exist.");

        ErrorDto errorDto = response.getBody();
        assertNotNull(errorDto, "Response body (ErrorDto) must not be null for an error.");

        String actualErrorMessage = errorDto.getErrors().get(0).replace("[", "").replace("]", "");

        assertEquals("A borrow request with this id does not exist", actualErrorMessage,
                "Expected error message for non-existent borrow request ID.");
    }

    @Test
    @Order(6)
    public void testCreateBorrowRequestInvalidPerson() {
        // Arrange
        int invalidPersonId = 999999;
        BorrowRequestDtoCreation borrowRequestDtoCreation = new BorrowRequestDtoCreation(
                VALID_STATUS,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(5),
                invalidPersonId,
                validSpecificGameId);

        // Act
        ResponseEntity<ErrorDto> response = client.postForEntity(
                "/borrowRequests",
                borrowRequestDtoCreation,
                ErrorDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(),
                "Should return 404 when the person ID does not exist.");

        ErrorDto errorDto = response.getBody();
        assertNotNull(errorDto, "Error response body (ErrorDto) should not be null.");

        String actualErrorMessage = errorDto.getErrors().get(0).replace("[", "").replace("]", "");
        assertEquals("A person with this id does not exist", actualErrorMessage,
                "Expected error message about nonexistent person ID");
    }

    @Test
    @Order(7)
    public void testCreateBorrowRequestInvalidSpecificBoardGame() {
        // Arrange
        int invalidBoardGameId = 888888;
        BorrowRequestDtoCreation body = new BorrowRequestDtoCreation(
                VALID_STATUS,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(5),
                validPersonId,
                invalidBoardGameId);

        // Act
        ResponseEntity<ErrorDto> response = client.postForEntity(
                "/borrowRequests",
                body,
                ErrorDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(),
                "Should return 404 when the specific board game ID does not exist.");

        ErrorDto errorDto = response.getBody();
        assertNotNull(errorDto, "Error response body (ErrorDto) should not be null.");

        String actualErrorMessage = errorDto.getErrors().get(0).replace("[", "").replace("]", "");
        assertEquals("A specific board game with this id does not exist", actualErrorMessage,
                "Expected error message about nonexistent specific board game ID");
    }

    @Test
    @Order(8)
    public void testGetValidBorrowRequestById() {
        // Arrange
        String url = "/borrowRequests/" + this.validBorrowRequestId;

        // Act
        ResponseEntity<BorrowRequestResponseDto> response = client.getForEntity(url, BorrowRequestResponseDto.class);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode(),
                "Expected 200 OK for an existing borrow request");
        BorrowRequestResponseDto dto = response.getBody();
        assertNotNull(dto, "Response body must not be null");
        assertEquals(validBorrowRequestId, dto.getId(), "The IDs must match");
    }

    @Test
    @Order(9)
    public void testGetInvalidBorrowRequestById() {
        // Arrange
        int nonExistentId = 999999;
        String url = "/borrowRequests/" + nonExistentId;

        // Act
        ResponseEntity<String> response = client.getForEntity(url, String.class);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(),
                "Should return 404 if the borrow request does not exist");
        String body = response.getBody();
        assertNotNull(body, "Error response should not be null");
        assertTrue(body.contains("A borrow request with this id (999999) does not exist"),
                "Error message should mention the missing ID");
    }

    @Test
    @Order(10)
    public void testViewLendingHistoryByBoardGame() {
        // Arrange
        String url = "/borrowRequests/history/" + validSpecificGameId;

        // Update the borrow request status to RETURNED to simulate lending history
        borrowRequest.setStatus(RequestStatus.RETURNED);
        borrowRequest = borrowRequestRepository.save(borrowRequest);

        // Act
        ResponseEntity<List<BorrowRequestResponseDto>> response = client.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<BorrowRequestResponseDto>>() {
                });

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode(), "HTTP status should be 200 OK");
        List<BorrowRequestResponseDto> lendingHistory = response.getBody();
        assertNotNull(lendingHistory, "The lending history list must not be null");
        assertEquals(1, lendingHistory.size(), "There should be exactly 1 returned borrow request in the history");

        BorrowRequestResponseDto dto = lendingHistory.get(0);
        assertEquals(validBorrowRequestId, dto.getId(), "BorrowRequest ID should match");
        assertEquals(RequestStatus.RETURNED, dto.getStatus(), "BorrowRequest should have the RETURNED status");
        assertEquals(validPersonId, dto.getPersonId(), "Person ID should match");
        assertEquals(validSpecificGameId, dto.getSpecificBoardGameId(), "SpecificBoardGame ID should match");
    }

    @Test
    @Order(11)
    public void testDeleteValidBorrowRequest() {
        // Arrange
        String url = "/borrowRequests/" + this.validBorrowRequestId;

        // Act
        ResponseEntity<Void> response = client.exchange(url, HttpMethod.DELETE, null, Void.class);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        // Assert
        ResponseEntity<ErrorDto> getResponse = client.getForEntity(url, ErrorDto.class);
        assertEquals(HttpStatus.NOT_FOUND, getResponse.getStatusCode(),
                "Expected 404 NOT_FOUND after deleting the borrow request");
    }

    @Test
    @Order(12)
    public void testDeleteInvalidBorrowRequest() {
        // Arrange
        int nonExistentId = 999999;
        String url = "/borrowRequests/" + nonExistentId;

        // Act
        ResponseEntity<String> response = client.exchange(
                url,
                HttpMethod.DELETE,
                null,
                String.class);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(),
                "Should return 404 if borrow request does not exist.");
        String body = response.getBody();
        assertNotNull(body, "Error response body should not be null.");
        assertTrue(body.contains("A borrow request with this id (999999) does not exist"),
                "Expected error message to contain 'A borrow request with this id (999999) does not exist'.");
    }

}
