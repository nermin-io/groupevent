package me.nerminsehic.groupevent.exception;

public class IllegalAccessTokenException extends RuntimeException {

    public IllegalAccessTokenException(String message) {
        super(message);
    }

    public IllegalAccessTokenException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalAccessTokenException(Throwable cause) {
        super(cause);
    }
}
