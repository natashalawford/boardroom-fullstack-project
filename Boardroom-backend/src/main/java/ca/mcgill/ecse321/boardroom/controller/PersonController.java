package ca.mcgill.ecse321.boardroom.controller;

import ca.mcgill.ecse321.boardroom.dtos.PersonLoginDto;
import ca.mcgill.ecse321.boardroom.dtos.PersonRequestDto;
import ca.mcgill.ecse321.boardroom.dtos.PersonUpdatePasswordDto;
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

  
    
    /** 
     * Creates a person entity and returns it
     * 
     * @param personToCreate
     * @return PersonResponseDto
     */
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public PersonResponseDto createPerson(@Valid @RequestBody PersonCreationDto personToCreate) {
        return new PersonResponseDto(personService.createPerson(personToCreate));
    }

    
    /** 
     * Finds a person with given id and returns it 
     * 
     * @param id
     * @return PersonResponseDto
     */
    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public PersonResponseDto getPerson(@PathVariable("id") int id) {
        return new PersonResponseDto(personService.findPersonById(id));
    }

    
    /** 
     * Updates all fields of person except password 
     * 
     * @param id
     * @param partialUpdatedPerson
     * @return PersonResponseDto
     */
    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public PersonResponseDto toggleAccountType(@PathVariable("id") int id, @Valid @RequestBody PersonRequestDto partialUpdatedPerson) {
        return new PersonResponseDto(personService.updatePerson(id, partialUpdatedPerson));
    } 

    
    /** 
     * Changes user's password
     * 
     * @param id
     * @param passwordDto
     */
    @PutMapping("{id}/password")
    @ResponseStatus(HttpStatus.OK)
    public void changePassword(@PathVariable("id") int id, @RequestBody PersonUpdatePasswordDto passwordDto) {
        personService.changePassword(id, passwordDto);
    }
    
    /** 
     * Logs a person into their "account" 
     * 
     * @param loginDto
     * @return PersonResponseDto
     */
    @PostMapping("login")
    @ResponseStatus(HttpStatus.OK)
    public PersonResponseDto loginPerson(@Valid @RequestBody PersonLoginDto loginDto) {
        // Directly use the incoming loginDto
        return personService.login(loginDto);
    }
    
    /** 
     * Delete a person entity
     * 
     * @param id
     */
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePersonById(@PathVariable("id") int id) {
        personService.deletePerson(id);
    }
}
