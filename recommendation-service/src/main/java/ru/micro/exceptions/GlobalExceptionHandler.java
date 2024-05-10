package ru.micro.exceptions;

import jakarta.ws.rs.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({IOException.class,
            InvalidKeyException.class, NoSuchAlgorithmException.class})
    public ResponseEntity<ErrorResponse> handleServerExceptions(Exception exception) {
        ErrorResponse response = new ErrorResponse(
                exception.getMessage(),
                new Date(System.currentTimeMillis())
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({NotFoundException.class, NotValidException.class, IllegalArgumentException.class, IllegalArgumentException.class})
    public ResponseEntity<ErrorResponse> handleValidationExceptions(Exception exception) {
        ErrorResponse response = new ErrorResponse(
                exception.getMessage(),
                new Date(System.currentTimeMillis())
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

//    @ExceptionHandler({})
//    public ResponseEntity<ErrorResponse> handleNotFoundExceptions(Exception exception) {
//        ErrorResponse response = new ErrorResponse(
//                exception.getMessage(),
//                new Date(System.currentTimeMillis())
//        );
//        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
//    }
}

