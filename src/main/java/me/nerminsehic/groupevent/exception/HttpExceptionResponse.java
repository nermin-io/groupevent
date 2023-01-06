package me.nerminsehic.groupevent.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.Map;

@Data
@AllArgsConstructor
public class HttpExceptionResponse {

    @JsonProperty("message")
    private String message;

    @JsonProperty("status")
    private HttpStatus status;

    @JsonProperty("timestamp")
    private Instant timestamp;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("errors")
    private Map<String, String> errors;

    public HttpExceptionResponse(String message) {
        this(message, HttpStatus.BAD_REQUEST);
    }

    public HttpExceptionResponse(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
        this.timestamp = Instant.now();
    }

    public HttpExceptionResponse(String message, HttpStatus status, Map<String, String> errors) {
        this(message, status);
        this.errors = errors;
    }
}
