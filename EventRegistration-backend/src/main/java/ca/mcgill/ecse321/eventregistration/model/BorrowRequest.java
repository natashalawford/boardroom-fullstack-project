package ca.mcgill.ecse321.eventregistration.model;

import ca.mcgill.ecse321.eventregistration.model.enums.RequestStatus;
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
    private LocalDateTime requestDate;

    /*
    The associations are a bit weird with this one, but let's say you want to get
    all the accepted requests of a certain person, you'd search the review table
    and get all reviews with acceptedLoaner of id that you're looking for.
     */
    @ManyToOne
    private Person borrower;

    // TODO: Below are not Person but GameOwner
    // TODO: Perhaps remove pendingLoaner, since we can just get all game
    //  owners with a certain game
    @ManyToOne
    private Person acceptedLoaner;
    @ManyToOne
    private Person pendingLoaner;

    protected BorrowRequest() {}

    public BorrowRequest(RequestStatus status, LocalDateTime requestDate,
                         Person borrower, Person acceptedLoaner,
                         Person pendingLoaner) {
        this.status = status;
        this.requestDate = requestDate;
        this.borrower = borrower;
        this.acceptedLoaner = acceptedLoaner;
        this.pendingLoaner = pendingLoaner;
    }

    public int getId() {
        return id;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public LocalDateTime getRequestDate() {
        return requestDate;
    }

    public Person getBorrower() {
        return borrower;
    }

    public Person getAcceptedLoaner() {
        return acceptedLoaner;
    }

    public Person getPendingLoaner() {
        return pendingLoaner;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    public void setAcceptedLoaner(Person acceptedLoaner) {
        this.acceptedLoaner = acceptedLoaner;
    }
}
