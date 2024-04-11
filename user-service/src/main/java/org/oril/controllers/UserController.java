package org.oril.controllers;

import lombok.AllArgsConstructor;
import org.oril.dao.UserDAO;
import org.oril.entities.AuthRequest;
import org.oril.entities.UserVO;
import org.oril.entities.NotValidException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.oril.entities.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping(value = "/users")
@AllArgsConstructor
public class UserController {

    private final UserDAO userDAO;
    @PostMapping("/register")
    public ResponseEntity<UserVO> save(@RequestBody AuthRequest userVO) {
        return ResponseEntity.ok(userDAO.save(userVO));
    }
    @PostMapping("/login")
    public ResponseEntity<UserVO> login(@RequestBody AuthRequest userVO) {
        return ResponseEntity.ok(userDAO.login(userVO));
    }

    @GetMapping("/secured")
    public ResponseEntity<String> securedEndpoint() {
        return ResponseEntity.ok("Hello, from secured endpoint!");
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(DataIntegrityViolationException ex) {
        ErrorResponse response = new ErrorResponse(
                ex.getMessage().substring(ex.getMessage().indexOf("ERROR:"), ex.getMessage().indexOf("Detail:")),
                new Date(System.currentTimeMillis())
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(NotValidException exception) {
        ErrorResponse response = new ErrorResponse(
                exception.getMessage(),
                new Date(System.currentTimeMillis())
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
