package ca.mcgill.ecse321.boardroom.controller;

import ca.mcgill.ecse321.boardroom.dtos.PersonRequestDto;
import ca.mcgill.ecse321.boardroom.dtos.responses.PersonResponseDto;
import ca.mcgill.ecse321.boardroom.exceptions.BoardroomException;
import ca.mcgill.ecse321.boardroom.model.Person;
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
    public ResponseEntity<Void> changePassword(@PathVariable("id") int id, @RequestBody String passwordChange) {

        try {
            personService.changePassword(id, passwordChange);
            return ResponseEntity.ok().build();
        } catch (BoardroomException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

    }
}