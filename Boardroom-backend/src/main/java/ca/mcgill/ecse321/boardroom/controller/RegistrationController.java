package ca.mcgill.ecse321.boardroom.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ca.mcgill.ecse321.boardroom.dtos.responses.EventRegistrationResponseDto;
import ca.mcgill.ecse321.boardroom.model.Registration;
import ca.mcgill.ecse321.boardroom.services.RegistrationService;

@RestController
@RequestMapping("/registration/{personId}/{eventId}")
public class RegistrationController {
    @Autowired
    private RegistrationService registrationService;

     
    /** 
     * @param personId
     * @param eventId
     * @return EventRegistrationResponseDto
     */
    @PutMapping
    public EventRegistrationResponseDto registerForEvent(@PathVariable("personId") int personId, @PathVariable("eventId") int eventId) {
        Registration registration = registrationService.registerForEvent(personId, eventId);
        return new EventRegistrationResponseDto(registration);
    }

    
    
    /** 
     * @param personId
     * @param eventId
     */
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unregisterFromEvent(@PathVariable("personId") int personId, @PathVariable("eventId") int eventId) {
        registrationService.unregisterFromEvent(personId, eventId);
    }

    
    
    /** 
     * @param personId
     * @param eventId
     * @return EventRegistrationResponseDto
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public EventRegistrationResponseDto getRegistration(@PathVariable("personId") int personId, @PathVariable("eventId") int eventId) {
        Registration registration = registrationService.getRegistration(personId, eventId);
        return new EventRegistrationResponseDto(registration);
    }
}