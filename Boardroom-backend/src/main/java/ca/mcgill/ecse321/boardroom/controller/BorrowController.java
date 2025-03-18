package ca.mcgill.ecse321.boardroom.controller;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ca.mcgill.ecse321.boardroom.dtos.BorrowRequestDtoCreation;
import ca.mcgill.ecse321.boardroom.dtos.responses.BorrowRequestResponseDto;
import ca.mcgill.ecse321.boardroom.model.BorrowRequest;
import ca.mcgill.ecse321.boardroom.model.enums.RequestStatus;
import ca.mcgill.ecse321.boardroom.services.BorrowService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/borrowRequests")
public class BorrowController {
    @Autowired
    private BorrowService borrowService;

    
    /** 
     * Create a new borrow request.
     * 
     * @param borrowRequestToCreate
     * @return BorrowRequestResponseDto
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BorrowRequestResponseDto createBorrowRequest(@Valid @RequestBody BorrowRequestDtoCreation borrowRequestToCreate){
        BorrowRequest createdBorrowRequest = borrowService.createBorrowRequest(borrowRequestToCreate);
        return new BorrowRequestResponseDto(createdBorrowRequest);
    }

    
    /** 
     * Update status of a borrow request.
     * 
     * @param id
     * @param status
     * @return BorrowRequestResponseDto
     */
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BorrowRequestResponseDto updateBorrowRequest(@PathVariable int id, @RequestBody RequestStatus status){
        BorrowRequest updatedBorrowRequest = borrowService.updateBorrowRequestStatus(id, status);
        return new BorrowRequestResponseDto(updatedBorrowRequest);
    }

    
    /** 
     * View pending borrow requests.
     * 
     * @return List<BorrowRequestResponseDto>
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<BorrowRequestResponseDto> viewPendingBorrowRequests(){
        List<BorrowRequestResponseDto> borrowRequestDtos = new ArrayList<>();
        List<BorrowRequest> borrowRequests = borrowService.viewPendingBorrowRequests();
        for(BorrowRequest borrowRequest : borrowRequests){
            borrowRequestDtos.add(new BorrowRequestResponseDto(borrowRequest));
        }
        return borrowRequestDtos;
    }

    
    /** 
     * Get borrow request by ID.
     * 
     * @param id
     * @return BorrowRequestResponseDto
     */
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BorrowRequestResponseDto getBorrowRequestById(@PathVariable int id) {
        BorrowRequest br = borrowService.getBorrowRequestById(id);
        return new BorrowRequestResponseDto(br);
    }

    
    /** 
     * Delete borrow request by ID.
     * 
     * @param id
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBorrowRequest(@PathVariable int id) {
        borrowService.deleteBorrowRequestById(id);
    }


    
    /** 
     * View history of borrow requests for a specific board game.
     * 
     * @param boardGameId
     * @return List<BorrowRequestResponseDto>
     */
    @GetMapping("/history/{specificBoardGameId}")
    @ResponseStatus(HttpStatus.OK)
    public List<BorrowRequestResponseDto> viewLendingHistoryByBoardGame(@PathVariable int specificBoardGameId) {
        List<BorrowRequest> borrowRequests = borrowService.viewBorrowRequestsByBoardgame(specificBoardGameId);
        List<BorrowRequestResponseDto> borrowRequestDtos = new ArrayList<>();
        for (BorrowRequest borrowRequest : borrowRequests) {
            borrowRequestDtos.add(new BorrowRequestResponseDto(borrowRequest));
        }
        return borrowRequestDtos;
    }
}