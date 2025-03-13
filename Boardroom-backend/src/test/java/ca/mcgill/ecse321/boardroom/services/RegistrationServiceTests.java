package ca.mcgill.ecse321.boardroom.services;

import ca.mcgill.ecse321.boardroom.model.BoardGame;
import ca.mcgill.ecse321.boardroom.model.Location;
import ca.mcgill.ecse321.boardroom.model.Person;
import ca.mcgill.ecse321.boardroom.model.Event;
import ca.mcgill.ecse321.boardroom.model.Registration;
import ca.mcgill.ecse321.boardroom.repositories.EventRepository;
import ca.mcgill.ecse321.boardroom.repositories.PersonRepository;
import ca.mcgill.ecse321.boardroom.repositories.RegistrationRepository;
import ca.mcgill.ecse321.boardroom.dtos.EventRegistrationDto;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class RegistrationServiceTests {

    @Mock
    private PersonRepository personRepository;

    @Mock
    private RegistrationRepository registrationRepository;

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private RegistrationService registrationService;

    private static final int EVENT_ID = 100;

    
    private static final String VALID_TITLE = "Board Game Night";
    private static final String VALID_DESCRIPTION = "A fun night of board games!";
    private static final LocalDateTime VALID_START_TIME = LocalDateTime.now().plusDays(1);
    private static final LocalDateTime VALID_END_TIME = LocalDateTime.now().plusDays(1).plusHours(2);
    private static final int VALID_MAX_PARTICIPANTS = 10;
    private static Location VALID_LOCATION;
    private static Person VALID_HOST;
    private static BoardGame VALID_BOARD_GAME;
    private static Person PERSON;

    private int PERSON_ID;

    @BeforeEach
    public void setup() {
        VALID_LOCATION = new Location("McGill", "Montreal", "QC");
        VALID_HOST = new Person("Alice", "alice@mail.com", "securepass", false);
        VALID_BOARD_GAME = new BoardGame("Uno", "A fun card game", 2, 54321);
        PERSON = new Person("Person", "person@mail.com", "pass12", false);
        int PERSON_ID = PERSON.getId();
        
        this.PERSON_ID = PERSON_ID;
    }

    @Test
    public void testRegisterForEvent_PersonNotFound() {
        // Arrange
        EventRegistrationDto eventRegistrationDto = new EventRegistrationDto(PERSON_ID, EVENT_ID);

        when(personRepository.findById(PERSON_ID)).thenReturn(java.util.Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> registrationService.registerForEvent(eventRegistrationDto)
        );

        assertEquals("Person not found", exception.getMessage());
        verify(registrationRepository, never()).save(any(Registration.class));
    }

    @Test
    public void testRegisterForEvent_EventNotFound() {
        // Arrange
        EventRegistrationDto eventRegistrationDto = new EventRegistrationDto(PERSON_ID, EVENT_ID);

        when(personRepository.findById(PERSON_ID)).thenReturn(java.util.Optional.of(PERSON));
        when(eventRepository.findById(EVENT_ID)).thenReturn(java.util.Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> registrationService.registerForEvent(eventRegistrationDto)
        );

        assertEquals("Event not found", exception.getMessage());
        verify(registrationRepository, never()).save(any(Registration.class));
    }

    @Test
    public void testRegisterForEvent_EventIsFull() {
        // Arrange
        Event VALID_EVENT = new Event(VALID_TITLE, VALID_DESCRIPTION, VALID_START_TIME, VALID_END_TIME, VALID_MAX_PARTICIPANTS, VALID_LOCATION, VALID_HOST, VALID_BOARD_GAME);
        EventRegistrationDto eventRegistrationDto = new EventRegistrationDto(PERSON_ID, EVENT_ID);

        when(personRepository.findById(PERSON_ID)).thenReturn(java.util.Optional.of(PERSON));
        when(eventRepository.findById(EVENT_ID)).thenReturn(java.util.Optional.of(VALID_EVENT));
        when(registrationRepository.countByKeyEvent(VALID_EVENT)).thenReturn(10L); // Event full

        // Act & Assert
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> registrationService.registerForEvent(eventRegistrationDto)
        );

        assertEquals("Event is full", exception.getMessage());
        verify(registrationRepository, never()).save(any(Registration.class));
    }

    @Test
    public void testRegisterForEvent_UserAlreadyRegistered() {
        // Arrange
        Event VALID_EVENT = new Event(VALID_TITLE, VALID_DESCRIPTION, VALID_START_TIME, VALID_END_TIME, VALID_MAX_PARTICIPANTS, VALID_LOCATION, VALID_HOST, VALID_BOARD_GAME);
        EventRegistrationDto eventRegistrationDto = new EventRegistrationDto(PERSON_ID, EVENT_ID);

        when(personRepository.findById(PERSON_ID)).thenReturn(java.util.Optional.of(PERSON));
        when(eventRepository.findById(EVENT_ID)).thenReturn(java.util.Optional.of(VALID_EVENT));
        when(registrationRepository.existsByKeyPersonAndKeyEvent(PERSON, VALID_EVENT)).thenReturn(true);

        // Act & Assert
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> registrationService.registerForEvent(eventRegistrationDto)
        );

        assertEquals("User is already registered for this event", exception.getMessage());
        verify(registrationRepository, never()).save(any(Registration.class));
    }

    @Test
    public void testRegisterForEvent_Success() {
        // Arrange
        Event VALID_EVENT = new Event(VALID_TITLE, VALID_DESCRIPTION, VALID_START_TIME, VALID_END_TIME, VALID_MAX_PARTICIPANTS, VALID_LOCATION, VALID_HOST, VALID_BOARD_GAME);
        EventRegistrationDto eventRegistrationDto = new EventRegistrationDto(PERSON_ID, EVENT_ID);

        when(personRepository.findById(PERSON_ID)).thenReturn(java.util.Optional.of(PERSON));
        when(eventRepository.findById(EVENT_ID)).thenReturn(java.util.Optional.of(VALID_EVENT));
        when(registrationRepository.countByKeyEvent(VALID_EVENT)).thenReturn(5L); // Event not full
        when(registrationRepository.existsByKeyPersonAndKeyEvent(PERSON, VALID_EVENT)).thenReturn(false);

        // Act
        Registration registration = registrationService.registerForEvent(eventRegistrationDto);
        
        // Assert
        assertNotNull(registration);
        assertEquals(PERSON, registration.getKey().getPerson());
        assertEquals(VALID_EVENT, registration.getKey().getEvent());
        verify(registrationRepository, times(1)).save(any(Registration.class));
    }

    @Test
    public void testRegisterForEvent_OverlappingRegisteredEvent() {
        // Arrange
        // Register first to an event that will overlap with the second event
        Person otherHost = new Person("Natasha", "natas@mail.com", "pass123", false);
        Event otherEvent = new Event("Other Event", VALID_DESCRIPTION, VALID_START_TIME.plusHours(1), VALID_END_TIME.plusHours(1), VALID_MAX_PARTICIPANTS, VALID_LOCATION, otherHost, VALID_BOARD_GAME);
        int otherEventId = 101;
    
        EventRegistrationDto otherEventRegistrationDto = new EventRegistrationDto(PERSON_ID, otherEventId);
    
        when(personRepository.findById(PERSON_ID)).thenReturn(java.util.Optional.of(PERSON));
        when(eventRepository.findById(otherEventId)).thenReturn(java.util.Optional.of(otherEvent));
        when(registrationRepository.countByKeyEvent(otherEvent)).thenReturn(5L); // Event not full
        when(registrationRepository.existsByKeyPersonAndKeyEvent(PERSON, otherEvent)).thenReturn(false);
    
        Registration otherRegistration = registrationService.registerForEvent(otherEventRegistrationDto);
    
        assertNotNull(otherRegistration);
        assertEquals(PERSON, otherRegistration.getKey().getPerson());
        assertEquals(otherEvent, otherRegistration.getKey().getEvent());
        verify(registrationRepository, times(1)).save(any(Registration.class));
    
        List<Registration> existingRegistrations = List.of(otherRegistration);
        when(registrationRepository.findByKeyPerson(PERSON)).thenReturn(existingRegistrations);

        // Now register to an event that will overlap with the first event
        Event VALID_EVENT = new Event(VALID_TITLE, VALID_DESCRIPTION, VALID_START_TIME, VALID_END_TIME, VALID_MAX_PARTICIPANTS, VALID_LOCATION, VALID_HOST, VALID_BOARD_GAME);
        EventRegistrationDto eventRegistrationDto = new EventRegistrationDto(PERSON_ID, EVENT_ID);
    
        when(eventRepository.findById(EVENT_ID)).thenReturn(java.util.Optional.of(VALID_EVENT));
        when(registrationRepository.countByKeyEvent(VALID_EVENT)).thenReturn(5L); // Event not full
        when(registrationRepository.existsByKeyPersonAndKeyEvent(PERSON, VALID_EVENT)).thenReturn(false);
    
        // Act & Assert
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> registrationService.registerForEvent(eventRegistrationDto)
        );
        assertEquals("User has a timing conflict with another registered event: " + otherEvent.getTitle(), exception.getMessage());
        
        //verify the overlapping registration was not saved
        verify(registrationRepository, never()).save(argThat(reg -> reg.getKey().getEvent().equals(VALID_EVENT)));
    }

    @Test
    public void testUnregisterFromEvent_Sucess(){
        //Arrange
        Event VALID_EVENT = new Event(VALID_TITLE, VALID_DESCRIPTION, VALID_START_TIME, VALID_END_TIME, VALID_MAX_PARTICIPANTS, VALID_LOCATION, VALID_HOST, VALID_BOARD_GAME);
        EventRegistrationDto eventRegistrationDto = new EventRegistrationDto(PERSON_ID, EVENT_ID);

        when(personRepository.findById(PERSON_ID)).thenReturn(java.util.Optional.of(PERSON));
        when(eventRepository.findById(EVENT_ID)).thenReturn(java.util.Optional.of(VALID_EVENT));
        when(registrationRepository.findByKeyPersonAndKeyEvent(PERSON, VALID_EVENT)).thenReturn(new Registration(new Registration.Key(VALID_EVENT, PERSON), LocalDateTime.now()));

        //Act 
        registrationService.unregisterFromEvent(eventRegistrationDto);

        //Assert
        verify(registrationRepository, times(1)).delete(any(Registration.class));
    }

    @Test
    public void testUnregisterFromEvent_PersonNotRegistered(){
        //Arrange
        Event VALID_EVENT = new Event(VALID_TITLE, VALID_DESCRIPTION, VALID_START_TIME, VALID_END_TIME, VALID_MAX_PARTICIPANTS, VALID_LOCATION, VALID_HOST, VALID_BOARD_GAME);
        EventRegistrationDto eventRegistrationDto = new EventRegistrationDto(PERSON_ID, EVENT_ID);

        when(personRepository.findById(PERSON_ID)).thenReturn(java.util.Optional.of(PERSON));
        when(eventRepository.findById(EVENT_ID)).thenReturn(java.util.Optional.of(VALID_EVENT));
        when(registrationRepository.findByKeyPersonAndKeyEvent(PERSON, VALID_EVENT)).thenReturn(null);

        // Act & Assert
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> registrationService.unregisterFromEvent(eventRegistrationDto)
        );
        assertEquals("User is not registered for this event", exception.getMessage());
        verify(registrationRepository, never()).delete(any(Registration.class));
    }

}
