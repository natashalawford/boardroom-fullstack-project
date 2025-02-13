package ca.mcgill.ecse321.boardroom.repositories;

import org.springframework.boot.test.context.SpringBootTest;
import ca.mcgill.ecse321.boardroom.model.BorrowRequest;
import ca.mcgill.ecse321.boardroom.model.Person;
import ca.mcgill.ecse321.boardroom.model.BoardGame;
import ca.mcgill.ecse321.boardroom.model.SpecificBoardGame;
import ca.mcgill.ecse321.boardroom.model.enums.*;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import java.time.LocalDateTime;

@SpringBootTest 
public class BorrowRequestTests {
    @Autowired
    private BorrowRequestRepository repo;
    @Autowired
    private PersonRepository personRepo;
    @Autowired
    private SpecificBoardGameRepository specificBoardGameRepo;
    @Autowired
    private BoardGameRepository boardGameRepo;

    @AfterEach
    public void clearDatabase() {
        repo.deleteAll();
        specificBoardGameRepo.deleteAll();
        boardGameRepo.deleteAll();
        personRepo.deleteAll();
    }
    
    @Test
    public void testCreateAndReadBorrowRequest() {
        // Arrange
        LocalDateTime start = LocalDateTime.parse("2025-02-13T00:00:00");
        LocalDateTime end = LocalDateTime.parse("2025-02-14T00:00:00");
        Person person = new Person("personName", "personEmail", "personPassword", false);
        BoardGame boardGame = new BoardGame("boardGameName", "boardGameDescription", 1, 2);
        person = personRepo.save(person);
        boardGame = boardGameRepo.save(boardGame);
        SpecificBoardGame specificBoardGame = new SpecificBoardGame(0, "specificBoardGameDescription", GameStatus.AVAILABLE, boardGame, person);
        specificBoardGame = specificBoardGameRepo.save(specificBoardGame);
        BorrowRequest borrowRequest = new BorrowRequest(0, RequestStatus.PENDING, start, end, person, specificBoardGame);
        borrowRequest = repo.save(borrowRequest);

        // Act
        BorrowRequest borrowRequestFromDb = repo.findBorrowRequestById(borrowRequest.getId());

        // Assert
        assertNotNull(borrowRequestFromDb);
        assertEquals(borrowRequest.getId(), borrowRequestFromDb.getId());
        assertEquals(borrowRequest.getStatus(), borrowRequestFromDb.getStatus());
        assertEquals(borrowRequest.getRequestStartDate(), borrowRequestFromDb.getRequestStartDate());
        assertEquals(borrowRequest.getRequestEndDate(), borrowRequestFromDb.getRequestEndDate());
        assertEquals(borrowRequest.getPerson().getId(), borrowRequestFromDb.getPerson().getId());
        assertEquals(borrowRequest.getSpecificBoardGame().getId(), borrowRequestFromDb.getSpecificBoardGame().getId());
    }
}