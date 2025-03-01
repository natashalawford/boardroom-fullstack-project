package ca.mcgill.ecse321.boardroom.service;

import ca.mcgill.ecse321.boardroom.repositories.EventRepository;
import ca.mcgill.ecse321.boardroom.requests.CreateEventRequest;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;
import ca.mcgill.ecse321.boardroom.model.Event;
import static org.junit.jupiter.api.Assertions.*;



import static org.mockito.Mockito.when;

@SpringBootTest
public class EventServiceTest {
    @Mock
    private EventRepository repo;
    @InjectMocks
    private EventService eventService;
    private static final String VALID_TITLE;

    public void findEventByValidID() {
        Event event = new
        when(repo.findEventById(1)).thenReturn();
    }
    @Test
    public void creatValidEvent() {
        //Arrange
        when(repo.save(any(Event.class))).thenAnswer((InvocationOnMock iom) -> iom.getArgument(0));

        CreateEventRequest newEvent = new CreateEventRequest();
        Event event = eventService.createEvent(newEvent);

        assertNotNull(event);
        assertEquals(VALID_TITLE, event.getTitle());
        verify(repo, times(1)).save(any(Event.class));
    }
}
