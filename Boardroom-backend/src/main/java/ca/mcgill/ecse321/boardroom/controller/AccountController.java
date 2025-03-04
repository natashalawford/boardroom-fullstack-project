package ca.mcgill.ecse321.boardroom.controller;

import ca.mcgill.ecse321.boardroom.dtos.PersonRequestDto;
import ca.mcgill.ecse321.boardroom.dtos.PersonResponseDto;
import ca.mcgill.ecse321.boardroom.model.Person;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController {

    /**
     * Update person type
     */
    @PutMapping("person/{id}/role")
    public PersonResponseDto toggleAccountType(@PathVariable int id,
                                              @RequestBody PersonRequestDto partialUpdatedPerson) {
        //Get rest of information of person from database
        Person personToUpdate =
                personService.findPersonById(id);

        //Update person
        Person updatedPerson =
                personService.updatePerson(id, partialUpdatedPerson.getName()
                        , partialUpdatedPerson.getEmail(),
                        personToUpdate.getPassword(),
                        partialUpdatedPerson.isOwner());

        return new PersonResponseDto(updatedPerson);

    }
}
