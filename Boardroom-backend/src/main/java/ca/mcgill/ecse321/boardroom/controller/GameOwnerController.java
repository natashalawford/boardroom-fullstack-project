package ca.mcgill.ecse321.boardroom.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;

import ca.mcgill.ecse321.boardroom.dtos.SpecificBoardGameRequestDto;
import ca.mcgill.ecse321.boardroom.dtos.creation.BoardGameCreationDto;
import ca.mcgill.ecse321.boardroom.dtos.creation.SpecificBoardGameCreationDto;
import ca.mcgill.ecse321.boardroom.dtos.responses.BoardGameResponseDto;
import ca.mcgill.ecse321.boardroom.dtos.responses.SpecificBoardGameResponseDto;
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
    public BoardGameResponseDto createBoardGame(@Valid @RequestBody BoardGameCreationDto boardGameToCreate) {
        return new BoardGameResponseDto(gameOwnerService.createBoardGame(boardGameToCreate));
    }

    
    /** 
     * @param specificBoardGameToCreate
     * @return SpecificBoardGameResponseDto
     */
    @PostMapping("specificboardgame")
    @ResponseStatus(HttpStatus.CREATED)
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
    public SpecificBoardGameResponseDto updateSpecificBoardGame(@PathVariable("id") int id, @Valid @RequestBody SpecificBoardGameRequestDto specificBoardGameToUpdate) {
        return new SpecificBoardGameResponseDto(gameOwnerService.updateSpecificBoardGame(id, specificBoardGameToUpdate));
    }

    
    /** 
     * @param id
     */
    @DeleteMapping("specificboardgame/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSpecificBoardGame(@PathVariable("id") int id) {
        gameOwnerService.deleteSpecificBoardGame(id);
    }
}
