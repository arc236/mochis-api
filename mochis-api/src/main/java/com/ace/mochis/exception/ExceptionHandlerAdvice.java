package com.ace.mochis.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@ControllerAdvice(annotations = RestController.class)
public class ExceptionHandlerAdvice {
    @ExceptionHandler(ServiceException.class)
    @ResponseBody
    ResponseEntity<Errors> handleException(ServiceException e, HttpServletResponse response) throws IOException {
        Errors errors = e.getErrors();
        return new ResponseEntity<>(errors, e.getHttpStatus());
    }
}
