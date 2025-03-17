package ca.mcgill.ecse321.boardroom.services;

import ca.mcgill.ecse321.boardroom.dtos.PersonCreationDto;
import ca.mcgill.ecse321.boardroom.dtos.PersonLoginDto;
import ca.mcgill.ecse321.boardroom.dtos.PersonRequestDto;
import ca.mcgill.ecse321.boardroom.dtos.responses.PersonResponseDto;
import ca.mcgill.ecse321.boardroom.exceptions.BoardroomException;
import ca.mcgill.ecse321.boardroom.model.Person;
import ca.mcgill.ecse321.boardroom.repositories.PersonRepository;
import jakarta.transaction.Transactional;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class PersonService {

    @Autowired
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
    public Person updatePerson(int id, PersonRequestDto personToUpdateDto) {

        // First check if this person exists, if not throw error
        Person personToUpdate = personRepo.findPersonById(id);

        if (null == personToUpdate) {
            throw new BoardroomException(HttpStatus.NOT_FOUND, "A person with " +
                    "this id does not exist");
        }

        Person updatedPerson = new Person(id,
                personToUpdateDto.getName(), personToUpdateDto.getEmail(),
                personToUpdate.getPassword(), personToUpdateDto.isOwner());

        return personRepo.save(updatedPerson);
    }

    @Transactional
    public PersonResponseDto login(PersonLoginDto loginDto) {
        if (loginDto.getEmail() == null || loginDto.getPassword() == null) {
            throw new BoardroomException(HttpStatus.BAD_REQUEST, "Email and password are required.");
        }

        // check for valid email, then password and throw error if invalid
        Optional<Person> optionalPerson = personRepo.findByEmail(loginDto.getEmail());
        if (optionalPerson.isEmpty()) {
            throw new BoardroomException(HttpStatus.UNAUTHORIZED, "Invalid email or password.");
        }

        Person person = optionalPerson.get();
        if (!person.getPassword().equals(loginDto.getPassword())) {
            throw new BoardroomException(HttpStatus.UNAUTHORIZED, "Invalid email or password.");
        }

        return new PersonResponseDto(person);
    }

}
