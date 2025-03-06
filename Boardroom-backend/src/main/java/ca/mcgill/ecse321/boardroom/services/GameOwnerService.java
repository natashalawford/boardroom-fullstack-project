package ca.mcgill.ecse321.boardroom.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import ca.mcgill.ecse321.boardroom.dtos.SpecificBoardGameCreationDto;
import ca.mcgill.ecse321.boardroom.dtos.SpecificBoardGameUpdateDto;
import ca.mcgill.ecse321.boardroom.exceptions.BoardroomException;
import ca.mcgill.ecse321.boardroom.model.BoardGame;
import ca.mcgill.ecse321.boardroom.model.Person;
import ca.mcgill.ecse321.boardroom.model.SpecificBoardGame;
import ca.mcgill.ecse321.boardroom.repositories.SpecificBoardGameRepository;

@Service
public class GameOwnerService { 
   
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
    public SpecificBoardGame updateSpecificBoardGame(SpecificBoardGameUpdateDto specificBoardGameToUpdate) {

        //Make sure this specific board game exists
        SpecificBoardGame existingSpecificBoardGame = specificBoardGameRepo.findSpecificBoardGameById(specificBoardGameToUpdate.getId());

        if (null == existingSpecificBoardGame) {
            throw new BoardroomException(HttpStatus.NOT_FOUND, "This specific board game does not exist, cannot update it");
        }

        //IMPORTANT: assuming we cannot update owner or board game

        //Construct new specific board game with updating attributes
        SpecificBoardGame updatedSpecificBoardGame = new SpecificBoardGame(specificBoardGameToUpdate.getId(), specificBoardGameToUpdate.getDescription(), specificBoardGameToUpdate.getPicture(), specificBoardGameToUpdate.getStatus(), existingSpecificBoardGame.getBoardGame(), existingSpecificBoardGame.getOwner());

        return specificBoardGameRepo.save(updatedSpecificBoardGame);
    }
}
