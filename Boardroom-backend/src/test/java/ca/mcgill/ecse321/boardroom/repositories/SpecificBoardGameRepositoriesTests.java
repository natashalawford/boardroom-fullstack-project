package ca.mcgill.ecse321.boardroom.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ca.mcgill.ecse321.boardroom.model.BoardGame;
import ca.mcgill.ecse321.boardroom.model.Person;
import ca.mcgill.ecse321.boardroom.model.SpecificBoardGame;
import ca.mcgill.ecse321.boardroom.model.enums.GameStatus;

@SpringBootTest
public class SpecificBoardGameRepositoriesTests {
    @Autowired
    private SpecificBoardGameRepository specificBoardGameRepo;
    @Autowired
    private BoardGameRepository boardGameRepo;
    @Autowired
    private PersonRepository personRepo;

    @AfterEach
    public void clearDatabase() {
        specificBoardGameRepo.deleteAll();
        boardGameRepo.deleteAll();
        personRepo.deleteAll();
    }

    @Test
    public void testCreateAndReadSpecificBoardGame() {
        // Arrange
        Person bob = new Person("Bob", "bob@mail.mcgill.ca", "1234", true);
        bob = personRepo.save(bob);

        BoardGame boardGame = new BoardGame("Monopoly", "A game about buying properties", 2, 1234);
        boardGame = boardGameRepo.save(boardGame);

        SpecificBoardGame specificBoardGame = new SpecificBoardGame(1212, "Good quality, no rips", GameStatus.AVAILABLE,
                boardGame, bob);

        // Act
        SpecificBoardGame specificBoardGameFromDb = specificBoardGameRepo.save(specificBoardGame);

        // Assert
        assertNotNull(specificBoardGame);
        assertEquals(specificBoardGame.getId(), specificBoardGameFromDb.getId());
        assertEquals(specificBoardGame.getPicture(), specificBoardGameFromDb.getPicture());
        assertEquals(specificBoardGame.getDescription(), specificBoardGameFromDb.getDescription());
        assertEquals(specificBoardGame.getStatus(), specificBoardGameFromDb.getStatus());
        assertEquals(specificBoardGame.getBoardGame().getTitle(), specificBoardGameFromDb.getBoardGame().getTitle());
        assertEquals(specificBoardGame.getOwner().getId(), specificBoardGameFromDb.getOwner().getId());
    }
}
