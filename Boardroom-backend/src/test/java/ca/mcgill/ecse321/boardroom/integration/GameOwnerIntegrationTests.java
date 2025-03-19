package ca.mcgill.ecse321.boardroom.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import ca.mcgill.ecse321.boardroom.dtos.ErrorDto;
import ca.mcgill.ecse321.boardroom.dtos.SpecificBoardGameRequestDto;
import ca.mcgill.ecse321.boardroom.dtos.creation.BoardGameCreationDto;
import ca.mcgill.ecse321.boardroom.dtos.creation.SpecificBoardGameCreationDto;
import ca.mcgill.ecse321.boardroom.dtos.responses.BoardGameResponseDto;
import ca.mcgill.ecse321.boardroom.dtos.responses.SpecificBoardGameResponseDto;
import ca.mcgill.ecse321.boardroom.model.BoardGame;
import ca.mcgill.ecse321.boardroom.model.Person;
import ca.mcgill.ecse321.boardroom.model.enums.GameStatus;
import ca.mcgill.ecse321.boardroom.repositories.BoardGameRepository;
import ca.mcgill.ecse321.boardroom.repositories.PersonRepository;
import ca.mcgill.ecse321.boardroom.repositories.SpecificBoardGameRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@MockitoSettings
public class GameOwnerIntegrationTests {
    @Autowired
    private TestRestTemplate client;

    @Autowired
    private SpecificBoardGameRepository specificBoardGameRepo;

    @Autowired
    private BoardGameRepository boardGameRepo;

    @Autowired
    private PersonRepository personRepo;

    // BoardGame fields
    private static final String VALID_BOARDGAME_TITLE = "Monopogy";
    private static final String VALID_DESCRIPTION = "This is a specific board game";
    private static final GameStatus VALID_STATUS = GameStatus.AVAILABLE;
    private static final int VALID_PLAYERS_NEEDED = 5;
    private static final int VALID_PICTURE = 15;

    private static int CREATED_SPECIFICBOARDGAME_ID;

    // Person fields
    private static final String VALID_OWNER_NAME = "John Doe";
    private static final String VALID_OWNER_EMAIL = "john.doe@gmail.com";
    private static final String VALID_OWNER_PASSWORD = "1234";
    private static final boolean VALID_OWNER_ROLE = true;

    @AfterAll
    public void resetDatabase() {
        specificBoardGameRepo.deleteAll();
        boardGameRepo.deleteAll();
        personRepo.deleteAll();
    }

    //should be testing all the service tests and also the ones that you didn't test

    @Test
    @SuppressWarnings("null")
    @Order(0)
    public void testCreateValidBoardGame() {
        // Arrange
        BoardGameCreationDto boardGameToCreate = new BoardGameCreationDto(VALID_BOARDGAME_TITLE, VALID_DESCRIPTION,
                VALID_PLAYERS_NEEDED, VALID_PICTURE);
        String url = "/boardgame";

        // Act
        ResponseEntity<BoardGameResponseDto> response = client.postForEntity(url, boardGameToCreate,
                BoardGameResponseDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        assertEquals(VALID_BOARDGAME_TITLE, response.getBody().getTitle());
        assertEquals(VALID_DESCRIPTION, response.getBody().getDescription());
        assertEquals(VALID_PLAYERS_NEEDED, response.getBody().getPlayersNeeded());
        assertEquals(VALID_PICTURE, response.getBody().getPicture());
    }



    @SuppressWarnings("null")
    @Test
    @Order(2)
    public void testCreateValidSpecificBoardGame() {
        // Arrange
        String url = "/specificboardgame";

        // Need to persist Person and BoardGame with same id and title for the service
        // method
        Person persistedPerson = personRepo
                .save(new Person(VALID_OWNER_NAME, VALID_OWNER_EMAIL, VALID_OWNER_PASSWORD, VALID_OWNER_ROLE));
        BoardGame persistedBoardGame = boardGameRepo
                .save(new BoardGame(VALID_BOARDGAME_TITLE, VALID_DESCRIPTION, VALID_PLAYERS_NEEDED, VALID_PICTURE));

        SpecificBoardGameCreationDto specificBoardGameToCreate = new SpecificBoardGameCreationDto(VALID_PICTURE,
                VALID_DESCRIPTION, VALID_STATUS, persistedBoardGame.getTitle(), persistedPerson.getId());

        // Act
        ResponseEntity<SpecificBoardGameResponseDto> response = client.postForEntity(url, specificBoardGameToCreate,
                SpecificBoardGameResponseDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        assertEquals(VALID_PICTURE, response.getBody().getPicture());
        assertEquals(VALID_DESCRIPTION, response.getBody().getDescription());
        assertEquals(VALID_STATUS, response.getBody().getStatus());
        assertEquals(persistedBoardGame.getTitle(), response.getBody().getBoardGameTitle());
        assertEquals(persistedPerson.getId(), response.getBody().getOwnerId());

        CREATED_SPECIFICBOARDGAME_ID = response.getBody().getId();
    }

    @Test
    @Order(2)
    public void testUpdateValidSpecificBoardgame() {
        // Arrange
        String url = "/specificboardgame/{id}";

        SpecificBoardGameRequestDto specificBoardGameToUpdate = new SpecificBoardGameRequestDto(VALID_DESCRIPTION,
                VALID_PICTURE, VALID_STATUS);

        // Act
        ResponseEntity<SpecificBoardGameResponseDto> response = client.exchange(url, HttpMethod.PUT,
                new HttpEntity<SpecificBoardGameRequestDto>(specificBoardGameToUpdate),
                SpecificBoardGameResponseDto.class, CREATED_SPECIFICBOARDGAME_ID);

        // Assert
        assertNotNull(response);

        assertEquals(HttpStatus.OK, response.getStatusCode());

    }

    @Test
    @Order(3)
    public void testDeleteSpecifiBoardGame() {
        //Arrange
        String url = "/specificboardgame/{id}";

        //Act
        ResponseEntity<Void> response = client.exchange(url, HttpMethod.DELETE, null, Void.class, CREATED_SPECIFICBOARDGAME_ID); 

        //Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        assertNull(specificBoardGameRepo.findSpecificBoardGameById(CREATED_SPECIFICBOARDGAME_ID));
    }


    //Jakarta validation test
    @Test
    public void testCreateInvalidTitleBoardGame() {
        //Arrange
        BoardGameCreationDto boardGameToCreate = new BoardGameCreationDto(" ", VALID_DESCRIPTION, VALID_PLAYERS_NEEDED, VALID_PICTURE);

        String url = "/boardgame";

        //Act
        ResponseEntity<ErrorDto> response = client.postForEntity(url, boardGameToCreate, ErrorDto.class);

        //Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode()); 
        assertEquals("Title is required", response.getBody().getErrors().get(0).replace("[", "").replace("]", ""));
    }

}
