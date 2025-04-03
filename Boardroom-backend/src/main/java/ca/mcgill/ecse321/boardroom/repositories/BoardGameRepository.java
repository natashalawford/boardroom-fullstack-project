package ca.mcgill.ecse321.boardroom.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import ca.mcgill.ecse321.boardroom.model.BoardGame;

public interface BoardGameRepository extends CrudRepository<BoardGame, String> {
    public BoardGame findBoardGameByTitle(String title);

    public boolean existsByTitle(String title);

    boolean existsByTitleIgnoreCase(String title);
}
