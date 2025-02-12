package ca.mcgill.ecse321.eventregistration.repositories;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.eventregistration.model.SpecificBoardGame;

public interface SpecificBoardGameRepository extends CrudRepository<SpecificBoardGame, Integer> {
    public SpecificBoardGame findSpecificBoardGameById(int id);
}
