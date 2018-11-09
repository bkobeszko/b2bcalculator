package pl.bkobeszko.b2bcalculator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Copyright (c) 2018, GNU AGPL v. 3.0
 *
 * @author Bartłomiej Kobeszko
 */
@ControllerAdvice(annotations = RestController.class)
@Slf4j
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
    
    @ExceptionHandler(value = { Throwable.class })
    protected ResponseEntity<Object> handleError(RuntimeException ex, WebRequest request) {
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        log.error("REST error handled as {}. WebRequest: {} RuntimeException: ", httpStatus, request, ex);
        
        return handleExceptionInternal(ex, httpStatus, new HttpHeaders(), httpStatus, request);
    }
}
