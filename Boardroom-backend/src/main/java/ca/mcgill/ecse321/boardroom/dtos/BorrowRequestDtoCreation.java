package ca.mcgill.ecse321.boardroom.dtos;

import java.time.LocalDateTime;

import ca.mcgill.ecse321.boardroom.model.enums.RequestStatus;

public class BorrowRequestDtoCreation {

    private RequestStatus status;

    private LocalDateTime requestStartDate;


    private LocalDateTime requestEndDate;

    private Integer personId;

    private Integer specificBoardGameId;

    public BorrowRequestDtoCreation(RequestStatus status,
            LocalDateTime requestStartDate,
            LocalDateTime requestEndDate, int personId,
            int specificBoardGameId) {
        this.status = status;
        this.requestStartDate = requestStartDate;
        this.requestEndDate = requestEndDate;
        this.personId = personId;
        this.specificBoardGameId = specificBoardGameId;
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
