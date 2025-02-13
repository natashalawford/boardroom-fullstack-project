package ca.mcgill.ecse321.boardroom.repositories;
import org.springframework.boot.test.context.SpringBootTest;
import ca.mcgill.ecse321.boardroom.repositories.BoardGameRepository;
import ca.mcgill.ecse321.boardroom.model.BoardGame;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


@SpringBootTest
public class BoardGameRepositoryTests {

    @Autowired
	private BoardGameRepository repo;

	@AfterEach
	public void clearDatabase() {
		repo.deleteAll();
	}

	@Test
	public void testCreateAndReadBoardGame() {
		// Arrange
        
		BoardGame boardGame = new BoardGame("boardGameName", "boardGameDescription", 1, 2);
		boardGame = repo.save(boardGame);

		// Act
		BoardGame boardGameFromDb = repo.findBoardGameByTitle(boardGame.getTitle());

		// Assert
		assertNotNull(boardGameFromDb);
		assertEquals(boardGame.getTitle(), boardGameFromDb.getTitle());
		assertEquals(boardGame.getDescription(), boardGameFromDb.getDescription());
		assertEquals(boardGame.getPlayersNeeded(), boardGameFromDb.getPlayersNeeded());
		assertEquals(boardGame.getPicture(), boardGameFromDb.getPicture());
	}
}


