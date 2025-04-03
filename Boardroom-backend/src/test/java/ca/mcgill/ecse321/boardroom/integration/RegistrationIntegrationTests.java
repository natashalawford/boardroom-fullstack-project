package ca.mcgill.ecse321.boardroom.integration;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import ca.mcgill.ecse321.boardroom.dtos.responses.EventRegistrationResponseDto;
import ca.mcgill.ecse321.boardroom.model.Event;
import ca.mcgill.ecse321.boardroom.model.Person;
import ca.mcgill.ecse321.boardroom.model.Registration;
import ca.mcgill.ecse321.boardroom.repositories.EventRepository;
import ca.mcgill.ecse321.boardroom.repositories.PersonRepository;
import ca.mcgill.ecse321.boardroom.repositories.RegistrationRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
public class RegistrationIntegrationTests {

    @Autowired
    private TestRestTemplate client;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private RegistrationRepository registrationRepository;

    private static final String VALID_NAME = "Bob";
    private static final String VALID_EMAIL = "bob@mail.com";
    private static final String VALID_PASSWORD = "password123";
    private static final String VALID_TITLE = "Board Game Night";
    private static final String VALID_DESCRIPTION = "A fun night with friends!";
    private static final LocalDateTime VALID_START_TIME = LocalDateTime.now().plusDays(1);
    private static final LocalDateTime VALID_END_TIME = LocalDateTime.now().plusDays(1).plusHours(2);
    private static final int VALID_MAX_PARTICIPANTS = 10;
    private Person person;
    private Event event;

    @BeforeAll
    public void setup() {
        person = new Person(VALID_NAME, VALID_EMAIL, VALID_PASSWORD, false);
        personRepository.save(person);

        event = new Event(VALID_TITLE, VALID_DESCRIPTION, VALID_START_TIME, VALID_END_TIME, VALID_MAX_PARTICIPANTS,
                null, person, null);
        eventRepository.save(event);
    }

    @AfterAll
    public void cleanup() {
        registrationRepository.deleteAll();
        eventRepository.deleteAll();
        personRepository.deleteAll();
    }

    @Test
    @Order(0)
    public void testRegisterForEvent() {
        // Arrange

        // Act
        ResponseEntity<EventRegistrationResponseDto> response = client.exchange("/registration/{personId}/{eventId}", HttpMethod.PUT,
                null, EventRegistrationResponseDto.class, person.getId(), event.getId());

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        EventRegistrationResponseDto createdRegistration = response.getBody();
        assertNotNull(createdRegistration);
        assertEquals(person.getId(), createdRegistration.getPersonId());
        assertEquals(event.getId(), createdRegistration.getEventId());
        assertNotNull(createdRegistration.getRegistrationId());
    }

    @Test
    @Order(1)
    public void testUnregisterFromEvent() {
        // Arrange

        // Act
        ResponseEntity<Void> response = client.exchange("/registration/{personId}/{eventId}", HttpMethod.DELETE, null,
                Void.class, person.getId(), event.getId());

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    @Order(2)
    public void testRegisterForNonExistentEvent() {
        // Arrange


        // Act
        ResponseEntity<EventRegistrationResponseDto> response = client.exchange("/registration/{personId}/{eventId}", HttpMethod.PUT,
                null, EventRegistrationResponseDto.class, person.getId(), 99999);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @Order(3)
    public void testRegisterNonExistentPerson() {
        // Arrange


        // Act
        ResponseEntity<EventRegistrationResponseDto> response = client.exchange("/registration/{personId}/{eventId}", HttpMethod.PUT,
                null, EventRegistrationResponseDto.class, 99999, event.getId());

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @Order(4)
    public void testUnregisterWhenNotRegistered() {
        // Arrange
        Person newPerson = new Person("Alice", "alice@mail.com", "password123", false);
        personRepository.save(newPerson);

        // Act
        ResponseEntity<Void> response = client.exchange("/registration/{personId}/{eventId}", HttpMethod.DELETE, null,
                Void.class, newPerson.getId(), event.getId());

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @Order(5)
    public void testGetRegistration() {
        // Arrange
        int eventId = event.getId();
        int personId = person.getId();
        Registration registration = new Registration(new Registration.Key(event, person), LocalDateTime.now());
        registrationRepository.save(registration);

        // Act
        ResponseEntity<EventRegistrationResponseDto> getResponse = client.getForEntity("/registration/{personId}/{eventId}",
                EventRegistrationResponseDto.class, personId, eventId);

        // Assert
        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        assertNotNull(getResponse.getBody());
        assertEquals(person.getId(), getResponse.getBody().getPersonId());
        assertEquals(event.getId(), getResponse.getBody().getEventId());
        assertNotNull(getResponse.getBody().getRegistrationId());
    }

    @Test
    @Order(6)
    public void testGetNonExistentRegistrationEventId() {
        // Arrange
        int personId = person.getId();

        // Act
        ResponseEntity<EventRegistrationResponseDto> getResponse = client.getForEntity("/registration/{personId}/{eventId}",
                EventRegistrationResponseDto.class, personId, 99999);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, getResponse.getStatusCode());
    }

    @Test
    @Order(7)
    public void testGetNonExistentRegistrationPersonId() {
        // Arrange
        int eventId = event.getId();

        // Act
        ResponseEntity<EventRegistrationResponseDto> getResponse = client.getForEntity("/registration/{personId}/{eventId}",
                EventRegistrationResponseDto.class, 99999, eventId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, getResponse.getStatusCode());
    }

    @Test
    @Order(8)
    public void testGetRegistrationsForEvent() {
        // Arrange
        int eventId = event.getId();

        // Create and save multiple registrations
        Person anotherPerson = new Person("Charlie", "charlie@mail.com", "password123", false);
        personRepository.save(anotherPerson);

        Registration registration1 = new Registration(new Registration.Key(event, person), LocalDateTime.now());
        Registration registration2 = new Registration(new Registration.Key(event, anotherPerson), LocalDateTime.now());
        registrationRepository.save(registration1);
        registrationRepository.save(registration2);

        // Act
        ResponseEntity<EventRegistrationResponseDto[]> response = client.getForEntity("/registration/event/{eventId}",
                EventRegistrationResponseDto[].class, eventId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().length);

        // Ensure the registrations match the expected persons
        boolean containsPerson1 = false;
        boolean containsPerson2 = false;

        for (EventRegistrationResponseDto dto : response.getBody()) {
            if (dto.getPersonId() == person.getId()) {
                containsPerson1 = true;
            } else if (dto.getPersonId() == anotherPerson.getId()) {
                containsPerson2 = true;
            }
        }

        assertTrue(containsPerson1);
        assertTrue(containsPerson2);
    }


}