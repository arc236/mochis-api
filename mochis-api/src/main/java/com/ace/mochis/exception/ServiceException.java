package com.ace.mochis.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ServiceException extends RuntimeException{

    private Errors errors = new Errors();
    private HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

    public ServiceException(ErrorMessages error, HttpStatus httpStatus) {
        this.errors.add(error);
        this.httpStatus = httpStatus;
    }
}
