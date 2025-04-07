package ca.mcgill.ecse321.boardroom.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import ca.mcgill.ecse321.boardroom.dtos.SpecificBoardGameRequestDto;
import ca.mcgill.ecse321.boardroom.dtos.creation.BoardGameCreationDto;
import ca.mcgill.ecse321.boardroom.dtos.creation.SpecificBoardGameCreationDto;
import ca.mcgill.ecse321.boardroom.exceptions.BoardroomException;
import ca.mcgill.ecse321.boardroom.model.BoardGame;
import ca.mcgill.ecse321.boardroom.model.Person;
import ca.mcgill.ecse321.boardroom.model.SpecificBoardGame;
import ca.mcgill.ecse321.boardroom.model.enums.GameStatus;
import ca.mcgill.ecse321.boardroom.repositories.BoardGameRepository;
import ca.mcgill.ecse321.boardroom.repositories.BorrowRequestRepository;
import ca.mcgill.ecse321.boardroom.repositories.SpecificBoardGameRepository;

@SpringBootTest
@MockitoSettings
public class GameOwnerServiceTests {
    @Mock
    private BoardGameRepository boardGameRepo;

    @Mock
    private SpecificBoardGameRepository specificBoardGameRepo;

    @Mock
    private PersonService personService;

    @Mock
    private BoardGameService boardGameService;

    @Mock
    private BorrowRequestRepository borrowRequestRepo;

    @InjectMocks
    private GameOwnerService gameOwnerService;

    //Attributes for specific board game
    private static final int VALID_SPECIFIC_ID = 1042;
    private static final int VALID_SPECIFIC_PICTURE = 1;
    private static final String VALID_SPECIFIC_DESCRIPTION = "This is a specific board game";
    private static final GameStatus VALID_STATUS = GameStatus.AVAILABLE; 

    private static final int UPDATED_VALID_SPECIFIC_PICTURE = 5; 
    private static final String UPDATED_VALID_SPECIFIC_DESCRIPTION = "This is an updated specific board game";
    private static final GameStatus UPDATED_VALID_STATUS = GameStatus.UNAVAILABLE;

    //Attributes for board game
    private static final String VALID_TITLE = "Monopoly";
    private static final String VALID_DESCRIPTION = "This is monopoly";
    private static final int VALID_PLAYERS_NEEDED = 5;
    private static final int VALID_PICTURE = 2;

    //Attributes for owner
    private static final int VALID_OWNER_ID = 1351;
    private static final String VALID_NAME = "John Doe";
    private static final String VALID_EMAIL = "john.doe@gmail.com";
    private static final String VALID_PASSWORD = "1234";
    private static final boolean VALID_OWNER = true; 

    //Object Instantiation
    BoardGame VALID_BOARDGAME;
    Person VALID_GAME_OWNER;
    
    // @BeforeEach
    // public void objectInstantiation() {
    //     VALID_BOARDGAME = new BoardGame(VALID_TITLE, VALID_DESCRIPTION, VALID_PLAYERS_NEEDED, VALID_PICTURE);
    //     VALID_GAME_OWNER = new Person(VALID_NAME, VALID_EMAIL, VALID_PASSWORD, VALID_OWNER); 
    // } 


    //CHECK IF THERE IS REPITITION IN MOST OF THE TESTS AND USE BEFORE EACH

    @Test
    public void testCreateValidBoardGame() {
        //Arrange
        BoardGameCreationDto boardGameToCreate = new BoardGameCreationDto(VALID_TITLE, VALID_DESCRIPTION, VALID_PLAYERS_NEEDED, VALID_PICTURE);

        when(boardGameRepo.existsByTitleIgnoreCase(anyString())).thenReturn(false);
        when(boardGameRepo.save(any(BoardGame.class))).thenAnswer((InvocationOnMock iom) -> iom.getArgument(0));

        //Act
        BoardGame createdBoardGame = gameOwnerService.createBoardGame(boardGameToCreate);


        //Assert
        assertNotNull(createdBoardGame);
        assertEquals(VALID_TITLE, createdBoardGame.getTitle());
        assertEquals(VALID_DESCRIPTION, createdBoardGame.getDescription());
        assertEquals(VALID_PLAYERS_NEEDED, createdBoardGame.getPlayersNeeded());
        assertEquals(VALID_PICTURE, createdBoardGame.getPicture());

        verify(boardGameRepo, times(1)).existsByTitleIgnoreCase(anyString());
        verify(boardGameRepo, times(1)).save(any(BoardGame.class)); 
    }

    @Test
    public void testCreateInvalidBoardGameTitleAlreadyExists() {
        //Arrange
        BoardGameCreationDto boardGameToCreate = new BoardGameCreationDto(VALID_TITLE, VALID_DESCRIPTION, VALID_PLAYERS_NEEDED, VALID_PICTURE);

        when(boardGameRepo.existsByTitleIgnoreCase(anyString())).thenReturn(true);
        
        //Act + Assert
        BoardroomException e = assertThrows(BoardroomException.class, () -> gameOwnerService.createBoardGame(boardGameToCreate));

        assertEquals(HttpStatus.BAD_REQUEST, e.getStatus());

        verify(boardGameRepo, times(0)).save(any(BoardGame.class));
    }

