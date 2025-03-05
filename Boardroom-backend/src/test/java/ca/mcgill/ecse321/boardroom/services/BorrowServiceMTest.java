package ca.mcgill.ecse321.boardroom.services;

import ca.mcgill.ecse321.boardroom.model.BoardGame;
import ca.mcgill.ecse321.boardroom.model.BorrowRequest;
import ca.mcgill.ecse321.boardroom.model.Person;
import ca.mcgill.ecse321.boardroom.model.SpecificBoardGame;
import ca.mcgill.ecse321.boardroom.model.enums.GameStatus;
import ca.mcgill.ecse321.boardroom.model.enums.RequestStatus;
import ca.mcgill.ecse321.boardroom.repositories.BorrowRequestRepository;
import ca.mcgill.ecse321.boardroom.repositories.PersonRepository;
import ca.mcgill.ecse321.boardroom.repositories.SpecificBoardGameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BorrowServiceMTest {

    @Mock
    private BorrowRequestRepository borrowRequestRepo;

    @Mock
    private PersonRepository personRepo;

    @Mock
    private SpecificBoardGameRepository specificBoardGameRepo;

    @InjectMocks
    private BorrowServiceM borrowServiceM;

    private SpecificBoardGame specificBoardGame;
    private Person person;
    private BorrowRequest borrowRequest1;
    private BorrowRequest borrowRequest2;

    @BeforeEach
    public void setUp() {
        person = new Person(1, "John Doe", "john.doe@gmail.com", "password", false);
        specificBoardGame = new SpecificBoardGame(1, "Good quality, no rips", GameStatus.AVAILABLE, new BoardGame("Monopoly", "A game about buying properties", 2, 1234), person);

        borrowRequest1 = new BorrowRequest(1, RequestStatus.RETURNED, LocalDateTime.now().minusDays(10), LocalDateTime.now().minusDays(5), person, specificBoardGame);
        borrowRequest2 = new BorrowRequest(2, RequestStatus.ACCEPTED, LocalDateTime.now().minusDays(3), LocalDateTime.now().plusDays(2), person, specificBoardGame);
    }

    @Test
    public void testViewBorrowRequestsByBoardgame() {
        when(borrowRequestRepo.findByBoardGameIdAndStatusIn(eq(specificBoardGame.getBoardGame().getTitle()), anyList()))
                .thenReturn(List.of(borrowRequest1, borrowRequest2));

        List<BorrowRequest> borrowRequests = borrowServiceM.viewBorrowRequestsByBoardgame(specificBoardGame.getBoardGame().getTitle());

        assertNotNull(borrowRequests);
        assertEquals(2, borrowRequests.size());
        assertTrue(borrowRequests.contains(borrowRequest1));
        assertTrue(borrowRequests.contains(borrowRequest2));

        verify(borrowRequestRepo, times(1)).findByBoardGameIdAndStatusIn(eq(specificBoardGame.getBoardGame().getTitle()), anyList());
    }
}