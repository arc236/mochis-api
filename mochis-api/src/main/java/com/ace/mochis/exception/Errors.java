package com.ace.mochis.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.validation.ConstraintViolation;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Errors {

    @JsonProperty("errors")
    private Set<Error> errors;

    public Errors() {
        this.errors = new HashSet<>();
    }

    public Set<Error> getErrors() {
        return errors;
    }

    public void setErrors(Set<Error> errors) {
        this.errors = errors;
    }

    public boolean containsError(ErrorMessages errorMessage) {
        for (Iterator<Error> iterator = errors.iterator(); iterator.hasNext(); ) {
            Error error = (Error) iterator.next();
            if (error.errorCode.equals(errorMessage.toString())) {
                return true;
            }
        }
        return false;
    }

    public void add(ErrorMessages errorMessage) {
        this.errors.add(new Error(errorMessage.name(), errorMessage.getErrorMessage()));
    }

    public void add(Error error) {
        this.errors.add(error);
    }

    public <T> void addConstraintViolationErrors(Set<ConstraintViolation<T>> violations) {
        for (ConstraintViolation<T> violation : violations) {
            add(ErrorMessages.valueOf(violation.getMessage()));
        }
    }

    @JsonSerialize
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    public static class Error {

        @JsonProperty("error")
        private String errorCode;

        @JsonProperty("error_description")
        private String errorDescription;

        public Error() {
        }

        public String getErrorCode() {
            return errorCode;
        }

        public void setErrorCode(String errorCode) {
            this.errorCode = errorCode;
        }

        public String getErrorDescription() {
            return errorDescription;
        }

        public void setErrorDescription(String errorDescription) {
            this.errorDescription = errorDescription;
        }

        public Error(String errorCode, String errorDescription) {
            this.errorCode = errorCode;
            this.errorDescription = errorDescription;
        }

        public String toString() {
            return this.errorCode + ":" + this.errorDescription;
        }
    }

}
