package ca.mcgill.ecse321.boardroom.services;

import ca.mcgill.ecse321.boardroom.dtos.creation.EventCreationDto;
import ca.mcgill.ecse321.boardroom.exceptions.BoardroomException;
import ca.mcgill.ecse321.boardroom.model.BoardGame;
import ca.mcgill.ecse321.boardroom.model.Person;
import ca.mcgill.ecse321.boardroom.repositories.EventRepository;
import ca.mcgill.ecse321.boardroom.repositories.BoardGameRepository;
import ca.mcgill.ecse321.boardroom.repositories.PersonRepository;
import ca.mcgill.ecse321.boardroom.model.Event;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
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
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


//@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class EventServiceTests {
    @Mock
    private EventRepository eventRepository;
    @Mock
    private PersonRepository personRepository;
    @Mock
    private BoardGameRepository boardGameRepository;
    @InjectMocks
    private EventService eventService;
    private static final String VALID_TITLE = "Board Game Night";
    private static final String VALID_DESCRIPTION = "A fun night of board games!";
    private static final LocalDateTime VALID_START_TIME = LocalDateTime.now().plusDays(1);
    private static final LocalDateTime VALID_END_TIME = LocalDateTime.now().plusDays(1).plusHours(2);
    private static final int VALID_MAX_PARTICIPANTS = 10;

    private static final String VALID_LOCATION = "1234 rue Sainte-Catherine";
    private static Person VALID_HOST;
    private static BoardGame VALID_BOARD_GAME;
    private int hostId;
    private String boardGameName;

    @BeforeEach
    public void setup() {
        VALID_HOST = new Person("Alice", "alice@mail.com", "securepass", false);
        int hostId = VALID_HOST.getId();
        VALID_BOARD_GAME = new BoardGame("Uno", "A fun card game", 2, 54321);
        String boardGameName = VALID_BOARD_GAME.getTitle();

        this.hostId = hostId;
        this.boardGameName = boardGameName;
    }

    @Test
    public void testCreateValidEvent() {
        //Arrange
        when(personRepository.findById(hostId)).thenReturn(Optional.of(VALID_HOST));
        when(boardGameRepository.findById(boardGameName)).thenReturn(Optional.of(VALID_BOARD_GAME));
        when(eventRepository.save(any(Event.class))).thenAnswer(invocation -> invocation.getArgument(0));

        EventCreationDto newEventDto = new EventCreationDto(
                VALID_TITLE, VALID_DESCRIPTION, VALID_START_TIME, VALID_END_TIME,
                VALID_MAX_PARTICIPANTS, VALID_LOCATION, hostId, boardGameName
        );


        Event createdEvent = eventService.createEvent(newEventDto);

        assertNotNull(createdEvent);
        assertEquals(VALID_TITLE, createdEvent.getTitle());
        assertEquals(VALID_DESCRIPTION, createdEvent.getDescription());
        assertEquals(VALID_START_TIME, createdEvent.getStartDateTime());
        assertEquals(VALID_END_TIME, createdEvent.getEndDateTime());
        assertEquals(VALID_MAX_PARTICIPANTS, createdEvent.getMaxParticipants());
        assertEquals(VALID_LOCATION, createdEvent.getLocation());
        assertEquals(VALID_HOST, createdEvent.getEventHost());
        assertEquals(VALID_BOARD_GAME, createdEvent.getBoardGame());
        verify(eventRepository, times(1)).save(any(Event.class));
    }
    @Test
    public void testCreateEventWithInvalidTimes() {
        // Arrange: Start time in the past
        EventCreationDto invalidEventDto = new EventCreationDto(
                VALID_TITLE, VALID_DESCRIPTION,
                LocalDateTime.now().minusDays(1), // Past start time
                VALID_END_TIME, VALID_MAX_PARTICIPANTS,
                VALID_LOCATION, hostId, boardGameName
        );

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> eventService.createEvent(invalidEventDto)
        );

        assertEquals("Start time cannot be in the past", exception.getMessage());

        // Verify that save was never called
        verify(eventRepository, never()).save(any(Event.class));
    }

    @Test
    public void testCreateEventWithEndTimeBeforeStartTime() {
        EventCreationDto invalidEventDto = new EventCreationDto(
                VALID_TITLE, VALID_DESCRIPTION,
                VALID_END_TIME,  // Swapped start and end times
                VALID_START_TIME,
                VALID_MAX_PARTICIPANTS, VALID_LOCATION, hostId, boardGameName
        );

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> eventService.createEvent(invalidEventDto)
        );

        assertEquals("End time must be after start time", exception.getMessage());

        // Verify that save was never called
        verify(eventRepository, never()).save(any(Event.class));
    }

    @Test
    public void testCreateEventWithNonexistentHost() {
        when(personRepository.findById(999)).thenReturn(Optional.empty());

        EventCreationDto invalidEventDto = new EventCreationDto(
                VALID_TITLE, VALID_DESCRIPTION, VALID_START_TIME, VALID_END_TIME,
                VALID_MAX_PARTICIPANTS, VALID_LOCATION, 999, boardGameName
        );

        BoardroomException exception = assertThrows(
                BoardroomException.class,
                () -> eventService.createEvent(invalidEventDto)
        );

        assertEquals("A person with this id does not exist", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());

        verify(eventRepository, never()).save(any(Event.class));
    }

    @Test
    public void testCreateEventWithNonexistentBoardGame() {
        when(personRepository.findById(hostId)).thenReturn(Optional.of(VALID_HOST));
        when(boardGameRepository.findById("NonexistentGame")).thenReturn(Optional.empty());

        EventCreationDto invalidEventDto = new EventCreationDto(
                VALID_TITLE, VALID_DESCRIPTION, VALID_START_TIME, VALID_END_TIME,
                VALID_MAX_PARTICIPANTS, VALID_LOCATION, hostId, "NonexistentGame"
        );

        BoardroomException exception = assertThrows(
                BoardroomException.class,
                () -> eventService.createEvent(invalidEventDto)
        );

        assertEquals("A board game with this name does not exist", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());

        verify(eventRepository, never()).save(any(Event.class));
    }

    @Test
    public void testFindEventByValidId() {
        // Arrange
        Event event = new Event(
                VALID_TITLE, VALID_DESCRIPTION, VALID_START_TIME, VALID_END_TIME,
                VALID_MAX_PARTICIPANTS, VALID_LOCATION, VALID_HOST, VALID_BOARD_GAME
        );

        when(eventRepository.findEventById(42)).thenReturn(event);

        // Act
        Event foundEvent = eventService.findEventById(42);

        // Assert
        assertNotNull(foundEvent);
        assertEquals(VALID_TITLE, foundEvent.getTitle());
        assertEquals(VALID_DESCRIPTION, foundEvent.getDescription());
        assertEquals(VALID_START_TIME, foundEvent.getStartDateTime());
        assertEquals(VALID_END_TIME, foundEvent.getEndDateTime());
        assertEquals(VALID_MAX_PARTICIPANTS, foundEvent.getMaxParticipants());
        assertEquals(VALID_LOCATION, foundEvent.getLocation());
        assertEquals(VALID_HOST, foundEvent.getEventHost());
        assertEquals(VALID_BOARD_GAME, foundEvent.getBoardGame());

        verify(eventRepository, times(1)).findEventById(42);
    }

    @Test
    public void testFindEventThatDoesntExist() {
        // Arrange
        when(eventRepository.findEventById(99)).thenReturn(null);

        // Act + Assert
        BoardroomException e = assertThrows(
                BoardroomException.class,
                () -> eventService.findEventById(99) // Assuming there's a personService
        );

        assertEquals(HttpStatus.NOT_FOUND, e.getStatus());
        assertEquals("no event has ID 99", e.getMessage());
    }

    @Test
    public void testDeleteEventById_Success() {
        // Arrange
        int eventId = 42;
        Event event = new Event(
                VALID_TITLE, VALID_DESCRIPTION, VALID_START_TIME, VALID_END_TIME,
                VALID_MAX_PARTICIPANTS, VALID_LOCATION, VALID_HOST, VALID_BOARD_GAME
        );

        when(eventRepository.findEventById(eventId)).thenReturn(event);

        // Act
        eventService.deleteEventById(eventId);

        // Assert
        verify(eventRepository, times(1)).deleteById(eventId);
    }

    @Test
    public void testDeleteEventById_NotFound() {
        // Arrange
        int eventId = 99; // Nonexistent event
        when(eventRepository.findEventById(eventId)).thenReturn(null);

        // Act + Assert
        BoardroomException exception = assertThrows(
                BoardroomException.class,
                () -> eventService.deleteEventById(eventId)
        );

        assertEquals("no event has ID 99", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());

        verify(eventRepository, never()).deleteById(anyInt());
    }

    @Test
    public void testGetEvents_Success() {
        // Arrange
        List<Event> mockEvents = new ArrayList<>();
        mockEvents.add(new Event("Game Night", "Play games", VALID_START_TIME, VALID_END_TIME, 10, VALID_LOCATION, VALID_HOST, VALID_BOARD_GAME));
        mockEvents.add(new Event("Chess Tournament", "Compete in chess", VALID_START_TIME, VALID_END_TIME, 20, VALID_LOCATION, VALID_HOST, VALID_BOARD_GAME));

        when(eventRepository.findAll()).thenReturn(mockEvents);

        // Act
        List<Event> events = eventService.getEvents();

        // Assert
        assertNotNull(events);
        assertEquals(2, events.size());
        verify(eventRepository, times(1)).findAll();
    }

    @Test
    public void testGetEvents_EmptyList() {
        // Arrange
        when(eventRepository.findAll()).thenReturn(new ArrayList<>()); // No events

        // Act
        List<Event> events = eventService.getEvents();

        // Assert
        assertNotNull(events);
        assertTrue(events.isEmpty());
        verify(eventRepository, times(1)).findAll();
    }

}
