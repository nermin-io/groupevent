package me.nerminsehic.groupevent.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.Instant;

@Data
@AllArgsConstructor
public class HttpExceptionResponse {

    private String message;
    private HttpStatus status;
    private Instant timestamp;
}
