package ca.mcgill.ecse321.boardroom.controller;

import ca.mcgill.ecse321.boardroom.dtos.PersonCreationDto;
import ca.mcgill.ecse321.boardroom.dtos.PersonRequestDto;
import ca.mcgill.ecse321.boardroom.dtos.responses.PersonResponseDto;
import ca.mcgill.ecse321.boardroom.services.PersonService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UpdatePersonController {
    @Autowired
    private PersonService personService;

    /**
     * Modify profile
     */
    @PutMapping("people/{id}/profile")
    public PersonResponseDto modifyProfile(@PathVariable("id") int id, @RequestBody PersonRequestDto updatedPerson) {
        PersonResponseDto personResponse = personService.updatePerson(id, updatedPerson);
        return personResponse;
    }

    /**
     * Change password
     */
    @PutMapping("people/{id}/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changePassword(@PathVariable("id") int id, @RequestBody String passwordChangeDto) {
        personService.changePassword(id, passwordChangeDto);
    }
}