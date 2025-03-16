package ca.mcgill.ecse321.boardroom.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;

import ca.mcgill.ecse321.boardroom.dtos.BoardGameCreationDto;
import ca.mcgill.ecse321.boardroom.dtos.SpecificBoardGameCreationDto;
import ca.mcgill.ecse321.boardroom.dtos.SpecificBoardGameRequestDto;
import ca.mcgill.ecse321.boardroom.dtos.responses.BoardGameResponseDto;
import ca.mcgill.ecse321.boardroom.dtos.responses.SpecificBoardGameResponseDto;
import ca.mcgill.ecse321.boardroom.services.GameOwnerService;


@RestController
public class GameOwnerController {
    @Autowired
    GameOwnerService gameOwnerService;


    @PostMapping("boardgame")
    @ResponseStatus(HttpStatus.CREATED)
    public BoardGameResponseDto createBoardGame(@RequestBody BoardGameCreationDto boardGameToCreate) {
        return new BoardGameResponseDto(gameOwnerService.createBoardGame(boardGameToCreate));
    }


    @PostMapping("specificboardgame")
    @ResponseStatus(HttpStatus.CREATED)
    public SpecificBoardGameResponseDto createSpecificBoardGame(@RequestBody SpecificBoardGameCreationDto specificBoardGameToCreate) { 
        return new SpecificBoardGameResponseDto(gameOwnerService.createSpecificBoardGame(specificBoardGameToCreate));
    }

    @PutMapping("specificboardgame/{id}")
    public SpecificBoardGameResponseDto updateSpecificBoardGame(@PathVariable("id") int id, @RequestBody SpecificBoardGameRequestDto specificBoardGameToUpdate) {
        return new SpecificBoardGameResponseDto(gameOwnerService.updateSpecificBoardGame(id, specificBoardGameToUpdate));
    }
}
