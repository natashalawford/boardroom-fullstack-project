package ca.mcgill.ecse321.boardroom.repositories;
import org.springframework.boot.test.context.SpringBootTest;
import ca.mcgill.ecse321.boardroom.repositories.BoardGameRepository;
import ca.mcgill.ecse321.boardroom.repositories.PersonRepository;
import ca.mcgill.ecse321.boardroom.model.BoardGame;

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
        
		// Date today = Date.valueOf("2025-02-03");
		// Person peteMikeJoe = new Person(
		// 		"Pete Mike Joe",
		// 		"pete.mike.joe@mail.mcgill.ca",
		// 		"petemjdabest",
		// 		today);
		// peteMikeJoe = repo.save(peteMikeJoe); //copy of peteMikeJoe with id set

		// Act
		// Person peteMikeJoeFromDb = repo.findPersonById(peteMikeJoe.getId());

		// Assert
		// assertNotNull(peteMikeJoeFromDb);
		// assertEquals(peteMikeJoe.getId(), peteMikeJoeFromDb.getId()); //first is the expected value, second is the actual value
		// assertEquals(peteMikeJoe.getName(), peteMikeJoeFromDb.getName());
		// assertEquals(peteMikeJoe.getEmailAddress(), peteMikeJoeFromDb.getEmailAddress());
		// assertEquals(peteMikeJoe.getPassword(), peteMikeJoeFromDb.getPassword());
		// assertEquals(peteMikeJoe.getCreationDate(), peteMikeJoeFromDb.getCreationDate());
	}
}


