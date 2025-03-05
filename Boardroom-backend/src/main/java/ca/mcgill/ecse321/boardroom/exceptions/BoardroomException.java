package ca.mcgill.ecse321.boardroom.exceptions;

import org.springframework.http.HttpStatus;

public class BoardroomException extends RuntimeException{
    private HttpStatus status;

    public BoardroomException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
