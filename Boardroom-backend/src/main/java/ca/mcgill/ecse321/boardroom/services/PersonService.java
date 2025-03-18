package ca.mcgill.ecse321.boardroom.services;

import ca.mcgill.ecse321.boardroom.dtos.PersonLoginDto;
import ca.mcgill.ecse321.boardroom.dtos.PersonRequestDto;
import ca.mcgill.ecse321.boardroom.dtos.creation.PersonCreationDto;
import ca.mcgill.ecse321.boardroom.dtos.responses.PersonResponseDto;
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
    private PersonRepository personRepo;

    public Person findPersonById(int id) {
        Person person = personRepo.findPersonById(id);
        if (null == person) {
            throw new BoardroomException(HttpStatus.NOT_FOUND, String.format(
                    "No person has id %d", id));
        }
        return person;
    }

   @Transactional
    public Person createPerson(PersonCreationDto personToCreate) {
        //Make sure the email is not in use  
        if (personRepo.existsByEmail(personToCreate.getEmail())) {
            throw new BoardroomException(HttpStatus.BAD_REQUEST, "This email is already in use");
        }

        Person newPerson = new Person(personToCreate.getName(),
                personToCreate.getEmail(),
                personToCreate.getPassword(),
                personToCreate.isOwner());

        return personRepo.save(newPerson);
    }

    @Transactional
    public Person updatePerson(int id, PersonRequestDto personToUpdateDto) {
        // First check if this person exists, if not throw error
        Person personToUpdate = this.findPersonById(id);

        Person updatedPerson = new Person(id,
                personToUpdateDto.getName(), personToUpdateDto.getEmail(),
                personToUpdate.getPassword(), personToUpdateDto.isOwner());

        return personRepo.save(updatedPerson);
    } 

    public PersonResponseDto login(PersonLoginDto loginDto) { 
        // Find person with email
        Person existingPerson = personRepo.findByEmail(loginDto.getEmail());

        if (null == existingPerson) {
            throw new BoardroomException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
        }

        // Verify password
        if (!existingPerson.getPassword().equals(loginDto.getPassword())) {
            throw new BoardroomException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
        }

        return new PersonResponseDto(existingPerson);
    }

    @Transactional
    public void deletePerson(int id) {
        //Get person to delete
        Person personToDelete = this.findPersonById(id); 

        //Delete person
        personRepo.delete(personToDelete);
    } 
}
