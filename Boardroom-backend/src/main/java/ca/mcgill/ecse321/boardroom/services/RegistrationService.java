package ca.mcgill.ecse321.boardroom.services;

import ca.mcgill.ecse321.boardroom.model.Event;
import ca.mcgill.ecse321.boardroom.model.Person;
import ca.mcgill.ecse321.boardroom.model.Registration;
import ca.mcgill.ecse321.boardroom.model.Registration.Key;
import ca.mcgill.ecse321.boardroom.repositories.EventRepository;
import ca.mcgill.ecse321.boardroom.repositories.PersonRepository;
import ca.mcgill.ecse321.boardroom.repositories.RegistrationRepository;
import ca.mcgill.ecse321.boardroom.exceptions.BoardroomException;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegistrationService {

    private final EventRepository eventRepository;
    private final PersonRepository personRepository;
    private final RegistrationRepository registrationRepository;

    @Autowired
    public RegistrationService(EventRepository eventRepository,
            PersonRepository personRepository,
            RegistrationRepository registrationRepository) {
        this.eventRepository = eventRepository;
        this.personRepository = personRepository;
        this.registrationRepository = registrationRepository;
    }

    @Transactional
    public Registration registerForEvent(int personId, int eventId) {

        // Get the user
        Person person = personRepository.findPersonById(personId);

        // Check if person exists, otherwise throw an exception
        if (person == null) {
            throw new BoardroomException(HttpStatus.NOT_FOUND, "Person not found");
        }

        // Get the event
        Event event = eventRepository.findEventById(eventId);

        // Check if event exists, otherwise throw an exception
        if (event == null) {
            throw new BoardroomException(HttpStatus.NOT_FOUND, "Event not found");
        }

        // Check if event is already full, if it is, throw an exception
        long registeredCount = registrationRepository.countByKeyEvent(event);
        if (registeredCount >= event.getMaxParticipants()) {
            throw new BoardroomException(HttpStatus.BAD_REQUEST, "Event is full");
        }

        // Check if the user is already registered for this event
        if (registrationRepository.existsByKeyPersonAndKeyEvent(person, event)) {
            throw new BoardroomException(HttpStatus.BAD_REQUEST, "User is already registered for this event");
        }

        // Check if the user is registered for a conflicting event
        List<Registration> existingRegistrations = registrationRepository.findByKeyPerson(person);
        for (Registration registration : existingRegistrations) {
            Event registeredEvent = registration.getKey().getEvent();
            if (eventsOverlap(registeredEvent, event)) {
                throw new BoardroomException(HttpStatus.BAD_REQUEST,
                        "User has a timing conflict with another registered event: " + registeredEvent.getTitle());
            }
        }

        // Register the user for the event
        Registration registration = new Registration(new Key(event, person), LocalDateTime.now());
        registrationRepository.save(registration);

        return registration;
    }

    // Checks if two events overlap
    private boolean eventsOverlap(Event e1, Event e2) {
        return !(e1.getEndDateTime().isBefore(e2.getStartDateTime()) || // event 1 ending is before event 2 starts
                e2.getEndDateTime().isBefore(e1.getStartDateTime())); // otherwise event 2 ending is before event 1
                                                                      // starts
        // if neither of these are true, the events overlap
    }

    @Transactional
    public void unregisterFromEvent(int personId, int eventId) {
        Person person = personRepository.findPersonById(personId);

        if (person == null) {
            throw new BoardroomException(HttpStatus.NOT_FOUND, "Person not found");
        }
        // Get the event
        Event event = eventRepository.findEventById(eventId);

        // Check if event exists, otherwise throw an exception
        if (event == null) {
            throw new BoardroomException(HttpStatus.NOT_FOUND, "Event not found");
        }

        Registration registration = registrationRepository.findByKeyPersonAndKeyEvent(person, event);

        // If registration is null, the user is not actually registered for this event,
        // throw an exception
        if (registration == null) {
            throw new BoardroomException(HttpStatus.BAD_REQUEST, "User is not registered for this event");
        }

        // otherwise, delete the registration
        registrationRepository.delete(registration);
    }

    public Registration getRegistration(int personId, int eventId) {

        Person person = personRepository.findPersonById(personId);
        Event event = eventRepository.findEventById(eventId);

        // check if person and event exist
        if (person == null) {
            throw new BoardroomException(HttpStatus.NOT_FOUND, "Person not found");
        }

        if (event == null) {
            throw new BoardroomException(HttpStatus.NOT_FOUND, "Event not found");
        }

        Registration registration = registrationRepository.findByKeyPersonAndKeyEvent(person, event);

        if (registration == null) {
            throw new BoardroomException(HttpStatus.NOT_FOUND, "Registration not found");
        }

        return registration;
    }
}