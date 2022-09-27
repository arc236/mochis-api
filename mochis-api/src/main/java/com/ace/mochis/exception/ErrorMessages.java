package com.ace.mochis.exception;

public enum ErrorMessages {
    STUBS_VALUE_UNSUPPORTED("Unsupported number of stubs"),
    INVALID_BATCH_DETAILS("Please submit correct batch details"),
    BATCH_STILL_OPEN("Batch is already opened."),
    BATCH_ALREADY_CLOSED("Batch is already closed."),
    INVALID_SERIAL("Invalid Serial"),
    STUB_NOT_FOUND("Stub not found."),
    STUB_ALREADY_OPENED("Stub already opened");

    private final String errorMessage;

    private ErrorMessages(final String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
