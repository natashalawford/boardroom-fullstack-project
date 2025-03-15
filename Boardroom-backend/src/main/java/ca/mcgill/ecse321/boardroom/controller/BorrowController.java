package ca.mcgill.ecse321.boardroom.controller;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ca.mcgill.ecse321.boardroom.dtos.BorrowRequestDtoCreation;
import ca.mcgill.ecse321.boardroom.dtos.BorrowRequestDtoSpecific;
import ca.mcgill.ecse321.boardroom.model.BorrowRequest;
import ca.mcgill.ecse321.boardroom.model.enums.RequestStatus;
import ca.mcgill.ecse321.boardroom.services.BorrowService;

@RestController
@RequestMapping("/borrowRequests")
public class BorrowController {
    @Autowired
    private BorrowService borrowService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BorrowRequestDtoSpecific createBorrowRequest(@RequestBody BorrowRequestDtoCreation borrowRequestToCreate){
        BorrowRequest createdBorrowRequest = borrowService.createBorrowRequest(borrowRequestToCreate);
        return new BorrowRequestDtoSpecific(createdBorrowRequest);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BorrowRequestDtoSpecific updateBorrowRequest(@PathVariable int id, @RequestBody RequestStatus status){
        BorrowRequest updatedBorrowRequest = borrowService.updateBorrowRequestStatus(id, status);
        return new BorrowRequestDtoSpecific(updatedBorrowRequest);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<BorrowRequestDtoSpecific> viewPendingBorrowRequests(){
        List<BorrowRequestDtoSpecific> borrowRequestDtos = new ArrayList<>();
        List<BorrowRequest> borrowRequests = borrowService.viewPendingBorrowRequests();
        for(BorrowRequest borrowRequest : borrowRequests){
            borrowRequestDtos.add(new BorrowRequestDtoSpecific(borrowRequest));
        }
        return borrowRequestDtos;
    }

}
