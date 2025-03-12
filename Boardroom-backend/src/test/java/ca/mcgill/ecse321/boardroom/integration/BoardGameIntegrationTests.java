package ca.mcgill.ecse321.boardroom.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import ca.mcgill.ecse321.boardroom.dtos.responses.BoardGameResponseDto;
import ca.mcgill.ecse321.boardroom.dtos.responses.SpecificBoardGameResponseDto;
import ca.mcgill.ecse321.boardroom.model.BoardGame;
import ca.mcgill.ecse321.boardroom.model.Person;
import ca.mcgill.ecse321.boardroom.model.SpecificBoardGame;
import ca.mcgill.ecse321.boardroom.model.enums.GameStatus;
import ca.mcgill.ecse321.boardroom.repositories.BoardGameRepository;
import ca.mcgill.ecse321.boardroom.repositories.PersonRepository;
import ca.mcgill.ecse321.boardroom.repositories.SpecificBoardGameRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
public class BoardGameIntegrationTests {
    @Autowired
	private TestRestTemplate client;

    @Autowired
    private PersonRepository personRepo;
    @Autowired
    private BoardGameRepository boardGameRepo;
    @Autowired
    private SpecificBoardGameRepository specificBoardGameRepo;

    private static Person VALID_OWNER;
    private static BoardGame VALID_BOARD_GAME;
    private static SpecificBoardGame VALID_SPECIFIC_BOARD_GAME;
    
    // TODO: Replace these dummy values with the actual values from the endpoint when it is created
    private String boardGameTitle;
    private int specificBoardGameId;

    @BeforeEach
    public void setup() {
        VALID_OWNER = new Person("John", "name@mail.com", "securepass", true);
        personRepo.save(VALID_OWNER);

        VALID_BOARD_GAME = new BoardGame("Uno", "A fun card game", 2, 54321);
        boardGameRepo.save(VALID_BOARD_GAME);
        boardGameTitle = VALID_BOARD_GAME.getTitle();

        VALID_SPECIFIC_BOARD_GAME = new SpecificBoardGame(1234, "Good condition", GameStatus.AVAILABLE, VALID_BOARD_GAME, VALID_OWNER);
        specificBoardGameRepo.save(VALID_SPECIFIC_BOARD_GAME);
        specificBoardGameId = VALID_SPECIFIC_BOARD_GAME.getId();
    }

    @AfterEach
    public void cleanup() {
        specificBoardGameRepo.deleteAll();
        boardGameRepo.deleteAll();
        personRepo.deleteAll();
    }

    @Test
	@Order(0)
	public void createValidBoardGame() {
		// TODO: Update board game title with the value from the endpoint
	}

	@Test
	@Order(1)
	public void findBoardGames() {
		// Arrange
		String url = "/boardgames/general/";

		// Act
        ResponseEntity<List<BoardGameResponseDto>> response = client.exchange(
            url,
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<BoardGameResponseDto>>() {}
        );

		// Assert
		assertEquals(HttpStatus.OK, response.getStatusCode());
		List<BoardGameResponseDto> responseBody = response.getBody();
		assertNotNull(responseBody);
        assertTrue(responseBody.size() > 0);
        
        // TODO: Update the following assertions with the actual values from the endpoint
        BoardGameResponseDto boardGameResponse = responseBody.get(0);
        assertEquals(VALID_BOARD_GAME.getTitle(), boardGameResponse.getTitle());
        assertEquals(VALID_BOARD_GAME.getDescription(), boardGameResponse.getDescription());
        assertEquals(VALID_BOARD_GAME.getPlayersNeeded(), boardGameResponse.getPlayersNeeded());
        assertEquals(VALID_BOARD_GAME.getPicture(), boardGameResponse.getPicture());
	}

    @Test
    @Order(2)
    public void findSpecificBoardGames() {
        // Arrange
        String url = "/boardgames/specific/";

        // Act
        ResponseEntity<List<SpecificBoardGameResponseDto>> response = client.exchange(
            url,
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<SpecificBoardGameResponseDto>>() {}
        );

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<SpecificBoardGameResponseDto> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertTrue(responseBody.size() > 0);

        SpecificBoardGameResponseDto specificBoardGameResponse = responseBody.get(0);
        assertEquals(VALID_SPECIFIC_BOARD_GAME.getId(), specificBoardGameResponse.getId());
        assertEquals(VALID_SPECIFIC_BOARD_GAME.getDescription(), specificBoardGameResponse.getDescription());
        assertEquals(VALID_SPECIFIC_BOARD_GAME.getOwner().getId(), specificBoardGameResponse.getPersonId());
        assertEquals(VALID_SPECIFIC_BOARD_GAME.getPicture(), specificBoardGameResponse.getPicture());
        assertEquals(VALID_SPECIFIC_BOARD_GAME.getStatus(), specificBoardGameResponse.getStatus());
        assertEquals(VALID_SPECIFIC_BOARD_GAME.getBoardGame().getTitle(), specificBoardGameResponse.getBoardGameTitle());
    }

    @Test
    @Order(3)
    public void findBoardGameByTitle() {
        // Arrange
        String url = "/boardgames/general/" + VALID_BOARD_GAME.getTitle();

        // Act
        ResponseEntity<BoardGameResponseDto> response = client.getForEntity(url, BoardGameResponseDto.class);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        BoardGameResponseDto responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(VALID_BOARD_GAME.getTitle(), responseBody.getTitle());
        assertEquals(VALID_BOARD_GAME.getDescription(), responseBody.getDescription());
        assertEquals(VALID_BOARD_GAME.getPlayersNeeded(), responseBody.getPlayersNeeded());
        assertEquals(VALID_BOARD_GAME.getPicture(), responseBody.getPicture());
    }

    @Test
    @Order(4)
    public void findSpecificBoardGameById() {
        // Arrange
        String url = "/boardgames/specific/" + VALID_SPECIFIC_BOARD_GAME.getId();

        // Act
        ResponseEntity<SpecificBoardGameResponseDto> response = client.getForEntity(url, SpecificBoardGameResponseDto.class);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        SpecificBoardGameResponseDto responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(VALID_SPECIFIC_BOARD_GAME.getId(), responseBody.getId());
        assertEquals(VALID_SPECIFIC_BOARD_GAME.getDescription(), responseBody.getDescription());
        assertEquals(VALID_SPECIFIC_BOARD_GAME.getOwner().getId(), responseBody.getPersonId());
        assertEquals(VALID_SPECIFIC_BOARD_GAME.getPicture(), responseBody.getPicture());
        assertEquals(VALID_SPECIFIC_BOARD_GAME.getStatus(), responseBody.getStatus());
        assertEquals(VALID_SPECIFIC_BOARD_GAME.getBoardGame().getTitle(), responseBody.getBoardGameTitle());
    }
}
