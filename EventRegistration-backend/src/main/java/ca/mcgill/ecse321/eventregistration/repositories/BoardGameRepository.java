package ca.mcgill.ecse321.eventregistration.repositories;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.eventregistration.model.BoardGame;

public interface BoardGameRepository extends CrudRepository<BoardGame, String> {
    public BoardGame findBoardGameByTitle(String title);
}
