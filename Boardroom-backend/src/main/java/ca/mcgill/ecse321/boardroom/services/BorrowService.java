package ca.mcgill.ecse321.boardroom.services;

import ca.mcgill.ecse321.boardroom.repositories.BorrowRequestRepository;
import ca.mcgill.ecse321.boardroom.repositories.PersonRepository;
import ca.mcgill.ecse321.boardroom.repositories.SpecificBoardGameRepository;
import ca.mcgill.ecse321.boardroom.model.BorrowRequest;
import ca.mcgill.ecse321.boardroom.model.SpecificBoardGame;
import ca.mcgill.ecse321.boardroom.model.enums.RequestStatus;
import ca.mcgill.ecse321.boardroom.model.Person;
import ca.mcgill.ecse321.boardroom.dtos.creation.BorrowRequestDtoCreation;
import ca.mcgill.ecse321.boardroom.exceptions.BoardroomException;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BorrowService {

    @Autowired
    private BorrowRequestRepository borrowRequestRepo;

    @Autowired
    private PersonRepository personRepo;

    @Autowired
    private SpecificBoardGameRepository specificBoardGameRepo;

    @Transactional
    public BorrowRequest createBorrowRequest(BorrowRequestDtoCreation borrowRequestToCreate) {
        validateRequestTimes(borrowRequestToCreate.getRequestStartDate(), borrowRequestToCreate.getRequestEndDate());

        Person personToFind = personRepo.findById(borrowRequestToCreate.getPersonId()).orElseThrow(
                () -> new BoardroomException(HttpStatus.NOT_FOUND, "A person with this id does not exist"));
        SpecificBoardGame specificBoardGameToFind = specificBoardGameRepo
                .findById(borrowRequestToCreate.getSpecificBoardGameId())
                .orElseThrow(() -> new BoardroomException(HttpStatus.NOT_FOUND,
                        "A specific board game with this id does not exist"));

        BorrowRequest borrowRequest = new BorrowRequest(borrowRequestToCreate.getStatus(),
                borrowRequestToCreate.getRequestStartDate(),
                borrowRequestToCreate.getRequestEndDate(),
                personToFind,
                specificBoardGameToFind);
        return borrowRequestRepo.save(borrowRequest);
    }

    @Transactional
    public BorrowRequest updateBorrowRequestStatus(int id, RequestStatus status) {
        BorrowRequest borrowRequest = borrowRequestRepo.findById(id).orElseThrow(
                () -> new BoardroomException(HttpStatus.NOT_FOUND, "A borrow request with this id does not exist"));
        borrowRequest.setStatus(status);
        return borrowRequestRepo.save(borrowRequest);
    }

    public List<BorrowRequest> viewBorrowRequestsByBoardgame(int specificBoardGameId) {
        SpecificBoardGame specificBoardGame = specificBoardGameRepo.findById(specificBoardGameId)
            .orElseThrow(() -> new BoardroomException(
                HttpStatus.NOT_FOUND,
                String.format("A specific board game with this id (%d) does not exist", specificBoardGameId)
            ));
        return borrowRequestRepo.findBySpecificBoardGameAndStatus(specificBoardGame, RequestStatus.RETURNED);
    }

    public List<BorrowRequest> viewPendingBorrowRequests() {
        return borrowRequestRepo.findByStatus(RequestStatus.PENDING);
    }

    public BorrowRequest getBorrowRequestById(int id) {
        return borrowRequestRepo.findById(id)
            .orElseThrow(() -> new BoardroomException(
                HttpStatus.NOT_FOUND,
                String.format("A borrow request with this id (%d) does not exist", id)
            ));
    }

    @Transactional
    public void deleteBorrowRequestById(int id) {
        borrowRequestRepo.findById(id)
            .orElseThrow(() -> new BoardroomException(
                HttpStatus.NOT_FOUND,
                String.format("A borrow request with this id (%d) does not exist", id)
            ));

        borrowRequestRepo.deleteById(id);
    }

    private void validateRequestTimes(LocalDateTime startTime, LocalDateTime endTime) {
                LocalDateTime now = LocalDateTime.now();

                if (startTime.isBefore(now)) {
                        throw new BoardroomException(
                                        HttpStatus.BAD_REQUEST,
                                        "Start time cannot be in the past");
                }

                if (endTime.isBefore(startTime)) {
                        throw new BoardroomException(
                                        HttpStatus.BAD_REQUEST,
                                        "End time must be after start time");
                }
    }

     
    public List<BorrowRequest> viewBorrowRequestsByPersonAndStatus(int personId, RequestStatus status) {
        Person person = personRepo.findById(personId)
            .orElseThrow(() -> new BoardroomException(
                HttpStatus.NOT_FOUND,
                String.format("A person with this id (%d) does not exist", personId)
            ));
        return borrowRequestRepo.findByPersonAndStatus(person, status);
    }

    public List<BorrowRequest> viewBorrowRequestsByOwnerAndStatus(int ownerId, RequestStatus status) {
        Person owner = personRepo.findById(ownerId)
            .orElseThrow(() -> new BoardroomException(
                HttpStatus.NOT_FOUND,
                String.format("A person with this id (%d) does not exist", ownerId)
            ));
    
        return borrowRequestRepo.findByOwnerIdAndStatus(ownerId, status);
    }
    
    

}
