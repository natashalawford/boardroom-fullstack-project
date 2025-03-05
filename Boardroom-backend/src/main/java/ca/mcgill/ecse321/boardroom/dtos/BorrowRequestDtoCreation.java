package ca.mcgill.ecse321.boardroom.dtos;

import java.time.LocalDateTime;

import ca.mcgill.ecse321.boardroom.model.enums.RequestStatus;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

public class BorrowRequestDtoCreation {

    @NotNull(message = "status is required")
    private RequestStatus status;

    @NotNull(message = "requested start date is required")
    @Future(message = "Start date and time must be in the future.")
    private LocalDateTime requestStartDate;

    @NotNull(message = "requested end date is required")
    @Future(message = "End date and time must be in the future.")
    private LocalDateTime requestEndDate;

    @NotNull(message = "person is required")
    private Integer personId;

    @NotNull(message = "specific board game is required")
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
