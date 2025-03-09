package ca.mcgill.ecse321.boardroom.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import ca.mcgill.ecse321.boardroom.dtos.BoardGameCreationDto;
import ca.mcgill.ecse321.boardroom.dtos.SpecificBoardGameCreationDto;
import ca.mcgill.ecse321.boardroom.dtos.SpecificBoardGameUpdateDto;
import ca.mcgill.ecse321.boardroom.exceptions.BoardroomException;
import ca.mcgill.ecse321.boardroom.model.BoardGame;
import ca.mcgill.ecse321.boardroom.model.Person;
import ca.mcgill.ecse321.boardroom.model.SpecificBoardGame;
import ca.mcgill.ecse321.boardroom.repositories.BoardGameRepository;
import ca.mcgill.ecse321.boardroom.repositories.SpecificBoardGameRepository;
import jakarta.transaction.Transactional;

@Service
public class GameOwnerService { 

    @Autowired
    private PersonService personService;
    
    @Autowired
    private BoardGameService boardGameService;

    @Autowired
    private BoardGameRepository boardGameRepo;
   
    @Autowired
    private SpecificBoardGameRepository specificBoardGameRepo;

    @Transactional
    public BoardGame createBoardGame(BoardGameCreationDto boardGameToCreate) {
        BoardGame boardGame = new BoardGame(boardGameToCreate.getTitle(),
                boardGameToCreate.getDescription(),
                boardGameToCreate.getPlayersNeeded(),
                boardGameToCreate.getPicture());
        return boardGameRepo.save(boardGame);
    }

    @Transactional
    public SpecificBoardGame createSpecificBoardGame(SpecificBoardGameCreationDto specificBoardGameToCreate) {
        //Make sure owner exists
        Person personToFind = personService.findPersonById(specificBoardGameToCreate.getPersonId());        

        //make sure person is owner
        if (!personToFind.isOwner()) {
            throw new BoardroomException(HttpStatus.BAD_REQUEST, "This person is not a game owner");
        }

        //Make sure board game exists
        BoardGame boardGame = boardGameService.getBoardGameByTitle(specificBoardGameToCreate.getBoardGameTitle());

        //Create new specific board game
        SpecificBoardGame newSpecificBoardGame = new SpecificBoardGame(specificBoardGameToCreate.getPicture(), specificBoardGameToCreate.getDescription(), specificBoardGameToCreate.getGameStatus(), boardGame, personToFind);

        return specificBoardGameRepo.save(newSpecificBoardGame);
    }

    public void deleteSpecificBoardGame(int id) {
        //Get board game to delete
        SpecificBoardGame specificBoardGameToDelete = specificBoardGameRepo.findSpecificBoardGameById(id);

        //Make sure this board game exists
        if (specificBoardGameToDelete == null) {
            throw new BoardroomException(HttpStatus.NOT_FOUND, String.format("This specific board game with id %d does not exist, so it cannot be deleted.", id));
        }

        //Delete board game
        specificBoardGameRepo.delete(specificBoardGameToDelete); 
    }

    public SpecificBoardGame updateSpecificBoardGame(SpecificBoardGameUpdateDto specificBoardGameToUpdate) {
        //Make sure this specific board game exists
        SpecificBoardGame existingSpecificBoardGame = boardGameService.getSpecificBoardGameById(specificBoardGameToUpdate.getId());

        if (null == existingSpecificBoardGame) {
            throw new BoardroomException(HttpStatus.NOT_FOUND, "This specific board game does not exist, cannot update it");
        }

        //Construct new specific board game with updating attributes
        SpecificBoardGame updatedSpecificBoardGame = new SpecificBoardGame(specificBoardGameToUpdate.getId(), specificBoardGameToUpdate.getDescription(), specificBoardGameToUpdate.getPicture(), specificBoardGameToUpdate.getStatus(), existingSpecificBoardGame.getBoardGame(), existingSpecificBoardGame.getOwner());

        return specificBoardGameRepo.save(updatedSpecificBoardGame);
    }
}
