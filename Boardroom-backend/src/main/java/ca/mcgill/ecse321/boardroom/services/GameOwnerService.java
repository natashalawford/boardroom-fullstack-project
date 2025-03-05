package ca.mcgill.ecse321.boardroom.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import ca.mcgill.ecse321.boardroom.dtos.SpecificBoardGameCreationDto;
import ca.mcgill.ecse321.boardroom.exceptions.BoardroomException;
import ca.mcgill.ecse321.boardroom.model.BoardGame;
import ca.mcgill.ecse321.boardroom.model.Person;
import ca.mcgill.ecse321.boardroom.model.SpecificBoardGame;
import ca.mcgill.ecse321.boardroom.repositories.SpecificBoardGameRepository;

@Service
public class GameOwnerService {

    /*
     * a specific board game must belong to an owner (so owner must exist) and be linked to an existing board game (so board game must exist), maybe this belongs in GameOwnerService,
     * 
     * The controller will take in:
     * for create: id of person and id of board game, then in request body all the extra info
     * service for create should just take in the info (if dto has ids for board game and owner, then find those first), then create and return the object
     * service for delete 
     */
   
    @Autowired
    private SpecificBoardGameRepository specificBoardGameRepo;

    @Autowired
    private PersonService personService;

    @Autowired
    private BoardGameService boardGameService;

    public SpecificBoardGame findSpecificBoardGameById(int id)  {
        SpecificBoardGame specificBoardGame = specificBoardGameRepo.findSpecificBoardGameById(id);        
        
        if (specificBoardGame == null) {
            throw new BoardroomException(HttpStatus.NOT_FOUND, String.format("No specific board game has id %s", id));
        }

        return specificBoardGame;
    }


    //Add a board game to owner's collection
    public SpecificBoardGame createSpecificBoardGame(SpecificBoardGameCreationDto specificBoardGameToCreate) {
        //Make sure owner exists
        Person owner = personService.findPersonById(specificBoardGameToCreate.getOwnerId());        

        //Make sure board game exists
        BoardGame boardGame = boardGameService.findBoardGameByTitle(specificBoardGameToCreate.getBoardGameTitle());

        //Create new specific board game
        SpecificBoardGame newSpecificBoardGame = new SpecificBoardGame(specificBoardGameToCreate.getPicture(), specificBoardGameToCreate.getDescription(), specificBoardGameToCreate.getStatus(), boardGame, owner);

        return specificBoardGameRepo.save(newSpecificBoardGame);
    }

    //Remove board game from owner's collection
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



    //update information about a board game in owner's collection
    // public SpecificBoardGame updateSpecificBoardGame(SpecificBoardGameDto specificBoardGameToUpdate) {

    // }
}
