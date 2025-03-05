package ca.mcgill.ecse321.boardroom.services;

import ca.mcgill.ecse321.boardroom.dtos.PersonCreationDto;
import ca.mcgill.ecse321.boardroom.exceptions.BoardroomException;
import ca.mcgill.ecse321.boardroom.model.Person;
import ca.mcgill.ecse321.boardroom.repositories.PersonRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class PersonService {

    @Autowired
    @SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
    private PersonRepository personRepo;


    public Person findPersonById(int id) {
        Person person = personRepo.findPersonById(id);
        if (person == null) {
            throw new BoardroomException(HttpStatus.NOT_FOUND, String.format(
                    "No person has id %d", id));
        }
        return person;
    }

    @Transactional
    public Person createPerson(PersonCreationDto personToCreate) {
        Person newPerson = new Person(personToCreate.getName(),
                personToCreate.getEmail(),
                personToCreate.getPassword(),
                personToCreate.isOwner());

        return personRepo.save(newPerson);
    }

    @Transactional
    public Person updatePerson(int id, String name, String email,
                               String password, boolean owner) {
        //First check if this person exists, if not throw error
        if (!personRepo.existsById(id)) {
           throw new BoardroomException(HttpStatus.NOT_FOUND, "a person with " +
                   "this id does not exist");
        }

        Person updatedPerson = new Person(id, name, email, password, owner);

        return personRepo.save(updatedPerson);
    }

}
