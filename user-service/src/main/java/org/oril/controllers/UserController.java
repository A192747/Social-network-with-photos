package org.oril.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.oril.dao.UserDAO;
import org.oril.dto.UserDTO;
import org.oril.models.User;
import org.oril.exceptions.NotValidException;
import org.oril.service.UserService;
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
    private UserService service;

    @PostMapping("/register")
    public ResponseEntity<User> save(@RequestBody UserDTO user) {
        return ResponseEntity.ok(service.save(user));
    }
    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody UserDTO user) {
        return ResponseEntity.ok(service.login(user));
    }



}
