package org.mortgage.exceptions;

public class FundsNotAvailableException extends RuntimeException {
    public FundsNotAvailableException(String message) {
        super(message);
    }
}
