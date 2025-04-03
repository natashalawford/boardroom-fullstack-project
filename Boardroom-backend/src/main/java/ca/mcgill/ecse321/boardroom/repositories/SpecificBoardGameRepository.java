package ca.mcgill.ecse321.boardroom.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.boardroom.model.Person;
import ca.mcgill.ecse321.boardroom.model.SpecificBoardGame;

public interface SpecificBoardGameRepository extends CrudRepository<SpecificBoardGame, Integer> {
    public SpecificBoardGame findSpecificBoardGameById(int id);
    public List<SpecificBoardGame> findSpecificBoardGameByOwner(Person owner);
}
