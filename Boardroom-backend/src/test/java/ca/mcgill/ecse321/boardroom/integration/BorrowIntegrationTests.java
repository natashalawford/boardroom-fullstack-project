package ca.mcgill.ecse321.boardroom.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
import org.springframework.cglib.core.Local;
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

    private final LocalDateTime VALID_REQUEST_START = LocalDateTime.now();
    private final LocalDateTime VALID_REQUEST_END = LocalDateTime.now().plusDays(7);
    private int VALID_PERSON_ID;
    private int VALID_SPECIFIC_GAME_ID;
    private int VALID_BORROW_REQUEST_ID;


    @BeforeEach
    public void setup() {
        person = new Person("John Doe", "john.doe@gmail.com", "password", false);
        person = personRepository.save(person);
       
        boardGame = new BoardGame("Monopoly", "A game about buying properties", 2, 1234);
        boardGame = boardGameRepository.save(boardGame);

        specificBoardGame = new SpecificBoardGame(12345, "Good quality no rips",GameStatus.AVAILABLE, boardGame, person);
        specificBoardGame = specificBoardGameRepository.save(specificBoardGame);

        borrowRequest = new BorrowRequest(RequestStatus.RETURNED, LocalDateTime.now().minusDays(10), LocalDateTime.now().minusDays(5), person, specificBoardGame);
        borrowRequest = borrowRequestRepository.save(borrowRequest);

        VALID_PERSON_ID = person.getId();
        VALID_SPECIFIC_GAME_ID = specificBoardGame.getId();
        VALID_BORROW_REQUEST_ID = borrowRequest.getId();
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
                RequestStatus.PENDING,
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
        assertEquals(RequestStatus.PENDING, createdBorrowRequest.getStatus());
        assertEquals(VALID_REQUEST_START, createdBorrowRequest.getRequestStartDate());
        assertEquals(VALID_REQUEST_END, createdBorrowRequest.getRequestEndDate());
        assertEquals(VALID_PERSON_ID, createdBorrowRequest.getPersonId());
        assertEquals(VALID_SPECIFIC_GAME_ID, createdBorrowRequest.getSpecificBoardGameId());
    }

    @Test
    @Order(1)
    public void testUpdateBorrowRequest() {
        // Arrange
        RequestStatus newStatus = RequestStatus.ACCEPTED;
        String url = "/borrowRequests/" + VALID_BORROW_REQUEST_ID;

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
}
