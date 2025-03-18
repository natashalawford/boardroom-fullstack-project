package ca.mcgill.ecse321.boardroom.services;

import ca.mcgill.ecse321.boardroom.dtos.BorrowRequestDtoCreation;
import ca.mcgill.ecse321.boardroom.exceptions.BoardroomException;
import ca.mcgill.ecse321.boardroom.model.*;
import ca.mcgill.ecse321.boardroom.model.enums.GameStatus;
import ca.mcgill.ecse321.boardroom.model.enums.RequestStatus;
import ca.mcgill.ecse321.boardroom.repositories.BorrowRequestRepository;
import ca.mcgill.ecse321.boardroom.repositories.PersonRepository;
import ca.mcgill.ecse321.boardroom.repositories.SpecificBoardGameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest
@MockitoSettings
public class BorrowServiceTests {

    @Mock
    private BorrowRequestRepository borrowRequestRepo;

    @Mock
    private PersonRepository personRepo;

    @Mock
    private SpecificBoardGameRepository specificBoardGameRepo;

    @InjectMocks
    private BorrowService borrowService;

    private static final int VALID_PERSON_ID = 1;
    private static final int VALID_SPECIFIC_GAME_ID = 2;
    private static final int VALID_BORROW_REQUEST_ID = 3;

    private static final RequestStatus VALID_STATUS = RequestStatus.PENDING;
    private static final RequestStatus UPDATED_STATUS = RequestStatus.ACCEPTED;

    private static final LocalDateTime VALID_START_DATE = LocalDateTime.now().plusDays(1);
    private static final LocalDateTime VALID_END_DATE = LocalDateTime.now().plusDays(2);

    private SpecificBoardGame specificBoardGame;
    private BorrowRequest borrowRequest1;
    private BorrowRequest borrowRequest2;

    @BeforeEach
    public void setUp() {
        Person person = new Person(VALID_PERSON_ID, "John Doe", "john.doe@gmail.com", "password", false);
        BoardGame boardGame = new BoardGame("Monopoly", "A game about buying properties", 2, 1234);
        specificBoardGame = new SpecificBoardGame(VALID_SPECIFIC_GAME_ID, "Good quality, no rips", GameStatus.AVAILABLE, boardGame, person);

        borrowRequest1 = new BorrowRequest(1, RequestStatus.RETURNED, LocalDateTime.now().minusDays(10), LocalDateTime.now().minusDays(5), person, specificBoardGame);
        borrowRequest2 = new BorrowRequest(2, RequestStatus.ACCEPTED, LocalDateTime.now().minusDays(3), LocalDateTime.now().plusDays(2), person, specificBoardGame);
    }

    @Test
    public void testCreateValidBorrowRequest() {
        //Arrange
        Person mockBorrower = new Person(VALID_PERSON_ID, "John Doe", "john.doe@gmail.com", "password", false);
        Person mockOwner = new Person(VALID_PERSON_ID, "John Dee", "john.dee@gmail.com", "password", true);
        BoardGame mockBoardGame = new BoardGame("boardGameName", "boardGameDescription", 1, 2);
        SpecificBoardGame mockSpecificBoardGame = new SpecificBoardGame(VALID_SPECIFIC_GAME_ID, "A great board game", GameStatus.AVAILABLE, mockBoardGame, mockOwner);
        BorrowRequestDtoCreation dto = new BorrowRequestDtoCreation(VALID_STATUS, VALID_START_DATE, VALID_END_DATE, VALID_PERSON_ID, VALID_SPECIFIC_GAME_ID);

        when(personRepo.findById(VALID_PERSON_ID)).thenReturn(Optional.of(mockBorrower));
        when(specificBoardGameRepo.findById(VALID_SPECIFIC_GAME_ID)).thenReturn(Optional.of(mockSpecificBoardGame));
        when(borrowRequestRepo.save(any(BorrowRequest.class))).thenReturn(new BorrowRequest(VALID_BORROW_REQUEST_ID, VALID_STATUS, VALID_START_DATE, VALID_END_DATE, mockBorrower, mockSpecificBoardGame));

        // Act
        BorrowRequest createdRequest = borrowService.createBorrowRequest(dto);

        // Assert
        assertNotNull(createdRequest);
        assertEquals(VALID_BORROW_REQUEST_ID, createdRequest.getId());
        assertEquals(VALID_STATUS, createdRequest.getStatus());
        assertEquals(VALID_START_DATE, createdRequest.getRequestStartDate());
        assertEquals(VALID_END_DATE, createdRequest.getRequestEndDate());
        assertEquals(mockBorrower, createdRequest.getPerson());
        assertEquals(mockSpecificBoardGame, createdRequest.getSpecificBoardGame());

        verify(personRepo, times(1)).findById(VALID_PERSON_ID);
        verify(specificBoardGameRepo, times(1)).findById(VALID_SPECIFIC_GAME_ID);
        verify(borrowRequestRepo, times(1)).save(any(BorrowRequest.class));
    }

