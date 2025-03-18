package ca.mcgill.ecse321.boardroom.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ca.mcgill.ecse321.boardroom.dtos.responses.*;
import ca.mcgill.ecse321.boardroom.exceptions.BoardroomException;
import ca.mcgill.ecse321.boardroom.model.BoardGame;
import ca.mcgill.ecse321.boardroom.model.SpecificBoardGame;
import ca.mcgill.ecse321.boardroom.services.BoardGameService;

@RestController
@RequestMapping("/boardgames") // Base path for board game endpoints
public class BoardGameController {

	@Autowired
	private BoardGameService boardGameService;

	/**
	 * Gets the list of existing board games in the database
	 * 
	 * @return List<BoardGameResponseDto> List of all board games in the system
	 */
	@GetMapping(value = {"/general"})
	public List<BoardGameResponseDto> getAllBoardGames() {
		List<BoardGameResponseDto> boardGameDtos = new ArrayList<>();
		for (BoardGame boardGame : boardGameService.getAllBoardGames()) {
			boardGameDtos.add(new BoardGameResponseDto(boardGame));
		}
		return boardGameDtos;
	}

	/**
	 * Gets the list of existing specific board games in the database
	 * 
	 * @return List<SpecificBoardGameResponseDto> List of all specific board games
	 *         in the system
	 */
	@GetMapping(value = {"/specific"})
	public List<SpecificBoardGameResponseDto> getAllSpecificBoardGames() {
		List<SpecificBoardGameResponseDto> specificBoardGameDtos = new ArrayList<>();
		for (SpecificBoardGame boardGame : boardGameService.getAllSpecificBoardGames()) {
			specificBoardGameDtos.add(new SpecificBoardGameResponseDto(boardGame));
		}
		return specificBoardGameDtos;
	}

	/**
	 * Gets an existing board game by its title
	 * 
	 * @return BoardGame The board game with the given title
	 */
	@GetMapping(value = {"/general/{title}"})
	public ResponseEntity<BoardGameResponseDto> findBoardGameByTitle(@PathVariable String title) {
		try {
			BoardGame boardGame = boardGameService.getBoardGameByTitle(title);
			return ResponseEntity.ok(new BoardGameResponseDto(boardGame));
		} catch (BoardroomException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
	}

	/**
	 * Gets an existing specific board game by its ID
	 * 
	 * @return SpecificBoardGame The specific board game with the given ID
	 */
	@GetMapping(value = {"/specific/{id}"})
	public ResponseEntity<SpecificBoardGameResponseDto> findSpecificBoardGameById(@PathVariable int id) {
		try {
			SpecificBoardGame specificBoardGame = boardGameService.getSpecificBoardGameById(id);
			return ResponseEntity.ok(new SpecificBoardGameResponseDto(specificBoardGame));
		} catch (BoardroomException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
	}
}
