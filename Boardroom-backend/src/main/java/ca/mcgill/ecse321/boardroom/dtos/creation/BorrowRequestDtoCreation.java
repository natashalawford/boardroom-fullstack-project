package ca.mcgill.ecse321.boardroom.dtos.creation;

import java.time.LocalDateTime;

import ca.mcgill.ecse321.boardroom.model.enums.RequestStatus;
import jakarta.validation.constraints.NotNull;

public class BorrowRequestDtoCreation {

    @NotNull(message = "status is Required")
    private RequestStatus status;

    @NotNull(message = "requestStartDate is required")
    private LocalDateTime requestStartDate;

    @NotNull(message = "requestEndDate is required")
    private LocalDateTime requestEndDate;

    @NotNull(message = "personId is required")
    private Integer personId;

    @NotNull(message = "specificBoardGameId is required")
    private Integer specificBoardGameId;

    public BorrowRequestDtoCreation() {
    }

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
