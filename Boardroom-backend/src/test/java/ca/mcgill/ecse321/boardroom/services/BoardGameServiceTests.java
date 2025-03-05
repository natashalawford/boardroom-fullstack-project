package ca.mcgill.ecse321.boardroom.services;

import ca.mcgill.ecse321.boardroom.model.*;
import ca.mcgill.ecse321.boardroom.model.enums.GameStatus;
import ca.mcgill.ecse321.boardroom.repositories.*;
import ca.mcgill.ecse321.boardroom.dtos.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

@ExtendWith(MockitoExtension.class)
public class BoardGameServiceTests {
        @Mock
        private PersonRepository personRepo;
        @Mock
        private BoardGameRepository boardGameRepo;
        @Mock
        private SpecificBoardGameRepository specificBoardGameRepo;
        @InjectMocks
        private BoardGameServiceJanelle boardGameService;

        // Fields for general board game
        private static final String VALID_TITLE = "Monopoly";
        private static final String VALID_DESCRIPTION = "Fun game for the whole family";
        private static final int VALID_PLAYERS_NEEDED = 4;
        private static final int VALID_GAME_PICTURE = 12345;

        // Fields for specific board game
        private static final int VALID_SPECIFIC_GAME_PICTURE = 23456;
        private static final String VALID_SPECIFIC_GAME_DESCRIPTION = "Good condition";
        private static final GameStatus VALID_GAME_STATUS = GameStatus.AVAILABLE;
        private static Person VALID_OWNER;
        private static BoardGame VALID_BOARD_GAME;
        private static SpecificBoardGame VALID_SPECIFIC_BOARD_GAME;

        @BeforeEach
        public void setMockOutput() {
                // Create valid objects
                VALID_OWNER = new Person("Alice", "alice@mail.com", "securepass", true);

                VALID_BOARD_GAME = new BoardGame(VALID_TITLE, VALID_DESCRIPTION, VALID_PLAYERS_NEEDED,
                                VALID_GAME_PICTURE);
                VALID_SPECIFIC_BOARD_GAME = new SpecificBoardGame(VALID_SPECIFIC_GAME_PICTURE,
                                VALID_SPECIFIC_GAME_DESCRIPTION, VALID_GAME_STATUS, VALID_BOARD_GAME, VALID_OWNER);

                // Mocking
                lenient().when(boardGameRepo.findBoardGameByTitle(anyString()))
                                .thenAnswer((InvocationOnMock invocation) -> {
                                        if (invocation.getArgument(0).equals(VALID_TITLE)) {
                                                return VALID_BOARD_GAME;
                                        } else {
                                                return null;
                                        }
                                });

                lenient().when(specificBoardGameRepo.findSpecificBoardGameById(anyInt()))
                                .thenAnswer((InvocationOnMock invocation) -> {
                                        if (invocation.getArgument(0).equals(VALID_SPECIFIC_BOARD_GAME.getId())) {
                                                return VALID_SPECIFIC_BOARD_GAME;
                                        } else {
                                                return null;
                                        }
                                });

                lenient().when(boardGameRepo.findAll()).thenAnswer((@SuppressWarnings("unused") InvocationOnMock invocation) -> {
                        ArrayList<BoardGame> list = new ArrayList<BoardGame>();
                        list.add(VALID_BOARD_GAME);
                        return list;
                });

                lenient().when(specificBoardGameRepo.findAll()).thenAnswer((@SuppressWarnings("unused") InvocationOnMock invocation) -> {
                        ArrayList<SpecificBoardGame> list = new ArrayList<SpecificBoardGame>();
                        list.add(VALID_SPECIFIC_BOARD_GAME);
                        return list;
                });

                lenient().when(personRepo.findById(anyInt()))
                                .thenAnswer((InvocationOnMock invocation) -> {
                                        if (invocation.getArgument(0).equals(VALID_OWNER.getId())) {
                                                return Optional.of(VALID_OWNER);
                                        } else {
                                                return Optional.empty();
                                        }
                                });

                Answer<?> returnParameterAsAnswer = (InvocationOnMock invocation) -> {
			return invocation.getArgument(0);
		};
		lenient().when(specificBoardGameRepo.save(any(SpecificBoardGame.class))).thenAnswer(returnParameterAsAnswer);
                lenient().when(boardGameRepo.save(any(BoardGame.class))).thenAnswer(returnParameterAsAnswer);
        }

