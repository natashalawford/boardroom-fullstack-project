package ca.mcgill.ecse321.boardroom.repositories;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.boardroom.model.Person;

public interface PersonRepository extends CrudRepository<Person, Integer> {
    public Person findPersonById(int id);

    public Person findByEmail(String email); 

    public boolean existsByEmail(String email);
}
