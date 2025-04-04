package ca.mcgill.ecse321.boardroom.controller;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import ca.mcgill.ecse321.boardroom.dtos.creation.BorrowRequestDtoCreation;
import ca.mcgill.ecse321.boardroom.dtos.responses.BorrowRequestResponseAccountDto;
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
    @CrossOrigin(origins = "http://localhost:5173")
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
    @CrossOrigin(origins = "http://localhost:5173")
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
    @CrossOrigin(origins = "http://localhost:5173")
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
    @CrossOrigin(origins = "http://localhost:5173")
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
    @CrossOrigin(origins = "http://localhost:5173")
    public void deleteBorrowRequest(@PathVariable int id) {
        borrowService.deleteBorrowRequestById(id);
    }


    
    /** 
     * View history of borrow requests for a specific board game. //not that useful as of now, use method below
     * 
     * @param boardGameId
     * @return List<BorrowRequestResponseDto>
     */
    @GetMapping("/history/{specificBoardGameId}")
    @ResponseStatus(HttpStatus.OK)
    @CrossOrigin(origins = "http://localhost:5173")
    public List<BorrowRequestResponseDto> viewLendingHistoryByBoardGame(@PathVariable int specificBoardGameId) {
        List<BorrowRequest> borrowRequests = borrowService.viewBorrowRequestsByBoardgame(specificBoardGameId);
        List<BorrowRequestResponseDto> borrowRequestDtos = new ArrayList<>();
        for (BorrowRequest borrowRequest : borrowRequests) {
            borrowRequestDtos.add(new BorrowRequestResponseDto(borrowRequest));
        }
        return borrowRequestDtos;
    }

     
    @PostMapping("/pending/{personId}")    
    @ResponseStatus(HttpStatus.OK)
    @CrossOrigin(origins = "http://localhost:5173")
    public List<BorrowRequestResponseAccountDto> viewBorrowRequestsByPersonAndStatus(@PathVariable int personId, @RequestBody RequestStatus status) {
        List<BorrowRequest> borrowRequests = borrowService.viewBorrowRequestsByPersonAndStatus(personId, status);
        List<BorrowRequestResponseAccountDto> borrowRequestDtos = new ArrayList<>();
        for (BorrowRequest borrowRequest : borrowRequests) {
            borrowRequestDtos.add(new BorrowRequestResponseAccountDto(borrowRequest));
        }
        return borrowRequestDtos;
    }
    
}