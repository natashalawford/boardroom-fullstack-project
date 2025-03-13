package ca.mcgill.ecse321.boardroom.dtos;

import java.time.LocalDateTime;

import ca.mcgill.ecse321.boardroom.model.BorrowRequest;
import ca.mcgill.ecse321.boardroom.model.enums.RequestStatus;

public class BorrowRequestDtoSpecific {
    private int id;
    private RequestStatus status;
    private LocalDateTime requestStartDate;
    private LocalDateTime requestEndDate;
    private int personId;
    private int specificBoardGameId;

    public BorrowRequestDtoSpecific() {
        
    }
    
    public BorrowRequestDtoSpecific(BorrowRequest borrowRequest) {
        this.id = borrowRequest.getId();
        this.status = borrowRequest.getStatus();
        this.requestStartDate = borrowRequest.getRequestStartDate();
        this.requestEndDate = borrowRequest.getRequestEndDate();
        this.personId = borrowRequest.getPerson().getId();
        this.specificBoardGameId = borrowRequest.getSpecificBoardGame().getId();
    }

    public int getId() {
        return id;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public LocalDateTime getRequestEndDate() {
        return requestEndDate;
    }

    public LocalDateTime getRequestStartDate() {
        return requestStartDate;
    }

    public int getSpecificBoardGameId() {
        return specificBoardGameId;
    }

    public int getPersonId() {
        return personId;
    }
}
