package ca.mcgill.ecse321.boardroom.repositories;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.boardroom.model.Person;

import java.util.Optional;

public interface PersonRepository extends CrudRepository<Person, Integer> {
    public Person findPersonById(int id);
    Optional<Person> findByEmail(String email);  // used for login
}
