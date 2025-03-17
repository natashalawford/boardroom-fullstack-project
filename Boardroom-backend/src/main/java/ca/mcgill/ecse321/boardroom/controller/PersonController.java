package ca.mcgill.ecse321.boardroom.controller;

import ca.mcgill.ecse321.boardroom.dtos.PersonCreationDto;
import ca.mcgill.ecse321.boardroom.dtos.PersonRequestDto;
import ca.mcgill.ecse321.boardroom.dtos.responses.PersonResponseDto;
import ca.mcgill.ecse321.boardroom.services.PersonService;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
    public PersonResponseDto createPerson(@Valid @RequestBody PersonCreationDto personToCreate) {
        return new PersonResponseDto(personService.createPerson(personToCreate));
    }

    /**
     * Update person type
     */
    @PutMapping("people/{id}/role")
    public PersonResponseDto toggleAccountType(@PathVariable("id") int id,@Valid @RequestBody PersonRequestDto partialUpdatedPerson) {
        return new PersonResponseDto(personService.updatePerson(id, partialUpdatedPerson));
    }

    @GetMapping("people/{id}") 
    public PersonResponseDto getPerson(@PathVariable("id") int id) {
        return new PersonResponseDto(personService.findPersonById(id));
    }

    @DeleteMapping("people/{id}")
    public void deletePerson(@PathVariable("id") int id) {
        personService.deletePerson(id);
    }
}
