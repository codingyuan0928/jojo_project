package com.group1.gosports_jojo.exception;

public class EmailSendFailedException extends RuntimeException{
    public EmailSendFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
