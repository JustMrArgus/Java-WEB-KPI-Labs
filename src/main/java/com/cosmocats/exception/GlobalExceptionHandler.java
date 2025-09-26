package com.cosmocats.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

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
        problem.setDetail("Validation failed for object 'ProductDto'. See errors for details.");
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
