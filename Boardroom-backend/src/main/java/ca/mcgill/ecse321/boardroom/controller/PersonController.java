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
import org.springframework.web.bind.annotation.*;


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
    @CrossOrigin(origins = "http://localhost:5173")
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
    @CrossOrigin(origins = "http://localhost:5173")
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
    @CrossOrigin(origins = "http://localhost:5173")
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
    @CrossOrigin(origins = "http://localhost:5173")
    public PersonResponseDto changePassword(@PathVariable("id") int id, @RequestBody PersonUpdatePasswordDto passwordDto) {
        return new PersonResponseDto(personService.changePassword(id, passwordDto));
    }
    
    /** 
     * Logs a person into their "account" 
     * 
     * @param loginDto
     * @return PersonResponseDto
     */
    @PostMapping("login")
    @ResponseStatus(HttpStatus.OK)
    @CrossOrigin(origins = "http://localhost:5173")
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
    @CrossOrigin(origins = "http://localhost:5173")
    public void deletePersonById(@PathVariable("id") int id) {
        personService.deletePerson(id);
    }
}
