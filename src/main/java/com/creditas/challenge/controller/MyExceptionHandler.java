package com.creditas.challenge.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.NoSuchElementException;

import javax.management.InvalidAttributeValueException;
import javax.persistence.NoResultException;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class MyExceptionHandler extends ResponseEntityExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(MyExceptionHandler.class);

	@ExceptionHandler({NoResultException.class, NoSuchElementException.class})
	public ResponseEntity<String> handleNotFound(Exception ex) {
        String error = ex.getMessage();
        logger.error("{}", ex);
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

	@ExceptionHandler({IllegalArgumentException.class, ParseException.class})
	public ResponseEntity<String> handleAttributeException(Exception ex) {
		String error = ex.getMessage();
        logger.error("{}", ex);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
	
	@ExceptionHandler(InvalidAttributeValueException.class)
	public void handleAttributeValueException(HttpServletResponse response, Exception ex) throws IOException {
        logger.error("{}", ex);
        response.sendError(HttpStatus.NO_CONTENT.value());
    }
	
	@ExceptionHandler(DataIntegrityViolationException.class)
	public void handleConstraintError(HttpServletResponse response, Exception ex) throws IOException {
        logger.error("{}", ex);
        response.sendError(HttpStatus.NOT_MODIFIED.value(), ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public void handleError(HttpServletResponse response, Exception ex) throws Exception {
        logger.error("{}", ex);
        response.sendError(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
    }
}
