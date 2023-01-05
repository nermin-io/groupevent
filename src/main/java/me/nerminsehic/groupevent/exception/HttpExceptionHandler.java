package me.nerminsehic.groupevent.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

@ControllerAdvice
public class HttpExceptionHandler {

    @ExceptionHandler(UniqueConstraintException.class)
    public ResponseEntity<HttpExceptionResponse> handleUniqueConstraintException(UniqueConstraintException ex) {
        HttpExceptionResponse response = new HttpExceptionResponse(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST,
                Instant.now()
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<HttpExceptionResponse> handleNotFoundException(NotFoundException ex) {
        HttpExceptionResponse response = new HttpExceptionResponse(
                ex.getMessage(),
                HttpStatus.NOT_FOUND,
                Instant.now()
        );

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(LinkException.class)
    public ResponseEntity<HttpExceptionResponse> handleLinkException(LinkException ex) {
        HttpExceptionResponse response = new HttpExceptionResponse(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST,
                Instant.now()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalOperationException.class)
    public ResponseEntity<HttpExceptionResponse> handleIllegalOperationException(IllegalOperationException ex) {
        HttpExceptionResponse response = new HttpExceptionResponse(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST,
                Instant.now()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
