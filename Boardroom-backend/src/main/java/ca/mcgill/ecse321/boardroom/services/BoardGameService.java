package ca.mcgill.ecse321.boardroom.services;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import ca.mcgill.ecse321.boardroom.exceptions.BoardroomException;
import ca.mcgill.ecse321.boardroom.model.*;
import ca.mcgill.ecse321.boardroom.repositories.*;

@Service
public class BoardGameService {

    @Autowired
    private BoardGameRepository boardGameRepo;

    @Autowired
    private SpecificBoardGameRepository specificBoardGameRepo;

    public BoardGame getBoardGameByTitle(String title) {
        BoardGame boardGameToFind = boardGameRepo.findBoardGameByTitle(title);
        if (boardGameToFind == null) {
            throw new BoardroomException(HttpStatus.NOT_FOUND, "A board game type with this title does not exist");
        }

        return boardGameToFind;
    }

    public SpecificBoardGame getSpecificBoardGameById(int id) {
        SpecificBoardGame specificBoardGameToFind = specificBoardGameRepo.findSpecificBoardGameById(id);
        if (specificBoardGameToFind == null) {
            throw new BoardroomException(HttpStatus.NOT_FOUND, "A specific board game with this id does not exist");
        }

        return specificBoardGameToFind;
    }

    public List<BoardGame> getAllBoardGames() {
        return toList(boardGameRepo.findAll());
    }

    public List<SpecificBoardGame> getAllSpecificBoardGames() {
        return toList(specificBoardGameRepo.findAll());
    }

    private <T> List<T> toList(Iterable<T> iterable) {
        List<T> resultList = new ArrayList<T>();
        for (T t : iterable) {
            resultList.add(t);
        }
        return resultList;
    }
}
