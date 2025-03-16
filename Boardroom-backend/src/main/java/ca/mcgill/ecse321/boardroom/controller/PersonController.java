package ca.mcgill.ecse321.boardroom.controller;

import ca.mcgill.ecse321.boardroom.dtos.PersonRequestDto;
import ca.mcgill.ecse321.boardroom.dtos.PersonUpdateDto;
import ca.mcgill.ecse321.boardroom.dtos.responses.PersonResponseDto;
import ca.mcgill.ecse321.boardroom.exceptions.BoardroomException;
import ca.mcgill.ecse321.boardroom.model.Person;
import ca.mcgill.ecse321.boardroom.services.PersonService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

 
@RestController
public class PersonController {
    @Autowired
    private PersonService personService;
 

    /**
     * Update person type
     */
    @PutMapping("person/{id}/role")
    public PersonResponseDto toggleAccountType(@PathVariable int id,
                                              @RequestBody PersonRequestDto partialUpdatedPerson) {
        //Get rest of information of person from database
        Person personToUpdate =
                personService.findPersonById(id);

        if (personToUpdate == null) {
                throw new BoardroomException(HttpStatus.NOT_FOUND, "A person with this id does not exist");
        }

        //Build updated person
        PersonUpdateDto updatedPerson = new PersonUpdateDto(id, partialUpdatedPerson.getName(), partialUpdatedPerson.getEmail(), personToUpdate.getPassword(), partialUpdatedPerson.isOwner()); 

        //Update person
        Person persistedUpdatedPerson =
                personService.updatePerson(updatedPerson);

        return new PersonResponseDto(persistedUpdatedPerson);

    }
}
