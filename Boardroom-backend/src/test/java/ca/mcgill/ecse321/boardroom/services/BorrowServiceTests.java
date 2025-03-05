package ca.mcgill.ecse321.boardroom.services;

import ca.mcgill.ecse321.boardroom.dtos.BorrowRequestDtoCreation;
import ca.mcgill.ecse321.boardroom.exceptions.BoardroomException;
import ca.mcgill.ecse321.boardroom.model.*;
import ca.mcgill.ecse321.boardroom.model.enums.GameStatus;
import ca.mcgill.ecse321.boardroom.model.enums.RequestStatus;
import ca.mcgill.ecse321.boardroom.repositories.BorrowRequestRepository;
import ca.mcgill.ecse321.boardroom.repositories.PersonRepository;
import ca.mcgill.ecse321.boardroom.repositories.SpecificBoardGameRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
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

}
