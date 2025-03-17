package ca.mcgill.ecse321.boardroom.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ca.mcgill.ecse321.boardroom.dtos.EventRegistrationDto;
import ca.mcgill.ecse321.boardroom.dtos.responses.EventRegistrationResponseDto;
import ca.mcgill.ecse321.boardroom.model.Registration;
import ca.mcgill.ecse321.boardroom.services.RegistrationService;

@RestController
public class RegistrationController {
    @Autowired
    private RegistrationService registrationService;

    /**
     * Register a person for an event.
     * 
     * @param eventRegistrationDto
     * @return EventRegistrationResponseDto
     * 
     */
    @PutMapping("/registration")
    @RequestMapping("/registration")
    public EventRegistrationResponseDto registerForEvent(@RequestBody EventRegistrationDto eventRegistrationDto) {
        Registration registration = registrationService.registerForEvent(eventRegistrationDto);
        return new EventRegistrationResponseDto(registration);
    }

    /**
     * Unregister a person from an event
     * @param eventRegistrationDto
     * @return void
     * 
     */
    @DeleteMapping("/unregistration")
    @RequestMapping("/unregistration")
    public void unregisterFromEvent(@RequestBody EventRegistrationDto eventRegistrationDto) {
        registrationService.unregisterFromEvent(eventRegistrationDto);
    }


    
}