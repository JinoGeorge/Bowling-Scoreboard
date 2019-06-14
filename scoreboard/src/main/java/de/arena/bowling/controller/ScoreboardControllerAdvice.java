package de.arena.bowling.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Methods to handle the exceptions thrown from the scoreboard application.
 * Only the 'message' from the exceptions are extracted and passed to the error view.
 */
@ControllerAdvice
@Slf4j
public class ScoreboardControllerAdvice {

    /**
     * Handles all exceptions without specific exception handlers defined.
     * Extracts the exception message from all exceptions from the application.
     * All errors coming here will be passed to the "error" view.
     *
     * @return the view name "error" to display the errors.
     */
    @ExceptionHandler(Exception.class)
    public String handleException(Exception exception, Model model) {
        log.error("", exception);
        model.addAttribute("errorMessage", exception.getMessage());
        return "error";
    }

    /**
     * Handles ConstrainViolationExceptions from javax bean validations.
     * The invalid field names and the error messages will be passed to the "error" view from here.
     *
     * @return the view name "error" to display the errors.
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public String handleConstraintViolationException(ConstraintViolationException exception, Model model) {
        List<String> violations = exception.getConstraintViolations()
                .stream()
                .map(violation -> violation.getPropertyPath().toString() + " : " + violation.getMessage())
                .collect(Collectors.toList());
        model.addAttribute("errorMessages", violations);
        return "error";
    }


}
