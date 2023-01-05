package me.nerminsehic.groupevent.exception;

import java.io.IOException;

public class SendMailException extends RuntimeException {

    public SendMailException(IOException ex) {
        super(ex.getMessage(), ex.getCause());
    }
}
