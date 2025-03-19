package ca.mcgill.ecse321.boardroom.dtos.responses;

import java.time.LocalDateTime;

import ca.mcgill.ecse321.boardroom.model.BorrowRequest;
import ca.mcgill.ecse321.boardroom.model.enums.RequestStatus;
import jakarta.validation.constraints.NotNull;

public class BorrowRequestResponseDto {

    @NotNull(message = "id is required")
    private Integer id;

    @NotNull(message = "status is required")
    private RequestStatus status;

    @NotNull(message = "requestStartDate is required")
    private LocalDateTime requestStartDate;

    @NotNull(message = "requestEndDate is required")
    private LocalDateTime requestEndDate;

    @NotNull(message = "personId is required")
    private Integer personId;

    @NotNull(message = "specificBoardGameId is required")
    private Integer specificBoardGameId;

    public BorrowRequestResponseDto() {
    }
    
    public BorrowRequestResponseDto(BorrowRequest borrowRequest) {
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
