package ca.mcgill.ecse321.boardroom.services;

import ca.mcgill.ecse321.boardroom.model.BoardGame;
import ca.mcgill.ecse321.boardroom.model.Location;
import ca.mcgill.ecse321.boardroom.model.Person;
import ca.mcgill.ecse321.boardroom.repositories.EventRepository;
import ca.mcgill.ecse321.boardroom.dtos.EventCreationDto;
import ca.mcgill.ecse321.boardroom.model.Event;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;


//@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class EventServiceTest {
    @Mock
    private EventRepository repo;
    @InjectMocks
    private EventService eventService;
    private static final String VALID_TITLE = "Board Game Night";
    private static final String VALID_DESCRIPTION = "A fun night of board games!";
    private static final LocalDateTime VALID_START_TIME = LocalDateTime.now().plusDays(1);
    private static final LocalDateTime VALID_END_TIME = LocalDateTime.now().plusDays(1).plusHours(2);
    private static final int VALID_MAX_PARTICIPANTS = 10;
    private static final Person VALID_HOST = new Person("validUser", "valid@email.com", "password", false);
    private static final Location VALID_LOCATION = new Location("McGill", "MTL", "QC");
    private static final BoardGame VALID_BOARD_GAME = new BoardGame("Uno", "fun card game", 2, 12345);

    @Test
    public void testCreateValidEvent() {
        //Arrange
        EventCreationDto newEventDto = new EventCreationDto(
                VALID_TITLE, VALID_DESCRIPTION, VALID_START_TIME, VALID_END_TIME,
                VALID_MAX_PARTICIPANTS, VALID_LOCATION, VALID_HOST, VALID_BOARD_GAME
        );

        when(repo.save(any(Event.class))).thenAnswer(invocation -> invocation.getArgument(0));

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
        verify(repo, times(1)).save(any(Event.class));
    }
    @Test
    public void testCreateEventWithInvalidTimes() {
        // Arrange: Start time in the past
        EventCreationDto invalidEventDto = new EventCreationDto(
                VALID_TITLE,
                VALID_DESCRIPTION,
                LocalDateTime.now().minusDays(1), // Past start time
                VALID_END_TIME,
                VALID_MAX_PARTICIPANTS,
                VALID_LOCATION,
                VALID_HOST,
                VALID_BOARD_GAME
        );

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> eventService.createEvent(invalidEventDto)
        );

        assertEquals("Start time cannot be in the past", exception.getMessage());

        // Verify that save was never called
        verify(repo, never()).save(any(Event.class));
    }

    @Test
    public void testCreateEventWithEndTimeBeforeStartTime() {
        EventCreationDto invalidEventDto = new EventCreationDto(
                VALID_TITLE,
                VALID_DESCRIPTION,
                VALID_END_TIME,  // Swapped start and end times
                VALID_START_TIME,
                VALID_MAX_PARTICIPANTS,
                VALID_LOCATION,
                VALID_HOST,
                VALID_BOARD_GAME
        );

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> eventService.createEvent(invalidEventDto)
        );

        assertEquals("End time must be after start time", exception.getMessage());

        // Verify that save was never called
        verify(repo, never()).save(any(Event.class));
    }
}
