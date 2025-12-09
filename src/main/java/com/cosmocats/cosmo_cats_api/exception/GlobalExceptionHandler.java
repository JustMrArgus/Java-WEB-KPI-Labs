package com.cosmocats.cosmo_cats_api.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ProblemDetail> handleAccessDeniedException(AccessDeniedException ex, HttpServletRequest request) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.FORBIDDEN);
        problem.setType(URI.create("https://cosmocats.example.com/problems/forbidden"));
        problem.setTitle("Access Denied");
        problem.setDetail(ex.getMessage());
        problem.setInstance(URI.create(request.getRequestURI()));

        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .contentType(MediaType.valueOf("application/problem+json"))
                .body(problem);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<Object> errors = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(fe -> {
                return new java.util.HashMap<String, Object>() {{
                    put("fieldName", fe.getField());
                    put("reason", fe.getDefaultMessage());
                }};
            })
            .collect(Collectors.toList());

        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problem.setType(URI.create("https://cosmocats.example.com/problems/validation-error"));
        problem.setTitle("Validation Failed");
        problem.setDetail("Validation failed for request. See errors for details.");
        problem.setProperty("errors", errors);
        problem.setInstance(URI.create(request.getRequestURI()));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.valueOf("application/problem+json"))
                .body(problem);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ProblemDetail> handleIllegalArg(IllegalArgumentException ex, HttpServletRequest request) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problem.setType(URI.create("https://cosmocats.example.com/problems/invalid-argument"));
        problem.setTitle("Invalid Argument");
        problem.setDetail(ex.getMessage());
        problem.setInstance(URI.create(request.getRequestURI()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.valueOf("application/problem+json"))
                .body(problem);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleResourceNotFound(ResourceNotFoundException ex, HttpServletRequest request) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problem.setType(URI.create("https://cosmocats.example.com/problems/resource-not-found"));
        problem.setTitle("Resource Not Found");
        problem.setDetail(ex.getMessage());
        problem.setInstance(URI.create(request.getRequestURI()));
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.valueOf("application/problem+json"))
                .body(problem);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ProblemDetail> handleDataIntegrityViolation(DataIntegrityViolationException ex, HttpServletRequest request) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.CONFLICT);
        problem.setType(URI.create("https://cosmocats.example.com/problems/data-integrity-violation"));
        problem.setTitle("Data Integrity Violation");
        problem.setDetail("A database constraint was violated. The operation could not be completed.");
        problem.setInstance(URI.create(request.getRequestURI()));
        problem.setProperty("cause", ex.getMostSpecificCause().getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .contentType(MediaType.valueOf("application/problem+json"))
                .body(problem);
    }

    @ExceptionHandler(PersistenceException.class)
    public ResponseEntity<ProblemDetail> handlePersistenceException(PersistenceException ex, HttpServletRequest request) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        problem.setType(URI.create("https://cosmocats.example.com/problems/persistence-error"));
        problem.setTitle("Database Error");
        problem.setDetail(ex.getMessage());
        problem.setInstance(URI.create(request.getRequestURI()));

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.valueOf("application/problem+json"))
                .body(problem);
    }

    @ExceptionHandler(DataIntegrityException.class)
    public ResponseEntity<ProblemDetail> handleDataIntegrityException(DataIntegrityException ex, HttpServletRequest request) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.CONFLICT);
        problem.setType(URI.create("https://cosmocats.example.com/problems/data-integrity-error"));
        problem.setTitle("Data Integrity Error");
        problem.setDetail(ex.getMessage());
        problem.setInstance(URI.create(request.getRequestURI()));

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .contentType(MediaType.valueOf("application/problem+json"))
                .body(problem);
    }

    @ExceptionHandler(FeatureNotAvailableException.class)
    public ResponseEntity<ProblemDetail> handleFeatureNotAvailable(FeatureNotAvailableException ex, HttpServletRequest request) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.FORBIDDEN);
        problem.setType(URI.create("https://cosmocats.example.com/problems/feature-not-available"));
        problem.setTitle("Feature Not Available");
        problem.setDetail(ex.getMessage());
        problem.setInstance(URI.create(request.getRequestURI()));

        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .contentType(MediaType.valueOf("application/problem+json"))
                .body(problem);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleAll(Exception ex, HttpServletRequest request) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        problem.setType(URI.create("https://cosmocats.example.com/problems/internal-server-error"));
        problem.setTitle("Internal Server Error");
        problem.setDetail("An unexpected error occurred.");
        problem.setInstance(URI.create(request.getRequestURI()));
        problem.setProperty("exception", ex.getClass().getSimpleName());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.valueOf("application/problem+json"))
                .body(problem);
    }
}