package ca.mcgill.ecse321.boardroom.services;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.boardroom.dtos.*;
import ca.mcgill.ecse321.boardroom.exceptions.BoardroomException;
import ca.mcgill.ecse321.boardroom.model.*;
import ca.mcgill.ecse321.boardroom.model.enums.GameStatus;
import ca.mcgill.ecse321.boardroom.repositories.*;

import jakarta.validation.Valid;

@Service
public class BoardGameService {
    @Autowired
    private PersonRepository personRepo;

    @Autowired
    private BoardGameRepository boardGameRepo;

    @Autowired
    private SpecificBoardGameRepository specificBoardGameRepo;

    @Transactional
    public BoardGame createBoardGame(@Valid BoardGameCreationDto boardGameToCreate) {
        BoardGame boardGame = new BoardGame(boardGameToCreate.getTitle(),
                boardGameToCreate.getDescription(),
                boardGameToCreate.getPlayersNeeded(),
                boardGameToCreate.getPicture());
        return boardGameRepo.save(boardGame);
    }

    @Transactional
    public SpecificBoardGame createSpecificBoardGame(
            @Valid SpecificBoardGameCreationDto specificBoardGameToCreate) {
        // Check if person and specific board game exist
        Person personToFind = personRepo.findById(specificBoardGameToCreate.getPersonId())
                .orElseThrow(() -> new BoardroomException(HttpStatus.NOT_FOUND,
                        "A person with this id does not exist"));
        if (!personToFind.isOwner()) {
            throw new BoardroomException(HttpStatus.BAD_REQUEST, "This person is not a game owner");
        }
        BoardGame boardGameToFind = getBoardGameByTitle(specificBoardGameToCreate.getBoardGameTitle());

        // Convert game status from dto (string) to enum
        GameStatus status = specificBoardGameToCreate.getGameStatus();

        SpecificBoardGame specificBoardGame = new SpecificBoardGame(specificBoardGameToCreate.getPicture(),
                specificBoardGameToCreate.getDescription(),
                status,
                boardGameToFind,
                personToFind);
        return specificBoardGameRepo.save(specificBoardGame);
    }

    @Transactional
    public List<BoardGame> getAllBoardGames() {
        return toList(boardGameRepo.findAll());
    }

    @Transactional
    public BoardGame getBoardGameByTitle(String title) {
        BoardGame boardGameToFind = boardGameRepo.findBoardGameByTitle(title);
        if (boardGameToFind == null) {
            throw new BoardroomException(HttpStatus.NOT_FOUND, "A board game type with this title does not exist");
        }

        return boardGameToFind;
    }

    @Transactional
    public List<SpecificBoardGame> getAllSpecificBoardGames() {
        return toList(specificBoardGameRepo.findAll());
    }

    @Transactional
    public SpecificBoardGame getSpecificBoardGameById(int id) {
        SpecificBoardGame specificBoardGameToFind = specificBoardGameRepo.findSpecificBoardGameById(id);
        if (specificBoardGameToFind == null) {
            throw new BoardroomException(HttpStatus.NOT_FOUND, "A specific board game with this id does not exist");
        }

        return specificBoardGameToFind;
    }

    private <T> List<T> toList(Iterable<T> iterable) {
        List<T> resultList = new ArrayList<T>();
        for (T t : iterable) {
            resultList.add(t);
        }
        return resultList;
    }
}
