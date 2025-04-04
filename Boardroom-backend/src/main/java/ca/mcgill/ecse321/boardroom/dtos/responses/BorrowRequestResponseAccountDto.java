package ca.mcgill.ecse321.boardroom.dtos.responses;

import java.time.LocalDateTime;

import ca.mcgill.ecse321.boardroom.model.BorrowRequest;
import ca.mcgill.ecse321.boardroom.model.enums.RequestStatus;
import jakarta.validation.constraints.NotNull;

public class BorrowRequestResponseAccountDto {

    @NotNull(message = "id is required")
    private Integer id;

    @NotNull(message = "status is required")
    private RequestStatus status;

    @NotNull(message = "requestStartDate is required")
    private LocalDateTime requestStartDate;

    @NotNull(message = "requestEndDate is required")
    private LocalDateTime requestEndDate;

    @NotNull(message = "personName is required")
    private String personName;

    @NotNull(message = "specificBoardGameTitle is required")
    private String specificBoardGameTitle;

    public BorrowRequestResponseAccountDto() {
    }
    
    public BorrowRequestResponseAccountDto(BorrowRequest borrowRequest) {
        this.id = borrowRequest.getId();
        this.status = borrowRequest.getStatus();
        this.requestStartDate = borrowRequest.getRequestStartDate();
        this.requestEndDate = borrowRequest.getRequestEndDate();
        this.personName = borrowRequest.getPerson().getName();
        this.specificBoardGameTitle = borrowRequest.getSpecificBoardGame().getBoardGame().getTitle();
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

    public String getSpecificBoardGameTitle() {
        return specificBoardGameTitle;
    }

    public String getPersonName() {
        return personName;
    }
}
