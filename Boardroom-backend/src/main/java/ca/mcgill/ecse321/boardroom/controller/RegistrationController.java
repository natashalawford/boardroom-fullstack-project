package ca.mcgill.ecse321.boardroom.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ca.mcgill.ecse321.boardroom.dtos.responses.EventRegistrationResponseDto;
import ca.mcgill.ecse321.boardroom.model.Registration;
import ca.mcgill.ecse321.boardroom.services.RegistrationService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/registration")
public class RegistrationController {
    @Autowired
    private RegistrationService registrationService;

     
    /** 
     * @param personId
     * @param eventId
     * @return EventRegistrationResponseDto
     */
    @PutMapping("/{personId}/{eventId}")
    @CrossOrigin(origins = "http://localhost:5173")
    public EventRegistrationResponseDto registerForEvent(@PathVariable("personId") int personId, @PathVariable("eventId") int eventId) {
        Registration registration = registrationService.registerForEvent(personId, eventId);
        return new EventRegistrationResponseDto(registration);
    }

    
    
    /** 
     * @param personId
     * @param eventId
     */
    @DeleteMapping("/{personId}/{eventId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CrossOrigin(origins = "http://localhost:5173")
    public void unregisterFromEvent(@PathVariable("personId") int personId, @PathVariable("eventId") int eventId) {
        registrationService.unregisterFromEvent(personId, eventId);
    }

    
    
    /** 
     * @param personId
     * @param eventId
     * @return EventRegistrationResponseDto
     */
    @GetMapping("/{personId}/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    @CrossOrigin(origins = "http://localhost:5173")
    public EventRegistrationResponseDto getRegistration(@PathVariable("personId") int personId, @PathVariable("eventId") int eventId) {
        Registration registration = registrationService.getRegistration(personId, eventId);
        return new EventRegistrationResponseDto(registration);
    }


    /**
     * Retrieves all registrations for a given event.
     * @param eventId The ID of the event.
     * @return List of EventRegistrationResponseDto objects containing registration details.
     */
    @GetMapping("/event/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    @CrossOrigin(origins = "http://localhost:5173")
    public List<EventRegistrationResponseDto> getRegistrationsForEvent(@PathVariable int eventId) {
        List<Registration> registrations = registrationService.getRegistrationsForEvent(eventId);
        return registrations.stream()
                .map(EventRegistrationResponseDto::new)
                .collect(Collectors.toList());
    }
}