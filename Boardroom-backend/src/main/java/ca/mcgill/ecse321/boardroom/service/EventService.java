package ca.mcgill.ecse321.boardroom.service;

import ca.mcgill.ecse321.boardroom.repositories.EventRepository;
import ca.mcgill.ecse321.boardroom.model.Event;
import ca.mcgill.ecse321.boardroom.model.Person;
import ca.mcgill.ecse321.boardroom.requests.CreateEventRequest;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EventService {

    private final EventRepository eventRepository;

    @Autowired
    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Transactional
    public Event createEvent(CreateEventRequest eventToCreate) {
        if(eventToCreate.getDescription() == null)
            throw ...
        validateEventTimes(eventToCreate.getStartDateTime(), eventToCreate.getEndDateTime());

        Event event = new Event(
                eventToCreate.getTitle(),
                eventToCreate.getDescription(),
                eventToCreate.getStartDateTime(),
                eventToCreate.getEndDateTime(),
                eventToCreate.getMaxParticipants(),
                eventToCreate.getLocation(),
                eventToCreate.getEventHost(),
                eventToCreate.getBoardGame()
        );

        return eventRepository.save(event);
    }


    private void validateEventTimes(LocalDateTime startTime, LocalDateTime endTime) {
        LocalDateTime now = LocalDateTime.from(Instant.now());

        if (startTime.isBefore(now)) {
            throw new IllegalArgumentException(
                    "Start time cannot be in the past"
            );
        }

        if (endTime.isBefore(startTime)) {
            throw new IllegalArgumentException(
                    "End time must be after start time"
            );
        }
    }
}