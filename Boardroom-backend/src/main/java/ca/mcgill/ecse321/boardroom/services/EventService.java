package ca.mcgill.ecse321.boardroom.services;

import ca.mcgill.ecse321.boardroom.repositories.EventRepository;
import ca.mcgill.ecse321.boardroom.model.Event;
import ca.mcgill.ecse321.boardroom.model.Person;
import ca.mcgill.ecse321.boardroom.dtos.EventCreationDto;

import java.time.Instant;
import java.time.LocalDateTime;
import jakarta.validation.Valid;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;


@Service
@Validated
public class EventService {

    private final EventRepository eventRepository;

    @Autowired
    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Transactional
    public Event createEvent(@Valid EventCreationDto eventToCreate) {
        validateEventTimes(eventToCreate.getStartDateTime(), eventToCreate.getEndDateTime());

        Event event = new Event(
                eventToCreate.getTitle(),
                eventToCreate.getDescription(),
                eventToCreate.getStartDateTime(),
                eventToCreate.getEndDateTime(),
                eventToCreate.getMaxParticipants(),
                eventToCreate.getLocation(),
                eventToCreate.getHost(),
                eventToCreate.getBoardGame()
        );

        return eventRepository.save(event);
    }


    private void validateEventTimes(LocalDateTime startTime, LocalDateTime endTime) {
        LocalDateTime now = LocalDateTime.now();

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