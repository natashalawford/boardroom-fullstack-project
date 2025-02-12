package main.java.ca.mcgill.ecse321.eventregistration.repo;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.eventregistration.model.GameOwner;


public interface GameOwnerRepository extends CrudRepository<GameOwner, Integer> {
    public Person findGameOwnerByPerson(Person person); //could be int for person Id
}