    @Test
    public void testCreateValidSpecificBoardGame() {
        //Arrange
        SpecificBoardGameCreationDto specificBoardGameToCreate = new SpecificBoardGameCreationDto(VALID_SPECIFIC_PICTURE, VALID_DESCRIPTION, VALID_STATUS, VALID_TITLE, VALID_OWNER_ID);

        when(personService.findPersonById(anyInt())).thenReturn(new Person(VALID_OWNER_ID, VALID_NAME, VALID_EMAIL, VALID_PASSWORD, VALID_OWNER));
        when(boardGameService.getBoardGameByTitle(anyString())).thenReturn(new BoardGame(VALID_TITLE, VALID_DESCRIPTION, VALID_PLAYERS_NEEDED, VALID_PICTURE));
        when(specificBoardGameRepo.save(any(SpecificBoardGame.class))).thenAnswer((InvocationOnMock iom) -> iom.getArgument(0));

        //Act
        SpecificBoardGame createdSpecificBoardGame = gameOwnerService.createSpecificBoardGame(specificBoardGameToCreate);

        //Assert
        assertNotNull(createdSpecificBoardGame);
        assertEquals(VALID_SPECIFIC_PICTURE, createdSpecificBoardGame.getPicture());
        assertEquals(VALID_DESCRIPTION, createdSpecificBoardGame.getDescription());
        assertEquals(VALID_STATUS, createdSpecificBoardGame.getStatus());
        assertEquals(VALID_TITLE, createdSpecificBoardGame.getBoardGame().getTitle());
        assertEquals(VALID_OWNER_ID, createdSpecificBoardGame.getOwner().getId());
    }

    // @Test
    // public void testCreate

    @Test
    public void testCreateInvalidSpecificBoardGamePersonNotOwner() {
        //Arrange
        SpecificBoardGameCreationDto specificBoardGameToCreate = new SpecificBoardGameCreationDto(VALID_SPECIFIC_PICTURE, VALID_DESCRIPTION, VALID_STATUS, VALID_TITLE, VALID_OWNER_ID);

        when(personService.findPersonById(anyInt())).thenReturn(new Person(VALID_OWNER_ID, VALID_NAME, VALID_EMAIL, VALID_PASSWORD, false));

        //Act + Assert
        BoardroomException e = assertThrows(BoardroomException.class, () -> gameOwnerService.createSpecificBoardGame(specificBoardGameToCreate));

        assertEquals(HttpStatus.BAD_REQUEST, e.getStatus());

        verify(specificBoardGameRepo, times(0)).save(any(SpecificBoardGame.class));
    }

    @Test
    public void testUpdateValidSpecificBoardGame() {
        //Arrange
        BoardGame boardGame = new BoardGame(VALID_TITLE, VALID_DESCRIPTION, VALID_PLAYERS_NEEDED, VALID_PICTURE);
        Person person = new Person(VALID_OWNER_ID, VALID_NAME, VALID_EMAIL, VALID_PASSWORD, VALID_OWNER);
        SpecificBoardGameRequestDto specificBoardGameToUpdate = new SpecificBoardGameRequestDto(UPDATED_VALID_SPECIFIC_DESCRIPTION, UPDATED_VALID_SPECIFIC_PICTURE, UPDATED_VALID_STATUS);

        when(boardGameService.getSpecificBoardGameById(anyInt())).thenReturn(new SpecificBoardGame(VALID_PICTURE, VALID_SPECIFIC_DESCRIPTION, VALID_STATUS, boardGame, person));
        when(specificBoardGameRepo.save(any(SpecificBoardGame.class))).thenAnswer((InvocationOnMock iom) -> iom.getArgument(0));

        //Act
        SpecificBoardGame updatedSpecificBoardGame = gameOwnerService.updateSpecificBoardGame(1, specificBoardGameToUpdate);

        //Assert
        assertNotNull(updatedSpecificBoardGame);
        assertEquals(UPDATED_VALID_SPECIFIC_DESCRIPTION, updatedSpecificBoardGame.getDescription());
        assertEquals(UPDATED_VALID_SPECIFIC_PICTURE, updatedSpecificBoardGame.getPicture());
        assertEquals(UPDATED_VALID_STATUS, updatedSpecificBoardGame.getStatus());
        assertEquals(boardGame.getTitle(), updatedSpecificBoardGame.getBoardGame().getTitle());
        assertEquals(person.getId(), updatedSpecificBoardGame.getOwner().getId());        
    }

    @Test
    public void testDeleteValidBoardGame() {
        //Arrange
        BoardGame boardGame = new BoardGame(VALID_TITLE, VALID_DESCRIPTION, VALID_PLAYERS_NEEDED, VALID_PICTURE);

        when(boardGameService.getBoardGameByTitle(anyString())).thenReturn(boardGame);

        //Act
        gameOwnerService.deleteBoardGame(VALID_TITLE);

        //Assert
        verify(boardGameRepo, times(1)).delete(any(BoardGame.class));
    }

    @Test 
    public void testDeleteValidSpecificBoardGame() {
        //Arrange
        BoardGame boardGame = new BoardGame(VALID_TITLE, VALID_DESCRIPTION, VALID_PLAYERS_NEEDED, VALID_PICTURE);
        Person person = new Person(VALID_OWNER_ID, VALID_NAME, VALID_EMAIL, VALID_PASSWORD, VALID_OWNER);

        SpecificBoardGame specificBoardGame = new SpecificBoardGame(VALID_SPECIFIC_ID, VALID_DESCRIPTION, VALID_SPECIFIC_PICTURE, VALID_STATUS, boardGame, person);

        when(boardGameService.getSpecificBoardGameById(anyInt())).thenReturn(specificBoardGame);

        //Act
        gameOwnerService.deleteSpecificBoardGame(VALID_SPECIFIC_ID);

        //Assert
        verify(borrowRequestRepo, times(1)).deleteAllBySpecificBoardGame(any(SpecificBoardGame.class));
        verify(specificBoardGameRepo, times(1)).delete(any(SpecificBoardGame.class));
    }
}
