package ca.mcgill.ecse321.boardroom.services;

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
    public Person createPerson(String name, String email,
                               String password, boolean owner) {
        Person newPerson = new Person(name, email, password, owner);

        return personRepo.save(newPerson);
    }

    @Transactional
    public Person updatePerson(Person updatedPerson) {
        return personRepo.save(updatedPerson);
    }

}
