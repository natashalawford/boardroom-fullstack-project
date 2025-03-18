package ca.mcgill.ecse321.boardroom.services;

import ca.mcgill.ecse321.boardroom.exceptions.BoardroomException;
import ca.mcgill.ecse321.boardroom.model.*;
import ca.mcgill.ecse321.boardroom.repositories.EventRepository;
import ca.mcgill.ecse321.boardroom.dtos.EventCreationDto;

import java.time.LocalDateTime;
import java.util.List;

import ca.mcgill.ecse321.boardroom.repositories.PersonRepository;
import ca.mcgill.ecse321.boardroom.repositories.BoardGameRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EventService {
        @Autowired
        private EventRepository eventRepository;
        @Autowired
        private PersonRepository personRepository;
        @Autowired
        private BoardGameRepository boardGameRepository;

        @Transactional
        public Event createEvent(EventCreationDto eventToCreate) {
                validateEventTimes(eventToCreate.getStartDateTime(), eventToCreate.getEndDateTime());

                Person personToFind = personRepository.findById(eventToCreate.getHostId()).orElseThrow(
                                () -> new BoardroomException(HttpStatus.NOT_FOUND,
                                                "A person with this id does not exist"));
                BoardGame boardGameToFind = boardGameRepository.findById(eventToCreate.getBoardGameName()).orElseThrow(
                                () -> new BoardroomException(HttpStatus.NOT_FOUND,
                                                "A board game with this name does not exist"));

                Event event = new Event(
                                eventToCreate.getTitle(),
                                eventToCreate.getDescription(),
                                eventToCreate.getStartDateTime(),
                                eventToCreate.getEndDateTime(),
                                eventToCreate.getMaxParticipants(),
                                eventToCreate.getLocation(),
                                personToFind,
                                boardGameToFind);

                return eventRepository.save(event);
        }

        public Event findEventById(int id) {
                Event event = eventRepository.findEventById(id);
                if(event == null) {
                        throw new BoardroomException(
                                HttpStatus.NOT_FOUND,
                                String.format("no event has ID %d", id));
                }
                return event;
        }

        public List<Event> getEvents() {
                List<Event> events = (List<Event>) eventRepository.findAll();
                return events;
        }

        @Transactional
        public void deleteEventById(int id) {
                Event event = eventRepository.findEventById(id);
                if(event != null) {
                        eventRepository.deleteById(id);
                } else {
                        throw new BoardroomException(
                                HttpStatus.NOT_FOUND,
                                String.format("no event has ID %d", id));
                }
        }

        private void validateEventTimes(LocalDateTime startTime, LocalDateTime endTime) {
                LocalDateTime now = LocalDateTime.now();

                if (startTime.isBefore(now)) {
                        throw new BoardroomException(
                                        HttpStatus.BAD_REQUEST,
                                        "Start time cannot be in the past");
                }

                if (endTime.isBefore(startTime)) {
                        throw new BoardroomException(
                                        HttpStatus.BAD_REQUEST,
                                        "End time must be after start time");
                }
        }
}