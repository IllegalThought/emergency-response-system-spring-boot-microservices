package com.campusnaksha.incident.exception;

public class InvalidIncidentStateException
        extends RuntimeException {

    public InvalidIncidentStateException(
            String message
    ) {
        super(message);
    }
}