    @Test
    public void testCreateBorrowRequestInvalidPerson() {
        // Arrange
        BorrowRequestDtoCreation dto = new BorrowRequestDtoCreation(VALID_STATUS, VALID_START_DATE, VALID_END_DATE, VALID_PERSON_ID, VALID_SPECIFIC_GAME_ID);

        when(personRepo.findById(VALID_PERSON_ID)).thenReturn(Optional.empty()); // No person found

        // Act & Assert
        BoardroomException exception = assertThrows(BoardroomException.class, () -> borrowService.createBorrowRequest(dto));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("A person with this id does not exist", exception.getMessage());

        verify(personRepo, times(1)).findById(VALID_PERSON_ID);
        verify(specificBoardGameRepo, never()).findById(anyInt()); // Should not call game lookup
        verify(borrowRequestRepo, never()).save(any());
    }

    @Test
    public void testCreateBorrowRequestInvalidSpecificBoardGame() {
        // Arrange
        BorrowRequestDtoCreation dto = new BorrowRequestDtoCreation(VALID_STATUS, VALID_START_DATE, VALID_END_DATE, VALID_PERSON_ID, VALID_SPECIFIC_GAME_ID);

        Person mockPerson = new Person(VALID_PERSON_ID, "John Doe", "john.doe@gmail.com", "password", false);
        when(personRepo.findById(VALID_PERSON_ID)).thenReturn(Optional.of(mockPerson));
        when(specificBoardGameRepo.findById(VALID_SPECIFIC_GAME_ID)).thenReturn(Optional.empty()); // No game found

        // Act & Assert
        BoardroomException exception = assertThrows(BoardroomException.class, () -> borrowService.createBorrowRequest(dto));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("A specific board game with this id does not exist", exception.getMessage());

        verify(personRepo, times(1)).findById(VALID_PERSON_ID);
        verify(specificBoardGameRepo, times(1)).findById(VALID_SPECIFIC_GAME_ID);
        verify(borrowRequestRepo, never()).save(any());
    }

    @Test
    public void testUpdateValidBorrowRequestStatus() {
        // Arrange
        Person mockBorrower = new Person(VALID_PERSON_ID, "John Doe", "john.doe@gmail.com", "password", false);
        Person mockOwner = new Person(VALID_PERSON_ID, "John Dee", "john.dee@gmail.com", "password", true);
        BoardGame mockBoardGame = new BoardGame("boardGameName", "boardGameDescription", 1, 2);
        SpecificBoardGame mockSpecificBoardGame = new SpecificBoardGame(VALID_SPECIFIC_GAME_ID, "A great board game", GameStatus.AVAILABLE, mockBoardGame, mockOwner);
        BorrowRequest mockBorrowRequest = new BorrowRequest(VALID_BORROW_REQUEST_ID, VALID_STATUS, VALID_START_DATE, VALID_END_DATE, mockBorrower, mockSpecificBoardGame);

        when(borrowRequestRepo.save(any(BorrowRequest.class))).thenReturn(mockBorrowRequest);
        when(borrowRequestRepo.findById(VALID_BORROW_REQUEST_ID)).thenReturn(Optional.of(mockBorrowRequest));

        // Act
        BorrowRequest updatedRequest = borrowService.updateBorrowRequestStatus(VALID_BORROW_REQUEST_ID, UPDATED_STATUS);

        // Assert
        assertNotNull(updatedRequest);
        assertEquals(UPDATED_STATUS, updatedRequest.getStatus());

        verify(borrowRequestRepo, times(1)).findById(VALID_BORROW_REQUEST_ID);
        verify(borrowRequestRepo, times(1)).save(any(BorrowRequest.class));
    }

