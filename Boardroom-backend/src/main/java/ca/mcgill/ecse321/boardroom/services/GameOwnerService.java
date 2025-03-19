package ca.mcgill.ecse321.boardroom.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import ca.mcgill.ecse321.boardroom.dtos.SpecificBoardGameRequestDto;
import ca.mcgill.ecse321.boardroom.dtos.creation.BoardGameCreationDto;
import ca.mcgill.ecse321.boardroom.dtos.creation.SpecificBoardGameCreationDto;
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
        //Make sure boardgame with this title doesn't already exist
        if (boardGameRepo.existsByTitle(boardGameToCreate.getTitle())) {
            throw new BoardroomException(HttpStatus.BAD_REQUEST, "A board game with this title already exists");
        }

        BoardGame boardGame = new BoardGame(boardGameToCreate.getTitle(),
                boardGameToCreate.getDescription(),
                boardGameToCreate.getPlayersNeeded(),
                boardGameToCreate.getPicture());
        
        return boardGameRepo.save(boardGame);
    }

    @Transactional
    public SpecificBoardGame createSpecificBoardGame(SpecificBoardGameCreationDto specificBoardGameToCreate) {
        //Make sure owner exists - personService will throw error
        Person personToFind = personService.findPersonById(specificBoardGameToCreate.getPersonId());        

        //Make sure Person is owner
        if (!personToFind.isOwner()) {
            throw new BoardroomException(HttpStatus.BAD_REQUEST, "This person is not a game owner");
        }

        //Make sure board game exists
        BoardGame boardGame = boardGameService.getBoardGameByTitle(specificBoardGameToCreate.getBoardGameTitle());

        //Create new specific board game
        SpecificBoardGame newSpecificBoardGame = new SpecificBoardGame(specificBoardGameToCreate.getPicture(), specificBoardGameToCreate.getDescription(), specificBoardGameToCreate.getGameStatus(), boardGame, personToFind);

        return specificBoardGameRepo.save(newSpecificBoardGame);
    }

    @Transactional
    public SpecificBoardGame updateSpecificBoardGame(int id, SpecificBoardGameRequestDto specificBoardGameToUpdate) {
        //Make sure this specific board game exists - boardgameservice will throw error
        SpecificBoardGame existingSpecificBoardGame = boardGameService.getSpecificBoardGameById(id);
 
        //Construct new specific board game with updating attributes
        SpecificBoardGame updatedSpecificBoardGame = new SpecificBoardGame(id, specificBoardGameToUpdate.getDescription(), specificBoardGameToUpdate.getPicture(), specificBoardGameToUpdate.getStatus(), existingSpecificBoardGame.getBoardGame(), existingSpecificBoardGame.getOwner());

        return specificBoardGameRepo.save(updatedSpecificBoardGame);
    }

    @Transactional
    public void deleteBoardGame(String title) {
        //Get board game to delete
        BoardGame boardGameToDelete = boardGameService.getBoardGameByTitle(title);
        //Delete board game
        boardGameRepo.delete(boardGameToDelete);
    }

    @Transactional
    public void deleteSpecificBoardGame(int id) {
        //Get board game to delete 
        SpecificBoardGame specificBoardGameToDelete = boardGameService.getSpecificBoardGameById(id);

        //Delete specific board game
        specificBoardGameRepo.delete(specificBoardGameToDelete); 
    }
}
