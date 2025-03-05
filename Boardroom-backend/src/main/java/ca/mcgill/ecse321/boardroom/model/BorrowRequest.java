package ca.mcgill.ecse321.boardroom.model;

import ca.mcgill.ecse321.boardroom.model.enums.RequestStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

import java.time.LocalDateTime;

@Entity
public class BorrowRequest {
    @Id
    @GeneratedValue
    private int id;
    private RequestStatus status;
    private LocalDateTime requestStartDate;
    private LocalDateTime requestEndDate;

    @ManyToOne
    private Person person;

    @ManyToOne
    private SpecificBoardGame specificBoardGame;

    protected BorrowRequest() {}

    public BorrowRequest(RequestStatus status,
                         LocalDateTime requestStartDate,
                         LocalDateTime requestEndDate, Person person,
                         SpecificBoardGame specificBoardGame) {
        this.status = status;
        this.requestStartDate = requestStartDate;
        this.requestEndDate = requestEndDate;
        this.person = person;
        this.specificBoardGame = specificBoardGame;
    }

    public BorrowRequest(int id, RequestStatus status,
                         LocalDateTime requestStartDate,
                         LocalDateTime requestEndDate, Person person,
                         SpecificBoardGame specificBoardGame) {
        this.id = id;
        this.status = status;
        this.requestStartDate = requestStartDate;
        this.requestEndDate = requestEndDate;
        this.person = person;
        this.specificBoardGame = specificBoardGame;
    }

    public int getId() {
        return id;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public LocalDateTime getRequestStartDate() {
        return requestStartDate;
    }

    public LocalDateTime getRequestEndDate() {
        return requestEndDate;
    }

    public Person getPerson() {
        return person;
    }

    public SpecificBoardGame getSpecificBoardGame() {
        return specificBoardGame;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }

}
