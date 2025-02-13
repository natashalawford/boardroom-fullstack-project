package ca.mcgill.ecse321.boardroom.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ca.mcgill.ecse321.boardroom.model.Person;

@SpringBootTest
public class PersonRepositoryTests {
	@Autowired
	private PersonRepository repo;

	@AfterEach
	public void clearDatabase() {
		repo.deleteAll();
	}

	@Test
	public void testCreateAndReadPerson() {
		// Arrange
		Person bob = new Person("Bob", "bob@mail.mcgill.ca", "1234", true);
		bob = repo.save(bob);

		// Act
		Person bobFromDb = repo.findPersonById(bob.getId());

		// Assert
		assertNotNull(bob);
		assertEquals(bob.getId(), bobFromDb.getId());
		assertEquals(bob.getName(), bobFromDb.getName());
		assertEquals(bob.getEmail(), bobFromDb.getEmail());
		assertEquals(bob.getPassword(), bobFromDb.getPassword());
		assertEquals(bob.isOwner(), bobFromDb.isOwner());
	}
}
