package ca.mcgill.ecse321.boardroom.services;

import ca.mcgill.ecse321.boardroom.model.Event;
import ca.mcgill.ecse321.boardroom.model.Person;
import ca.mcgill.ecse321.boardroom.model.Registration;
import ca.mcgill.ecse321.boardroom.model.Registration.Key;
import ca.mcgill.ecse321.boardroom.repositories.EventRepository;
import ca.mcgill.ecse321.boardroom.repositories.PersonRepository;
import ca.mcgill.ecse321.boardroom.repositories.RegistrationRepository;
import ca.mcgill.ecse321.boardroom.dtos.EventRegistrationDto;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
    public Registration registerForEvent(EventRegistrationDto eventRegistrationDto) {

        // Get the user
        int personId = eventRegistrationDto.getPersonId();
        Person person = personRepository.findById(personId).orElse(null);

        // Check if person exists, otherwise throw an exception
        if (person == null) {
            throw new IllegalArgumentException("Person not found");
        }

        // Get the event
        int eventId = eventRegistrationDto.getEventId();
        Event event = eventRepository.findById(eventId).orElse(null);

        // Check if event exists, otherwise throw an exception
        if (event == null) {
            throw new IllegalArgumentException("Event not found");
        }

        // Check if event is already full, if it is, throw an exception
        long registeredCount = registrationRepository.countByKeyEvent(event);
        if (registeredCount >= event.getMaxParticipants()) {
            throw new IllegalStateException("Event is full");
        }

        // Check if the user is already registered for this event
        if (registrationRepository.existsByKeyPersonAndKeyEvent(person, event)) {
            throw new IllegalStateException("User is already registered for this event");
        }

        // Check if the user is registered for a conflicting event
        List<Registration> existingRegistrations = registrationRepository.findByKeyPerson(person);
        for (Registration registration : existingRegistrations) {
            Event registeredEvent = registration.getKey().getEvent();
            if (eventsOverlap(registeredEvent, event)) {
                throw new IllegalStateException("User has a timing conflict with another registered event: " + registeredEvent.getTitle());
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
                 e2.getEndDateTime().isBefore(e1.getStartDateTime())); // otherwise event 2 ending is before event 1 starts
                 // if neither of these are true, the events overlap
    }
}