package ca.mcgill.ecse321.boardroom.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ca.mcgill.ecse321.boardroom.model.BoardGame;
import ca.mcgill.ecse321.boardroom.model.Person;
import ca.mcgill.ecse321.boardroom.model.Event;

@SpringBootTest
public class EventRepositoryTests {
    @Autowired
    private EventRepository eventRepo;
    @Autowired
    private PersonRepository personRepo;
    @Autowired
    private BoardGameRepository boardGameRepo;

    @AfterEach
    public void clearDatabase() {
        eventRepo.deleteAll();
        personRepo.deleteAll();
        boardGameRepo.deleteAll();
    }

    @Test
    public void testCreateAndReadEvent() {
        // Arrange
        Person bob = new Person("Bob", "bob@mail.mcgill.ca", "1234", true);
        bob = personRepo.save(bob);

        BoardGame boardGame = new BoardGame("Monopoly", "A game about buying properties", 2, 1234);
        boardGame = boardGameRepo.save(boardGame);

        LocalDateTime startDateTime = LocalDateTime.parse("2025-02-13T00:00:00");
        LocalDateTime endDateTime = LocalDateTime.parse("2025-02-14T00:00:00");
        Event event = new Event("Cafe Get-together", "Meet new friends", startDateTime, endDateTime, 10, "1234 Rue Sainte-Catherine, Montreal, QC",
                bob, boardGame);
        event = eventRepo.save(event);

        // Act
        Event eventFromDB = eventRepo.findEventById(event.getId());

        // Assert
        assertNotNull(eventFromDB);
        assertEquals(event.getId(), eventFromDB.getId());
        assertEquals(event.getTitle(), eventFromDB.getTitle());
        assertEquals(event.getDescription(), eventFromDB.getDescription());
        assertEquals(event.getStartDateTime(), eventFromDB.getStartDateTime());
        assertEquals(event.getEndDateTime(), eventFromDB.getEndDateTime());
        assertEquals(event.getMaxParticipants(), eventFromDB.getMaxParticipants());
        assertEquals(event.getLocation(), eventFromDB.getLocation());
        assertEquals(event.getEventHost().getId(), eventFromDB.getEventHost().getId());
        assertEquals(event.getBoardGame().getTitle(), eventFromDB.getBoardGame().getTitle());
    }
}
