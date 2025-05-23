package ca.mcgill.ecse321.boardroom.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.util.MimeType.SpecificityComparator;

import ca.mcgill.ecse321.boardroom.dtos.SpecificBoardGameRequestDto;
import ca.mcgill.ecse321.boardroom.dtos.creation.BoardGameCreationDto;
import ca.mcgill.ecse321.boardroom.dtos.creation.SpecificBoardGameCreationDto;
import ca.mcgill.ecse321.boardroom.dtos.responses.BoardGameResponseDto;
import ca.mcgill.ecse321.boardroom.dtos.responses.BorrowRequestResponseAccountDto;
import ca.mcgill.ecse321.boardroom.dtos.responses.SpecificBoardGameResponseDto;
import ca.mcgill.ecse321.boardroom.model.BorrowRequest;
import ca.mcgill.ecse321.boardroom.model.SpecificBoardGame;
import ca.mcgill.ecse321.boardroom.services.GameOwnerService;
import jakarta.validation.Valid;


@RestController
public class GameOwnerController {

    @Autowired
    GameOwnerService gameOwnerService;

    
    /** 
     * @param boardGameToCreate
     * @return BoardGameResponseDto
     */
    @PostMapping("boardgame")
    @ResponseStatus(HttpStatus.CREATED)
    @CrossOrigin(origins = "http://localhost:5173")
    public BoardGameResponseDto createBoardGame(@Valid @RequestBody BoardGameCreationDto boardGameToCreate) {
        return new BoardGameResponseDto(gameOwnerService.createBoardGame(boardGameToCreate));
    }

    
    /** 
     * @param specificBoardGameToCreate
     * @return SpecificBoardGameResponseDto
     */
    @PostMapping("specificboardgame")
    @ResponseStatus(HttpStatus.CREATED)
    @CrossOrigin(origins = "http://localhost:5173")
    public SpecificBoardGameResponseDto createSpecificBoardGame(@Valid @RequestBody SpecificBoardGameCreationDto specificBoardGameToCreate) { 
        return new SpecificBoardGameResponseDto(gameOwnerService.createSpecificBoardGame(specificBoardGameToCreate));
    }

    
    /** 
     * @param id
     * @param specificBoardGameToUpdate
     * @return SpecificBoardGameResponseDto
     */
    @PutMapping("specificboardgame/{id}")
    @ResponseStatus(HttpStatus.OK)
    @CrossOrigin(origins = "http://localhost:5173")
    public SpecificBoardGameResponseDto updateSpecificBoardGame(@PathVariable("id") int id, @Valid @RequestBody SpecificBoardGameRequestDto specificBoardGameToUpdate) {
        return new SpecificBoardGameResponseDto(gameOwnerService.updateSpecificBoardGame(id, specificBoardGameToUpdate));
    }

    
    /** 
     * @param id
     */
    @DeleteMapping("specificboardgame/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CrossOrigin(origins = "http://localhost:5173")
    public void deleteSpecificBoardGame(@PathVariable("id") int id) {
        gameOwnerService.deleteSpecificBoardGame(id);
    }


    //added for account details page
    @GetMapping("specificboardgame/owner/{id}")
    @ResponseStatus(HttpStatus.OK)
    @CrossOrigin(origins = "http://localhost:5173")
    public List<SpecificBoardGameResponseDto> getSpecificBoardGamesByOwner(@PathVariable("id") int id){
        List<SpecificBoardGame> ownedGames = gameOwnerService.getSpecificBoardGamesByOwner(id);
        List<SpecificBoardGameResponseDto> ownedGamesDtos = new ArrayList<>();
        for (SpecificBoardGame specificBoardGame : ownedGames) {
            ownedGamesDtos.add(new SpecificBoardGameResponseDto(specificBoardGame));
        }
        return ownedGamesDtos;
    }

}
