package ca.mcgill.ecse321.boardroom.integration;

import ca.mcgill.ecse321.boardroom.model.Event;
import ca.mcgill.ecse321.boardroom.requests.CreateEventRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EventIntegrationTests {
    @Autowired
    private TestRestTemplate client;

    String VALID_TITLE;
    String

    @Test
    public void createValidEvent() {
        CreateEventRequest body = new CreateEventRequest();
        ResponseEntity<EventResponseDTO> response = client.postForEntity("/event", body, EventResponseDTO.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        EventResponseDTO responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(VALID_TITLE, responseBody.getTitle());
        assertTrue(responseBody.getId() > 0, "the ID should be greater than zero");



    }
}
