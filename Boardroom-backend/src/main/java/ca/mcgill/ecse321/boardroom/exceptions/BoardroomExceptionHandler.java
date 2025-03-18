package ca.mcgill.ecse321.boardroom.exceptions;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import ca.mcgill.ecse321.boardroom.dtos.ErrorDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

@ControllerAdvice
public class BoardroomExceptionHandler {
    @ExceptionHandler(BoardroomException.class)
    public ResponseEntity<ErrorDto> handleEventRegistrationException(BoardroomException e) {
        return new ResponseEntity<ErrorDto>(new ErrorDto(e.getMessage()), e.getStatus());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorDto> handleConstraintViolationException(ConstraintViolationException e) {
        List<String> errors = new ArrayList<>();
        for (ConstraintViolation<?> cv : e.getConstraintViolations()) {
            errors.add(cv.getMessage());
        }
        return new ResponseEntity<ErrorDto>(new ErrorDto(errors), HttpStatus.BAD_REQUEST);
    }
}