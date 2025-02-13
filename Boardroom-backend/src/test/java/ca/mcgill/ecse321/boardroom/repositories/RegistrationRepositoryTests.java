package ca.mcgill.ecse321.boardroom.repositories;

import org.springframework.boot.test.context.SpringBootTest;
import ca.mcgill.ecse321.boardroom.model.Registration;
import ca.mcgill.ecse321.boardroom.model.Person;
import ca.mcgill.ecse321.boardroom.model.Event;
import ca.mcgill.ecse321.boardroom.model.Location;
import ca.mcgill.ecse321.boardroom.model.BoardGame;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import java.time.LocalDateTime;

@SpringBootTest
public class RegistrationRepositoryTests {
    @Autowired
    private RegistrationRepository registrationRepo;
    @Autowired
    private PersonRepository personRepo;
    @Autowired
    private EventRepository eventRepo;
    @Autowired
    private LocationRepository locationRepo;
    @Autowired
    private BoardGameRepository boardGameRepo;

    @AfterEach
    public void clearDatabase() {
        registrationRepo.deleteAll();
        eventRepo.deleteAll();
        personRepo.deleteAll();
        locationRepo.deleteAll();
        boardGameRepo.deleteAll();

    }

    @Test
    public void testCreateAndReadRegistration() {
        // Arrange
        LocalDateTime date = LocalDateTime.parse("2025-02-13T00:00:00");
        Person person = new Person("personName", "personEmail", "personPassword", false);
        person = personRepo.save(person);
        BoardGame boardGame = new BoardGame("boardGameName", "boardGameDescription", 1, 2);
        boardGame = boardGameRepo.save(boardGame);
        Location location = new Location("locationName", "locationCity", "locationProvince");
        location = locationRepo.save(location);
        Event event = new Event("eventTitle", "eventDescription", date, date, 1, location, person, boardGame);
        event = eventRepo.save(event);
        Registration registration = new Registration(new Registration.Key(event, person), date);
        registration = registrationRepo.save(registration);

        // Act
        Registration registrationFromDb = registrationRepo.findRegistrationByKey(registration.getKey());

        // Assert
        assertNotNull(registrationFromDb);
        assertEquals(registration.getKey(), registrationFromDb.getKey());
        assertEquals(registration.getRegistrationDate(), registrationFromDb.getRegistrationDate());
        assertEquals(registration.getKey().getEvent().getId(), registrationFromDb.getKey().getEvent().getId());
        assertEquals(registration.getKey().getPerson().getId(), registrationFromDb.getKey().getPerson().getId());
    }
}