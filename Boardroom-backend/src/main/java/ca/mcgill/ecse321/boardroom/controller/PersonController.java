package ca.mcgill.ecse321.boardroom.controller;

import ca.mcgill.ecse321.boardroom.dtos.PersonLoginDto;
import ca.mcgill.ecse321.boardroom.dtos.PersonRequestDto;
import ca.mcgill.ecse321.boardroom.dtos.creation.PersonCreationDto;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

 
@RestController
@RequestMapping("people")
public class PersonController {
    @Autowired
    private PersonService personService;

  
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public PersonResponseDto createPerson(@Valid @RequestBody PersonCreationDto personToCreate) {
        return new PersonResponseDto(personService.createPerson(personToCreate));
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public PersonResponseDto getPerson(@PathVariable("id") int id) {
        return new PersonResponseDto(personService.findPersonById(id));
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public PersonResponseDto toggleAccountType(@PathVariable("id") int id, @Valid @RequestBody PersonRequestDto partialUpdatedPerson) {
        return new PersonResponseDto(personService.updatePerson(id, partialUpdatedPerson));
    } 

    @PostMapping("login")
    @ResponseStatus(HttpStatus.OK)
    public PersonResponseDto loginPerson(@Valid @RequestBody PersonLoginDto loginDto) {
        // Directly use the incoming loginDto
        return personService.login(loginDto);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePersonById(@PathVariable("id") int id) {
        personService.deletePerson(id);
    }
}
