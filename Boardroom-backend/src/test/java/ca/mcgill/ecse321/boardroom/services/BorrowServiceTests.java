package ca.mcgill.ecse321.boardroom.services;

import ca.mcgill.ecse321.boardroom.dtos.creation.BorrowRequestDtoCreation;
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
    private static final int VALID_OWNER_ID = 2;
    private static final int VALID_SPECIFIC_GAME_ID = 1;
    private static final int VALID_BORROW_REQUEST_ID = 1;
    private static final int VALID_BORROW_REQUEST_ID2 = 2;
    private static final int INVALID_BORROW_REQUEST_ID = 999;

    private static final RequestStatus VALID_STATUS = RequestStatus.PENDING;
    private static final RequestStatus VALID_STATUS_COMPLETED = RequestStatus.RETURNED;

    private static final RequestStatus UPDATED_STATUS = RequestStatus.ACCEPTED;

    private static final LocalDateTime VALID_START_DATE = LocalDateTime.now().plusDays(1);
    private static final LocalDateTime VALID_END_DATE = LocalDateTime.now().plusDays(2);

    private Person person;
    private Person owner;
    private BoardGame boardGame;
    private SpecificBoardGame specificBoardGame;
    private BorrowRequest borrowRequest1;
    private BorrowRequest borrowRequest2;

    @BeforeEach
    public void setup() {
        person = new Person(VALID_PERSON_ID, "John Doe", "john.doe@gmail.com", "password", false);
        owner = new Person(VALID_OWNER_ID, "John Dee", "john.dee@gmail.com", "password", true);
        boardGame = new BoardGame("Monopoly", "A game about buying properties", 2, 1234);
        specificBoardGame = new SpecificBoardGame(VALID_SPECIFIC_GAME_ID, "Good quality, no rips", GameStatus.AVAILABLE, boardGame, owner);
        borrowRequest1 = new BorrowRequest(VALID_BORROW_REQUEST_ID, VALID_STATUS, VALID_START_DATE, VALID_END_DATE, person, specificBoardGame);
        borrowRequest2 = new BorrowRequest(VALID_BORROW_REQUEST_ID2, VALID_STATUS_COMPLETED,VALID_START_DATE, VALID_END_DATE, person, specificBoardGame);
    }

    @Test
    public void testCreateValidBorrowRequest() {
        //Arrange
        BorrowRequestDtoCreation borrowRequestDtoCreation = new BorrowRequestDtoCreation(VALID_STATUS, VALID_START_DATE, VALID_END_DATE, VALID_PERSON_ID, VALID_SPECIFIC_GAME_ID);

        when(personRepo.findById(VALID_PERSON_ID)).thenReturn(Optional.of(person));
        when(specificBoardGameRepo.findById(VALID_SPECIFIC_GAME_ID)).thenReturn(Optional.of(specificBoardGame));
        when(borrowRequestRepo.save(any(BorrowRequest.class))).thenReturn(new BorrowRequest(VALID_BORROW_REQUEST_ID, VALID_STATUS, VALID_START_DATE, VALID_END_DATE, person, specificBoardGame));

        // Act
        BorrowRequest createdRequest = borrowService.createBorrowRequest(borrowRequestDtoCreation);

        // Assert
        assertNotNull(createdRequest);
        assertEquals(VALID_BORROW_REQUEST_ID, createdRequest.getId());
        assertEquals(VALID_STATUS, createdRequest.getStatus());
        assertEquals(VALID_START_DATE, createdRequest.getRequestStartDate());
        assertEquals(VALID_END_DATE, createdRequest.getRequestEndDate());
        assertEquals(person, createdRequest.getPerson());
        assertEquals(specificBoardGame, createdRequest.getSpecificBoardGame());

        verify(personRepo, times(1)).findById(VALID_PERSON_ID);
        verify(specificBoardGameRepo, times(1)).findById(VALID_SPECIFIC_GAME_ID);
        verify(borrowRequestRepo, times(1)).save(any(BorrowRequest.class));
    }

    @Test
    public void testCreateBorrowRequestWithInvalidStartTime() {
        // Arrange
        BorrowRequestDtoCreation invalidBorrowRequestDto = new BorrowRequestDtoCreation(
                VALID_STATUS,
                LocalDateTime.now().minusDays(1), 
                LocalDateTime.now().plusDays(5),
                VALID_PERSON_ID,
                VALID_SPECIFIC_GAME_ID
        );

        BoardroomException exception = assertThrows(
                BoardroomException.class,
                () -> borrowService.createBorrowRequest(invalidBorrowRequestDto)
        );

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Start time cannot be in the past", exception.getMessage());

        verify(borrowRequestRepo, never()).save(any(BorrowRequest.class));
    }

    @Test
    public void testCreateBorrowRequestWithEndTimeBeforeStartTime() {
        // Arrange
        BorrowRequestDtoCreation invalidBorrowRequestDto = new BorrowRequestDtoCreation(
                VALID_STATUS,
                LocalDateTime.now().plusDays(5), 
                LocalDateTime.now().plusDays(1),  
                VALID_PERSON_ID,
                VALID_SPECIFIC_GAME_ID
        );

        BoardroomException exception = assertThrows(
                BoardroomException.class,
                () -> borrowService.createBorrowRequest(invalidBorrowRequestDto)
        );

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("End time must be after start time", exception.getMessage());

        verify(borrowRequestRepo, never()).save(any(BorrowRequest.class));
    }


    @Test
    public void testCreateBorrowRequestInvalidPerson() {
        // Arrange
        BorrowRequestDtoCreation dto = new BorrowRequestDtoCreation(VALID_STATUS, VALID_START_DATE, VALID_END_DATE, VALID_PERSON_ID, VALID_SPECIFIC_GAME_ID);

        when(personRepo.findById(VALID_PERSON_ID)).thenReturn(Optional.empty());

        // Act & Assert
        BoardroomException exception = assertThrows(BoardroomException.class, () -> borrowService.createBorrowRequest(dto));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("A person with this id does not exist", exception.getMessage());

        verify(personRepo, times(1)).findById(VALID_PERSON_ID);
        verify(specificBoardGameRepo, never()).findById(anyInt());
        verify(borrowRequestRepo, never()).save(any());
    }

    @Test
    public void testCreateBorrowRequestInvalidSpecificBoardGame() {
        // Arrange
        BorrowRequestDtoCreation dto = new BorrowRequestDtoCreation(VALID_STATUS, VALID_START_DATE, VALID_END_DATE, VALID_PERSON_ID, VALID_SPECIFIC_GAME_ID);
        when(personRepo.findById(VALID_PERSON_ID)).thenReturn(Optional.of(person));
        when(specificBoardGameRepo.findById(VALID_SPECIFIC_GAME_ID)).thenReturn(Optional.empty());

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
        when(borrowRequestRepo.findById(VALID_BORROW_REQUEST_ID)).thenReturn(Optional.of(borrowRequest1));
        when(borrowRequestRepo.save(any(BorrowRequest.class))).thenReturn(borrowRequest1);

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
        when(borrowRequestRepo.findById(VALID_BORROW_REQUEST_ID)).thenReturn(Optional.empty());

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
        when(specificBoardGameRepo.findById(VALID_SPECIFIC_GAME_ID)).thenReturn(Optional.of(specificBoardGame));
        when(borrowRequestRepo.findBySpecificBoardGameAndStatus(eq(specificBoardGame), eq(RequestStatus.RETURNED)))
                .thenReturn(List.of(borrowRequest2));
    
        // Act
        List<BorrowRequest> borrowRequests = borrowService.viewBorrowRequestsByBoardgame(VALID_SPECIFIC_GAME_ID);
    
        // Assert
        assertNotNull(borrowRequests);
        assertEquals(1, borrowRequests.size());
        assertEquals(RequestStatus.RETURNED, borrowRequests.get(0).getStatus());
    
        verify(specificBoardGameRepo, times(1)).findById(VALID_SPECIFIC_GAME_ID);
        verify(borrowRequestRepo, times(1)).findBySpecificBoardGameAndStatus(specificBoardGame, RequestStatus.RETURNED);
    }

    @Test
    public void testViewBorrowRequestsByBoardgameInvalid() {
        // Arrange
        int invalidSpecificBoardGameId = 999;
        when(specificBoardGameRepo.findById(invalidSpecificBoardGameId)).thenReturn(Optional.empty());

        // Act & Assert:
        BoardroomException exception = assertThrows(BoardroomException.class, () ->
            borrowService.viewBorrowRequestsByBoardgame(invalidSpecificBoardGameId)
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals(String.format("A specific board game with this id (%d) does not exist", invalidSpecificBoardGameId),
                     exception.getMessage());

        verify(specificBoardGameRepo, times(1)).findById(invalidSpecificBoardGameId);
        verify(borrowRequestRepo, never()).findBySpecificBoardGameAndStatus(any(SpecificBoardGame.class), eq(RequestStatus.RETURNED));
    }
    
    @Test
    public void testViewPendingBorrowRequests() {
        //Arrange
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

    @Test
    public void testGetValidBorrowRequestById() {
        // Arrange
        BorrowRequest mockRequest = new BorrowRequest(VALID_BORROW_REQUEST_ID, RequestStatus.PENDING, VALID_START_DATE, VALID_END_DATE, null,null);
        when(borrowRequestRepo.findById(VALID_BORROW_REQUEST_ID)).thenReturn(Optional.of(mockRequest));

        // Act
        BorrowRequest result = borrowService.getBorrowRequestById(VALID_BORROW_REQUEST_ID);

        // Assert
        assertNotNull(result);
        assertEquals(VALID_BORROW_REQUEST_ID, result.getId());
        verify(borrowRequestRepo, times(1)).findById(VALID_BORROW_REQUEST_ID);
    }

    @Test
    public void testGetInvalidBorrowRequestById() {
        // Arrange
        when(borrowRequestRepo.findById(INVALID_BORROW_REQUEST_ID)).thenReturn(Optional.empty());

        // Act & Assert
        BoardroomException ex = assertThrows(
            BoardroomException.class,
            () -> borrowService.getBorrowRequestById(INVALID_BORROW_REQUEST_ID)
        );

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
        assertTrue(ex.getMessage().contains("A borrow request with this id (999) does not exist"));
        verify(borrowRequestRepo, times(1)).findById(INVALID_BORROW_REQUEST_ID);
    }

    @Test
    public void testDeleteValidBorrowRequest() {
        // Arrange
        BorrowRequest mockBorrowRequest = new BorrowRequest(VALID_BORROW_REQUEST_ID, VALID_STATUS, VALID_START_DATE, VALID_END_DATE, person, specificBoardGame);

        when(borrowRequestRepo.findById(VALID_BORROW_REQUEST_ID)).thenReturn(Optional.of(mockBorrowRequest));

        // Act
        borrowService.deleteBorrowRequestById(VALID_BORROW_REQUEST_ID);

        // Assert
        verify(borrowRequestRepo, times(1)).findById(VALID_BORROW_REQUEST_ID);
        verify(borrowRequestRepo, times(1)).deleteById(VALID_BORROW_REQUEST_ID);
    }

    @Test
    public void testDeleteInvalidBorrowRequest() {
        // Arrange
        when(borrowRequestRepo.findById(INVALID_BORROW_REQUEST_ID)).thenReturn(Optional.empty());

        // Act + Assert
        BoardroomException ex = assertThrows(BoardroomException.class,
            () -> borrowService.deleteBorrowRequestById(INVALID_BORROW_REQUEST_ID)
        );

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
        assertEquals("A borrow request with this id (999) does not exist", ex.getMessage());

        verify(borrowRequestRepo, times(1)).findById(INVALID_BORROW_REQUEST_ID);
        verify(borrowRequestRepo, times(0)).deleteById(anyInt());
    }

}       