package ca.mcgill.ecse321.boardroom.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import ca.mcgill.ecse321.boardroom.exceptions.BoardroomException;
import ca.mcgill.ecse321.boardroom.model.BoardGame;
import ca.mcgill.ecse321.boardroom.repositories.BoardGameRepository;

@Service
public class BoardGameService {

    @Autowired
    private BoardGameRepository boardGameRepo;
    
    public BoardGame findBoardGameByTitle(String title) {
        BoardGame boardGame = boardGameRepo.findBoardGameByTitle(title);

        if (boardGame == null) {
            throw new BoardroomException(HttpStatus.NOT_FOUND, String.format("Board game with title %s does not exist", title));
        }

        return boardGame;
    }
}
