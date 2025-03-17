package ca.mcgill.ecse321.boardroom.integration;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import ca.mcgill.ecse321.boardroom.dtos.EventRegistrationDto;
import ca.mcgill.ecse321.boardroom.dtos.responses.EventRegistrationResponseDto;
import ca.mcgill.ecse321.boardroom.model.Event;
import ca.mcgill.ecse321.boardroom.model.Person;
import ca.mcgill.ecse321.boardroom.repositories.EventRepository;
import ca.mcgill.ecse321.boardroom.repositories.PersonRepository;
import ca.mcgill.ecse321.boardroom.repositories.RegistrationRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
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

        event = new Event(VALID_TITLE, VALID_DESCRIPTION, VALID_START_TIME, VALID_END_TIME, VALID_MAX_PARTICIPANTS, null, person, null);
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
        EventRegistrationDto request = new EventRegistrationDto(person.getId(), event.getId());

        // Act
        ResponseEntity<EventRegistrationResponseDto> response = client.exchange("/registration", HttpMethod.PUT, new HttpEntity<>(request), EventRegistrationResponseDto.class);

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
        EventRegistrationDto request = new EventRegistrationDto(person.getId(), event.getId());

        // Act
        ResponseEntity<Void> response = client.exchange("/unregistration", HttpMethod.DELETE, new HttpEntity<>(request), Void.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}