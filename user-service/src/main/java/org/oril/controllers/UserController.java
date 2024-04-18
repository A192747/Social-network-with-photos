package org.oril.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.oril.dao.UserDAO;
import org.oril.dto.UserDTO;
import org.oril.models.User;
import org.oril.exceptions.NotValidException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.oril.exceptions.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping(value = "/users")
@AllArgsConstructor
public class UserController {
    @Autowired
    private final UserDAO userDAO;
    @Autowired
    public final ModelMapper modelMapper;

    @PostMapping("/register")
    public ResponseEntity<User> save(@RequestBody UserDTO user) {
        return ResponseEntity.ok(userDAO.save(convertToUser(user)));
    }
    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody UserDTO user) {
        return ResponseEntity.ok(userDAO.login(convertToUser(user)));
    }
    private User convertToUser(UserDTO user) {
        return modelMapper.map(user, User.class);
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
