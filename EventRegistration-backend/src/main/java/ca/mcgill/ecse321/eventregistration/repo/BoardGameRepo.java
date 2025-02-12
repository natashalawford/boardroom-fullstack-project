package main.java.ca.mcgill.ecse321.eventregistration.repo;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.eventregistration.model.BoardGame;


public interface BoardGameRepo extends CrudRepository<BoardGame, Integer> {
    
}