        @Test
        public void testCreateValidGame() {
                // Arrange
                BoardGameCreationDtoJanelle newBoardGameDto = new BoardGameCreationDtoJanelle(
                                VALID_TITLE, VALID_DESCRIPTION, VALID_PLAYERS_NEEDED, VALID_GAME_PICTURE);
                BoardGame createdBoardGame = boardGameService.createBoardGame(newBoardGameDto);

                // Assert
                assertNotNull(createdBoardGame);
                assertEquals(VALID_TITLE, createdBoardGame.getTitle());
                assertEquals(VALID_DESCRIPTION, createdBoardGame.getDescription());
                assertEquals(VALID_PLAYERS_NEEDED, createdBoardGame.getPlayersNeeded());
                assertEquals(VALID_GAME_PICTURE, createdBoardGame.getPicture());
                verify(boardGameRepo, times(1)).save(any(BoardGame.class));
        }

        @Test
        public void testCreateValidSpecificGame() {
            // Arrange
            SpecificBoardGameCreationDtoJanelle newSpecificBoardGameDto = new SpecificBoardGameCreationDtoJanelle(
                            VALID_SPECIFIC_GAME_PICTURE, VALID_SPECIFIC_GAME_DESCRIPTION,
                            VALID_GAME_STATUS, VALID_TITLE, VALID_OWNER.getId());
            SpecificBoardGame createdSpecificBoardGame = boardGameService
                            .createSpecificBoardGame(newSpecificBoardGameDto);
        
            // Assert
            assertNotNull(createdSpecificBoardGame);
            assertEquals(VALID_SPECIFIC_GAME_PICTURE, createdSpecificBoardGame.getPicture());
            assertEquals(VALID_SPECIFIC_GAME_DESCRIPTION, createdSpecificBoardGame.getDescription());
            assertEquals(VALID_GAME_STATUS, createdSpecificBoardGame.getStatus());
            assertEquals(VALID_TITLE, createdSpecificBoardGame.getBoardGame().getTitle());
            assertEquals(VALID_OWNER, createdSpecificBoardGame.getOwner());
            verify(specificBoardGameRepo, times(1)).save(any(SpecificBoardGame.class));
        }

        @Test
        public void testGetBoardGameByTitle() {
                BoardGame boardGame = boardGameService.getBoardGameByTitle(VALID_TITLE);

                // Assert
                assertNotNull(boardGame);
                assertEquals(VALID_TITLE, boardGame.getTitle());
                assertEquals(VALID_DESCRIPTION, boardGame.getDescription());
                assertEquals(VALID_PLAYERS_NEEDED, boardGame.getPlayersNeeded());
                assertEquals(VALID_GAME_PICTURE, boardGame.getPicture());
        }

        @Test
        public void testGetSpecificBoardGameById() {
                SpecificBoardGame specificBoardGame = boardGameService
                                .getSpecificBoardGameById(VALID_SPECIFIC_BOARD_GAME.getId());

                // Assert
                assertNotNull(specificBoardGame);
                assertEquals(VALID_SPECIFIC_GAME_PICTURE, specificBoardGame.getPicture());
                assertEquals(VALID_SPECIFIC_GAME_DESCRIPTION, specificBoardGame.getDescription());
                assertEquals(VALID_GAME_STATUS, specificBoardGame.getStatus());
                assertEquals(VALID_TITLE, specificBoardGame.getBoardGame().getTitle());
                assertEquals(VALID_OWNER, specificBoardGame.getOwner());
        }

        @Test
        public void testGetAllBoardGames() {
                List<BoardGame> boardGames = new ArrayList<BoardGame>();
                boardGames = boardGameService.getAllBoardGames();
                BoardGame boardGame = boardGames.get(0);
                assertNotNull(boardGame);
        }

        @Test
        public void testGetAllSpecificBoardGames() {
                List<SpecificBoardGame> specificBoardGames = new ArrayList<SpecificBoardGame>();
                specificBoardGames = boardGameService.getAllSpecificBoardGames();
                SpecificBoardGame specificBoardGame = specificBoardGames.get(0);
                assertNotNull(specificBoardGame);
        }
}
