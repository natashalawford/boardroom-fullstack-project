package ca.mcgill.ecse321.boardroom.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ca.mcgill.ecse321.boardroom.model.BoardGame;
import ca.mcgill.ecse321.boardroom.model.Location;
import ca.mcgill.ecse321.boardroom.model.Person;
import ca.mcgill.ecse321.boardroom.model.Event;

@SpringBootTest
public class EventRepositoryTests {
    @Autowired
	private EventRepository eventRepo;
	@Autowired
    private LocationRepository locationRepo;
    @Autowired
    private PersonRepository personRepo;
    @Autowired
    private BoardGameRepository boardGameRepo;

	@AfterEach
	public void clearDatabase() {
		eventRepo.deleteAll();
        locationRepo.deleteAll();
        personRepo.deleteAll();
        boardGameRepo.deleteAll();
	}

	@Test
	public void testCreateAndReadEvent() {
		// Arrange
        String address = "1234 Rue Sainte-Catherine";
        String city = "Montreal";
        String province = "Quebec";
        Location montrealCafe = new Location(address, city, province);
        montrealCafe = locationRepo.save(montrealCafe);

		Person bob = new Person("Bob", "bob@mail.mcgill.ca", "1234", true);
		bob = personRepo.save(bob);

        BoardGame boardGame = new BoardGame("Monopoly", "A game about buying properties", 2, 1234);
        boardGame = boardGameRepo.save(boardGame);
		
        LocalDateTime startDateTime = LocalDateTime.now();
        LocalDateTime endDateTime = LocalDateTime.now().plusHours(2);
        Event event = new Event("Cafe Get-together", "Meet new friends", startDateTime, endDateTime, 10, montrealCafe, bob, boardGame);

        // Act
		Event eventFromDB = eventRepo.save(event);

		// Assert
        assertNotNull(eventFromDB);
        assertEquals(event.getId(), eventFromDB.getId());
        assertEquals(event.getTitle(), eventFromDB.getTitle());
        assertEquals(event.getDescription(), eventFromDB.getDescription());
        assertEquals(event.getStartDateTime(), eventFromDB.getStartDateTime());
        assertEquals(event.getEndDateTime(), eventFromDB.getEndDateTime());
        assertEquals(event.getMaxParticipants(), eventFromDB.getMaxParticipants());
        assertEquals(event.getLocation().getId(), eventFromDB.getLocation().getId());
        assertEquals(event.getEventHost().getId(), eventFromDB.getEventHost().getId());
        assertEquals(event.getBoardGame().getTitle(), eventFromDB.getBoardGame().getTitle());
    }
}
