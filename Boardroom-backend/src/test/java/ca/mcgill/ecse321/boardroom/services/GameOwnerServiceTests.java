package ca.mcgill.ecse321.boardroom.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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

import ca.mcgill.ecse321.boardroom.dtos.SpecificBoardGameCreationDto;
import ca.mcgill.ecse321.boardroom.dtos.SpecificBoardGameRequestDto;
import ca.mcgill.ecse321.boardroom.dtos.responses.SpecificBoardGameResponseDto;
import ca.mcgill.ecse321.boardroom.model.BoardGame;
import ca.mcgill.ecse321.boardroom.model.Person;
import ca.mcgill.ecse321.boardroom.model.SpecificBoardGame;
import ca.mcgill.ecse321.boardroom.model.enums.GameStatus;
import ca.mcgill.ecse321.boardroom.repositories.SpecificBoardGameRepository;

@SpringBootTest
@MockitoSettings
public class GameOwnerServiceTests {
    @Mock
    private SpecificBoardGameRepository specificBoardGameRepo;

    @Mock
    private PersonService personService;

    @Mock
    private BoardGameService boardGameService;

    @InjectMocks
    private GameOwnerService gameOwnerService;

    //Attributes for specific board game
    private static final int VALID_SPECIFIC_PICTURE = 1;
    private static final String VALID_SPECIFIC_DESCRIPTION = "This is a specific board game";
    private static final GameStatus VALID_STATUS = GameStatus.AVAILABLE; 

    //Attributes for board game
    private static final String VALID_TITLE = "Monopoly";
    private static final String VALID_DESCRIPTION = "This is monopoly";
    private static final int VALID_PLAYERSNEEDED = 5;
    private static final int VALID_PICTURE = 2;

    //Attributes for owner
    private static final String VALID_NAME = "John Doe";
    private static final String VALID_EMAIL = "john.doe@gmail.com";
    private static final String VALID_PASSWORD = "1234";
    private static final boolean VALID_OWNER = true; 

    //Object Instantiation
    BoardGame VALID_BOARDGAME;
    Person VALID_GAME_OWNER;
    
    @BeforeEach
    public void objectInstantiation() {
        VALID_BOARDGAME = new BoardGame(VALID_TITLE, VALID_DESCRIPTION, VALID_PLAYERSNEEDED, VALID_PICTURE);
        VALID_GAME_OWNER = new Person(VALID_NAME, VALID_EMAIL, VALID_PASSWORD, VALID_OWNER); 
    }
    
    // @Test
    // public void testFindValidSpecificBoardGame() {
    //     //Arrange
    //     // BoardGame VALID_BOARDGAME = new BoardGame(VALID_TITLE, VALID_DESCRIPTION, VALID_PLAYERSNEEDED, VALID_PICTURE);

    //     // Person VALID_GAME_OWNER = new Person(VALID_NAME, VALID_EMAIL, VALID_PASSWORD, VALID_OWNER);

    //     when(specificBoardGameRepo.findSpecificBoardGameById(1)).thenReturn(new SpecificBoardGame(VALID_SPECIFIC_PICTURE, VALID_SPECIFIC_DESCRIPTION, VALID_STATUS, VALID_BOARDGAME, VALID_GAME_OWNER));

    //     //Act
    //     SpecificBoardGame specificBoardGame = boardGameService.getSpecificBoardGameById(1);

    //     //Assert
    //     assertNotNull(specificBoardGame);
    //     assertEquals(VALID_SPECIFIC_PICTURE, specificBoardGame.getPicture());
    //     assertEquals(VALID_SPECIFIC_DESCRIPTION, specificBoardGame.getDescription());
    //     assertEquals(VALID_STATUS, specificBoardGame.getStatus());
    //     assertEquals(VALID_BOARDGAME, specificBoardGame.getBoardGame());
    //     assertEquals(VALID_GAME_OWNER, specificBoardGame.getOwner());

    //     verify(specificBoardGameRepo, times(1)).findSpecificBoardGameById(anyInt());
    // }

    // @Test
    // public void testFindInvalidSpecificBoardGame() {

