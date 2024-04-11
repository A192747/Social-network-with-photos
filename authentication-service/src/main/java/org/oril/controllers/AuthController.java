package org.oril.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.oril.entities.AuthRequest;
import org.oril.entities.AuthResponse;
import org.oril.entities.UserVO;
import org.oril.services.AuthService;
import org.oril.util.ErrorResponse;
import org.oril.util.NotValidException;
import org.oril.util.UnAuthException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping(value = "/register")
    public ResponseEntity<AuthResponse> register(@RequestBody @Valid AuthRequest request,
                                                 BindingResult bindingResult) {
        hasErrors(bindingResult);
        AuthResponse user = authService.register(request);
        if (user.equals(new AuthResponse())) {
            throw new UnAuthException("Не удалось зарегистрировать пользователя с таким именем! Попробуйте другое имя пользователя");
        }
        return ResponseEntity.ok(user);
    }
    @PostMapping(value = "/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid AuthRequest request,
                                              BindingResult bindingResult) {
        hasErrors(bindingResult);
        AuthResponse user = authService.login(request);
        if (user.equals(new AuthResponse())) {
            throw new UnAuthException("Не верный логин или пароль!");
        }
        return ResponseEntity.ok(user);

    }
    private void hasErrors(BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            StringBuilder errorMsg = new StringBuilder();
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                errorMsg.append(error.getField())
                        .append(" - ").append(error.getDefaultMessage())
                        .append(";");
            }
            throw new NotValidException(errorMsg.toString());
        }
    }
    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(UnAuthException exception) {
        ErrorResponse response = new ErrorResponse(
                exception.getMessage(),
                new Date(System.currentTimeMillis())
        );
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
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
