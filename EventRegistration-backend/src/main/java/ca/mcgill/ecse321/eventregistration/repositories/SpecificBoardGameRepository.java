package main.java.ca.mcgill.ecse321.eventregistration.repo;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.eventregistration.model.SpecificBoardGame;


public interface SpecificBoardGameRepository extends CrudRepository<SpecificBoardGame, Integer> {
    public Person findSpecificBoardGameById(int id);
}
