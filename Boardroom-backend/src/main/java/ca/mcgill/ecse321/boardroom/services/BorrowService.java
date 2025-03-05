package ca.mcgill.ecse321.boardroom.services;

import ca.mcgill.ecse321.boardroom.repositories.BorrowRequestRepository;
import ca.mcgill.ecse321.boardroom.repositories.PersonRepository;
import ca.mcgill.ecse321.boardroom.repositories.SpecificBoardGameRepository;
import ca.mcgill.ecse321.boardroom.model.BorrowRequest;
import ca.mcgill.ecse321.boardroom.model.SpecificBoardGame;
import ca.mcgill.ecse321.boardroom.model.enums.RequestStatus;
import ca.mcgill.ecse321.boardroom.model.Person;
import ca.mcgill.ecse321.boardroom.dtos.BorrowRequestDtoCreation;
import ca.mcgill.ecse321.boardroom.dtos.BorrowRequestDtoSpecific;
import ca.mcgill.ecse321.boardroom.exceptions.BoardroomException;

import java.time.Instant;
import java.time.LocalDateTime;
import jakarta.validation.Valid;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class BorrowService {
   
    @Autowired
    private BorrowRequestRepository borrowRequestRepo;
    
    @Autowired 
    private PersonRepository personRepo;

    @Autowired 
    private SpecificBoardGameRepository specificBoardGameRepo;



    @Transactional
    public BorrowRequest creatBorrowRequest(@Valid BorrowRequestDtoCreation borrowRequestToCreate){
        //Check if person and specific board game exist
        Person personToFind = personRepo.findById(borrowRequestToCreate.getPersonId()).orElseThrow(() -> new BoardroomException(HttpStatus.NOT_FOUND, "A person with this id does not exist"));
        SpecificBoardGame specificBoardGameToFind = specificBoardGameRepo.findById(borrowRequestToCreate.getSpecificBoardGameId()).orElseThrow(() -> new BoardroomException(HttpStatus.NOT_FOUND, "A specific board game with this id does not exist"));

        BorrowRequest borrowRequest = new BorrowRequest( borrowRequestToCreate.getStatus(), 
                                              borrowRequestToCreate.getRequestStartDate(), 
                                              borrowRequestToCreate.getRequestEndDate(), 
                                              personToFind, 
                                              specificBoardGameToFind);
        return borrowRequestRepo.save(borrowRequest);
    }    

    @Transactional
    public BorrowRequest updateBorrowRequestStatus(int id, RequestStatus status){
        BorrowRequest borrowRequest = borrowRequestRepo.findById(id).orElseThrow(() -> new BoardroomException(HttpStatus.NOT_FOUND, "A borrow request with this id does not exist"));
        borrowRequest.setStatus(status);
        return borrowRequestRepo.save(borrowRequest);
    }
}
