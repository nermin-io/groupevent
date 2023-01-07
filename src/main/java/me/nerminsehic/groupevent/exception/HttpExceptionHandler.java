package me.nerminsehic.groupevent.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class HttpExceptionHandler {

    @ExceptionHandler(UniqueConstraintException.class)
    public ResponseEntity<HttpExceptionResponse> handleUniqueConstraintException(UniqueConstraintException ex) {
        HttpExceptionResponse response = new HttpExceptionResponse(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<HttpExceptionResponse> handleNotFoundException(NotFoundException ex) {
        HttpExceptionResponse response = new HttpExceptionResponse(
                ex.getMessage(),
                HttpStatus.NOT_FOUND
        );

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(LinkException.class)
    public ResponseEntity<HttpExceptionResponse> handleLinkException(LinkException ex) {
        HttpExceptionResponse response = new HttpExceptionResponse(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalOperationException.class)
    public ResponseEntity<HttpExceptionResponse> handleIllegalOperationException(IllegalOperationException ex) {
        HttpExceptionResponse response = new HttpExceptionResponse(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<HttpExceptionResponse> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        HttpExceptionResponse httpException = new HttpExceptionResponse(
                "Could not process the entity as one or more fields are invalid",
                HttpStatus.UNPROCESSABLE_ENTITY,
                errors
        );
        return new ResponseEntity<>(httpException, HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