    // }
    
    
    @Test
    public void testCreateValidSpecificBoardGame() {
        //Arrange

        // BoardGame VALID_BOARDGAME = new BoardGame(VALID_TITLE, VALID_DESCRIPTION, VALID_PLAYERSNEEDED, VALID_PICTURE);
        // Person VALID_GAME_OWNER = new Person(VALID_NAME, VALID_EMAIL, VALID_PASSWORD, VALID_OWNER); 


        SpecificBoardGameCreationDto specificBoardGameToCreate = new SpecificBoardGameCreationDto(VALID_SPECIFIC_PICTURE, VALID_SPECIFIC_DESCRIPTION, VALID_STATUS, VALID_TITLE, 1);


        when(personService.findPersonById(anyInt())).thenReturn(VALID_GAME_OWNER);
        
        when(boardGameService.getBoardGameByTitle(anyString())).thenReturn(VALID_BOARDGAME);
        
        when(specificBoardGameRepo.save(any(SpecificBoardGame.class))).thenAnswer((InvocationOnMock iom) -> iom.getArgument(0));


        //Act
        SpecificBoardGame createdSpecificBoardGame = gameOwnerService.createSpecificBoardGame(specificBoardGameToCreate);

        //Assert
        assertNotNull(createdSpecificBoardGame);
        assertEquals(VALID_SPECIFIC_PICTURE, createdSpecificBoardGame.getPicture());
        assertEquals(VALID_SPECIFIC_DESCRIPTION, createdSpecificBoardGame.getDescription());
        assertEquals(VALID_STATUS, createdSpecificBoardGame.getStatus());
        assertEquals(VALID_BOARDGAME, createdSpecificBoardGame.getBoardGame());
        assertEquals(VALID_GAME_OWNER, createdSpecificBoardGame.getOwner()); 

        verify(personService, times(1)).findPersonById(anyInt());
        verify(boardGameService, times(1)).getBoardGameByTitle(anyString());
        verify(specificBoardGameRepo, times(1)).save(any(SpecificBoardGame.class));

    }

    // @Test
    // public void testCreateInvalidSpecificBoardGame1() {

    // }

    // @Test
    // public void testCreateInvalidSpecificBoardGame2() {

    // }

    @Test
    public void testUpdateValidSpecificBoardGame() {
        //Arrange 

        SpecificBoardGame existingSpecificBoardGame = new SpecificBoardGame(VALID_SPECIFIC_PICTURE, VALID_SPECIFIC_DESCRIPTION, VALID_STATUS, VALID_BOARDGAME, VALID_GAME_OWNER);

        when(boardGameService.getSpecificBoardGameById(anyInt())).thenReturn(existingSpecificBoardGame);
        when(specificBoardGameRepo.save(any(SpecificBoardGame.class))).thenAnswer((InvocationOnMock iom) -> iom.getArgument(0));

        int id = 1;
        SpecificBoardGameRequestDto specificBoardGameToUpdate = new SpecificBoardGameRequestDto(VALID_SPECIFIC_DESCRIPTION, VALID_SPECIFIC_PICTURE, VALID_STATUS);
    
        //Act
        SpecificBoardGameResponseDto updatedSpecificBoardGame = gameOwnerService.updateSpecificBoardGame(id, specificBoardGameToUpdate);

        //Assert
        assertNotNull(updatedSpecificBoardGame);
        assertEquals(VALID_SPECIFIC_PICTURE, updatedSpecificBoardGame.getPicture());
        assertEquals(VALID_SPECIFIC_DESCRIPTION, updatedSpecificBoardGame.getDescription());
        assertEquals(VALID_STATUS, updatedSpecificBoardGame.getStatus());
        assertEquals(VALID_BOARDGAME.getTitle(), updatedSpecificBoardGame.getBoardGameTitle());
        assertEquals(VALID_GAME_OWNER.getId(), updatedSpecificBoardGame.getOwnerId());

        verify(boardGameService, times(1)).getSpecificBoardGameById(anyInt());
        verify(specificBoardGameRepo, times(1)).save(any(SpecificBoardGame.class));
    }

    // @Test
    // public void testUpdateInvalidSpecificBoardGame() {}

}
