package ca.mcgill.ecse321.boardroom.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.AfterAll;
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

import ca.mcgill.ecse321.boardroom.dtos.BoardGameCreationDto;
import ca.mcgill.ecse321.boardroom.dtos.ErrorDto;
import ca.mcgill.ecse321.boardroom.dtos.SpecificBoardGameCreationDto;
import ca.mcgill.ecse321.boardroom.dtos.responses.BoardGameResponseDto;
import ca.mcgill.ecse321.boardroom.dtos.responses.SpecificBoardGameResponseDto;
import ca.mcgill.ecse321.boardroom.model.BoardGame;
import ca.mcgill.ecse321.boardroom.model.Person;
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

        // BoardGame fields
        private static final String VALID_BOARDGAME_TITLE = "Monopoly";
        private static final String VALID_DESCRIPTION = "This is a specific board game";
        private static final GameStatus VALID_STATUS = GameStatus.AVAILABLE;
        private static final int VALID_PLAYERS_NEEDED = 5;
        private static final int VALID_PICTURE = 15;

        // Person fields
        private static final String VALID_OWNER_NAME = "John Doe";
        private static final String VALID_OWNER_EMAIL = "john.doe@gmail.com";
        private static final String VALID_OWNER_PASSWORD = "1234";
        private static final boolean VALID_OWNER_ROLE = true;

        private static BoardGameResponseDto VALID_BOARD_GAME;
        private static SpecificBoardGameResponseDto VALID_SPECIFIC_BOARD_GAME;

        @AfterAll
        public void resetDatabase() {
                specificBoardGameRepo.deleteAll();
                boardGameRepo.deleteAll();
                personRepo.deleteAll();
        }

        @Test
        @Order(0)
        public void createValidBoardGame() {
                // Arrange
                BoardGameCreationDto boardGameToCreate = new BoardGameCreationDto(VALID_BOARDGAME_TITLE,
                                VALID_DESCRIPTION,
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
                VALID_BOARD_GAME = response.getBody();
        }

        @Test
        @Order(1)
        public void testCreateValidSpecificBoardGame() {
                // Arrange
                String url = "/specificboardgame";

                // Need to persist Person and BoardGame with same id and title for the service
                // method
                Person persistedPerson = personRepo
                                .save(new Person(VALID_OWNER_NAME, VALID_OWNER_EMAIL, VALID_OWNER_PASSWORD,
                                                VALID_OWNER_ROLE));
                BoardGame persistedBoardGame = boardGameRepo
                                .save(new BoardGame(VALID_BOARDGAME_TITLE, VALID_DESCRIPTION, VALID_PLAYERS_NEEDED,
                                                VALID_PICTURE));

                SpecificBoardGameCreationDto specificBoardGameToCreate = new SpecificBoardGameCreationDto(VALID_PICTURE,
                                VALID_DESCRIPTION, VALID_STATUS, persistedBoardGame.getTitle(),
                                persistedPerson.getId());

                // Act
                ResponseEntity<SpecificBoardGameResponseDto> response = client.postForEntity(url,
                                specificBoardGameToCreate,
                                SpecificBoardGameResponseDto.class);

                // Assert
                assertNotNull(response);
                assertEquals(HttpStatus.CREATED, response.getStatusCode());

                assertEquals(VALID_PICTURE, response.getBody().getPicture());
                assertEquals(VALID_DESCRIPTION, response.getBody().getDescription());
                assertEquals(VALID_STATUS, response.getBody().getStatus());
                assertEquals(persistedBoardGame.getTitle(), response.getBody().getBoardGameTitle());
                assertEquals(persistedPerson.getId(), response.getBody().getOwnerId());

                VALID_SPECIFIC_BOARD_GAME = response.getBody();
        }

        @Test
        @Order(2)
        public void findBoardGames() {
                // Arrange
                String url = "/boardgame";

                // Act
                ResponseEntity<List<BoardGameResponseDto>> response = client.exchange(
                                url,
                                HttpMethod.GET,
                                null,
                                new ParameterizedTypeReference<List<BoardGameResponseDto>>() {
                                });

                // Assert
                assertEquals(HttpStatus.OK, response.getStatusCode(), "HTTP status should be 200 OK");
                List<BoardGameResponseDto> responseBody = response.getBody();
                assertNotNull(responseBody, "Response body should not be null");

                assertEquals(1, responseBody.size(), "Response body should have 1 board game");

                BoardGameResponseDto boardGameResponse = responseBody.get(0);
                assertEquals(VALID_BOARD_GAME.getTitle(), boardGameResponse.getTitle());
                assertEquals(VALID_BOARD_GAME.getDescription(), boardGameResponse.getDescription());
                assertEquals(VALID_BOARD_GAME.getPlayersNeeded(), boardGameResponse.getPlayersNeeded());
                assertEquals(VALID_BOARD_GAME.getPicture(), boardGameResponse.getPicture());
        }

        @Test
        @Order(3)
        public void findSpecificBoardGames() {
                // Arrange
                String url = "/specificboardgame";

                // Act
                ResponseEntity<List<SpecificBoardGameResponseDto>> response = client.exchange(
                                url,
                                HttpMethod.GET,
                                null,
                                new ParameterizedTypeReference<List<SpecificBoardGameResponseDto>>() {
                                });

                // Assert
                assertEquals(HttpStatus.OK, response.getStatusCode());
                List<SpecificBoardGameResponseDto> responseBody = response.getBody();
                assertNotNull(responseBody);
                assertEquals(responseBody.size(), 1, "Response body should have 1 specific board game");

                SpecificBoardGameResponseDto specificBoardGameResponse = responseBody.get(0);
                assertEquals(VALID_SPECIFIC_BOARD_GAME.getId(), specificBoardGameResponse.getId());
                assertEquals(VALID_SPECIFIC_BOARD_GAME.getDescription(), specificBoardGameResponse.getDescription());
                assertEquals(VALID_SPECIFIC_BOARD_GAME.getOwnerId(), specificBoardGameResponse.getOwnerId());
                assertEquals(VALID_SPECIFIC_BOARD_GAME.getPicture(), specificBoardGameResponse.getPicture());
                assertEquals(VALID_SPECIFIC_BOARD_GAME.getStatus(), specificBoardGameResponse.getStatus());
                assertEquals(VALID_SPECIFIC_BOARD_GAME.getBoardGameTitle(),
                                specificBoardGameResponse.getBoardGameTitle());
        }

        @Test
        @Order(4)
        public void findBoardGameByTitle() {
                // Arrange
                String url = "/boardgame/" + VALID_BOARD_GAME.getTitle();

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
        @Order(5)
        public void findBoardGameByInvalidTitle() {
                // Arrange
                String url = "/boardgame/InvalidTitle";

                // Act
                ResponseEntity<ErrorDto> response = client.getForEntity(url, ErrorDto.class);

                // Assert
                assertNotNull(response);
                assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
                assertTrue(response.getBody().getErrors().contains("A board game type with this title does not exist"));
        }

        @Test
        @Order(6)
        public void findSpecificBoardGameById() {
                // Arrange
                String url = "/specificboardgame/" + VALID_SPECIFIC_BOARD_GAME.getId();

                // Act
                ResponseEntity<SpecificBoardGameResponseDto> response = client.getForEntity(url,
                                SpecificBoardGameResponseDto.class);

                // Assert
                assertEquals(HttpStatus.OK, response.getStatusCode());
                SpecificBoardGameResponseDto responseBody = response.getBody();
                assertNotNull(responseBody);
                assertEquals(VALID_SPECIFIC_BOARD_GAME.getId(), responseBody.getId());
                assertEquals(VALID_SPECIFIC_BOARD_GAME.getDescription(), responseBody.getDescription());
                assertEquals(VALID_SPECIFIC_BOARD_GAME.getOwnerId(), responseBody.getOwnerId());
                assertEquals(VALID_SPECIFIC_BOARD_GAME.getPicture(), responseBody.getPicture());
                assertEquals(VALID_SPECIFIC_BOARD_GAME.getStatus(), responseBody.getStatus());
                assertEquals(VALID_SPECIFIC_BOARD_GAME.getBoardGameTitle(), responseBody.getBoardGameTitle());
        }

        @Test
        @Order(7)
        public void findSpecificBoardGameByInvalidId() {
                // Arrange
                String url = "/specificboardgame/123";

                // Act
                ResponseEntity<ErrorDto> response = client.getForEntity(url,
                                ErrorDto.class);

                // Assert
                assertNotNull(response);
                assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
                assertTrue(response.getBody().getErrors()
                                .contains("A specific board game with this id does not exist"));
        }
}
