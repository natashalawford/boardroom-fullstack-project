package ca.mcgill.ecse321.boardroom.controller;

import ca.mcgill.ecse321.boardroom.dtos.PersonCreationDto;
import ca.mcgill.ecse321.boardroom.dtos.PersonRequestDto;
import ca.mcgill.ecse321.boardroom.dtos.responses.PersonResponseDto;
import ca.mcgill.ecse321.boardroom.services.PersonService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

 
@RestController
public class PersonController {
    @Autowired
    private PersonService personService;

    
    @PostMapping("people")
    @ResponseStatus(HttpStatus.CREATED)
    public PersonResponseDto createPerson(@RequestBody PersonCreationDto personToCreate) {
        return new PersonResponseDto(personService.createPerson(personToCreate));
    }

    /**
     * Update person type
     */
    @PutMapping("people/{id}/role")
    public PersonResponseDto toggleAccountType(@PathVariable("id") int id,@RequestBody PersonRequestDto partialUpdatedPerson) {
        
        PersonResponseDto updatedPerson = personService.updatePerson(id, partialUpdatedPerson);

        return updatedPerson;

    }
}