    @Test
    public void testUpdateInvalidBorrowRequestStatus() {
        // Arrange
        when(borrowRequestRepo.findById(VALID_BORROW_REQUEST_ID)).thenReturn(Optional.empty()); // No borrow request found

        // Act & Assert
        BoardroomException exception = assertThrows(BoardroomException.class, () -> borrowService.updateBorrowRequestStatus(VALID_BORROW_REQUEST_ID, UPDATED_STATUS));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("A borrow request with this id does not exist", exception.getMessage());

        verify(borrowRequestRepo, times(1)).findById(VALID_BORROW_REQUEST_ID);
        verify(borrowRequestRepo, never()).save(any());
    }

    @Test
    public void testViewBorrowRequestsByBoardgame() {
        // Arrange
        when(borrowRequestRepo.findBySpecificBoardGameAndStatus(eq(specificBoardGame), eq(RequestStatus.RETURNED)))
                .thenReturn(List.of(borrowRequest1));

        // Act
        List<BorrowRequest> borrowRequests = borrowService.viewBorrowRequestsByBoardgame(specificBoardGame);

        // Assert
        assertNotNull(borrowRequests);
        assertEquals(1, borrowRequests.size());
        assertTrue(borrowRequests.contains(borrowRequest1));
        assertFalse(borrowRequests.contains(borrowRequest2));

        verify(borrowRequestRepo, times(1)).findBySpecificBoardGameAndStatus(eq(specificBoardGame), eq(RequestStatus.RETURNED));
    }

    @Test
    public void testViewPendingBorrowRequests() {
        // Arrange
        borrowRequest1.setStatus(RequestStatus.PENDING);
        borrowRequest2.setStatus(RequestStatus.ACCEPTED);
        when(borrowRequestRepo.findByStatus(RequestStatus.PENDING)).thenReturn(List.of(borrowRequest1));

        // Act
        List<BorrowRequest> borrowRequests = borrowService.viewPendingBorrowRequests();

        // Assert
        assertNotNull(borrowRequests);
        assertEquals(1, borrowRequests.size());
        assertTrue(borrowRequests.contains(borrowRequest1));
        assertFalse(borrowRequests.contains(borrowRequest2));

        verify(borrowRequestRepo, times(1)).findByStatus(RequestStatus.PENDING);
    }

    //delete borrow request tests
    @Test
    public void testDeleteValidBorrowRequest() {
        // Arrange
        int validId = VALID_BORROW_REQUEST_ID;

        // Create a Person for the game’s owner
        Person fakeOwner = new Person(
            999,                        
            "Jane Owner",            
            "owner@example.com",     
            "testOwnerPwd",         
            true                     
        );

        // Create a BoardGame
        BoardGame fakeBoardGame = new BoardGame(
            "Fake Game Title",         
            "Sample board game desc",  
            2,                          
            4                           
        );

        // Create a SpecificBoardGame with the above owner and BG
        SpecificBoardGame fakeSpecificBoardGame = new SpecificBoardGame(
            123,                       
            "Some condition/description",
            456,                       
            GameStatus.AVAILABLE,       
            fakeBoardGame,            
            fakeOwner                 
        );

        // Create a Person for the “borrower”
        Person fakeBorrower = new Person(
            111,                        
            "Alice Borrower",
            "borrower@example.com",
            "testBorrowerPwd",
            false                      
        );

        // Now create the BorrowRequest with the arguments
        BorrowRequest mockBorrowRequest = new BorrowRequest(
            validId,                 
            VALID_STATUS,               
            VALID_START_DATE,
            VALID_END_DATE,
            fakeBorrower,            
            fakeSpecificBoardGame     
        );

        // Mock findById to return that BorrowRequest
        when(borrowRequestRepo.findById(validId)).thenReturn(Optional.of(mockBorrowRequest));

        // Act
        borrowService.deleteBorrowRequestById(validId);

        // Assert
        verify(borrowRequestRepo, times(1)).findById(validId);
        verify(borrowRequestRepo, times(1)).deleteById(validId);
    }


    @Test
    public void testDeleteInvalidBorrowRequest() {
        // Arrange
        int invalidId = 999;
        when(borrowRequestRepo.findById(invalidId)).thenReturn(Optional.empty());

        // Act + Assert
        BoardroomException ex = assertThrows(BoardroomException.class,
            () -> borrowService.deleteBorrowRequestById(invalidId)
        );

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
        assertEquals("A borrow request with this id (999) does not exist", ex.getMessage());

        // Ensure delete is not called
        verify(borrowRequestRepo, times(1)).findById(invalidId);
        verify(borrowRequestRepo, times(0)).deleteById(anyInt());
    }


}       