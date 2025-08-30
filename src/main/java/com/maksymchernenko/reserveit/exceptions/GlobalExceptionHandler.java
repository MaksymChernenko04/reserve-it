package com.maksymchernenko.reserveit.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * The global exception handler.
 * <p>
 * Provides centralized handling of exceptions thrown by controllers.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles all unexpected exceptions and returns a generic error page.
     *
     * @return the error page name
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = { Exception.class })
    public String handleBadRequestException() { return "error"; }
}
