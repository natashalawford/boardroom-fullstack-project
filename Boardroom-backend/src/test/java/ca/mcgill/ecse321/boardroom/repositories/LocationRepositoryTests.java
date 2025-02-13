package ca.mcgill.ecse321.boardroom.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ca.mcgill.ecse321.boardroom.model.Location;

@SpringBootTest
public class LocationRepositoryTests {
    @Autowired
	private LocationRepository repo;

	@AfterEach
	public void clearDatabase() {
		repo.deleteAll();
	}

	@Test
	public void testCreateAndReadLocation() {
		// Arrange
        String address = "1234 Rue Sainte-Catherine";
        String city = "Montreal";
        String province = "Quebec";
        Location montrealCafe = new Location(address, city, province);
        montrealCafe = repo.save(montrealCafe);

		// Act
		Location cafeFromDB = repo.findLocationById(montrealCafe.getId());

		// Assert
		assertNotNull(cafeFromDB);
		assertEquals(montrealCafe.getId(), cafeFromDB.getId());
		assertEquals(montrealCafe.getAddress(), cafeFromDB.getAddress());
		assertEquals(montrealCafe.getCity(), cafeFromDB.getCity());
		assertEquals(montrealCafe.getProvince(), cafeFromDB.getProvince());
	}
}
