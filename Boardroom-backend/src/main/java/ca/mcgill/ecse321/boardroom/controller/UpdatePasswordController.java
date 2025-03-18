package ca.mcgill.ecse321.boardroom.controller;

import ca.mcgill.ecse321.boardroom.dtos.PersonUpdatePasswordDto;
import ca.mcgill.ecse321.boardroom.exceptions.BoardroomException;
import ca.mcgill.ecse321.boardroom.services.PersonService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UpdatePasswordController {
    @Autowired
    private PersonService personService;

    /**
     * Change password
     */
     @PutMapping("people/{id}/password")
    @ResponseStatus(HttpStatus.OK) // Automatically returns 200 OK if no exception is thrown
    public void changePassword(@PathVariable("id") int id, @RequestBody PersonUpdatePasswordDto passwordDto) {
        personService.changePassword(id, passwordDto);
    }
}