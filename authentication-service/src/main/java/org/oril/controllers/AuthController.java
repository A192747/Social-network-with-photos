package org.oril.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.oril.dto.UserDTO;
import org.oril.entities.AuthResponse;
import org.oril.services.AuthService;
import org.oril.exceptions.ErrorResponse;
import org.oril.exceptions.NotValidException;
import org.oril.exceptions.UnAuthException;
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
    public ResponseEntity<AuthResponse> register(@RequestBody @Valid UserDTO request,
                                                 BindingResult bindingResult) {
        hasErrors(bindingResult);
        AuthResponse user = authService.register(request);
        if (user.equals(new AuthResponse())) {
            throw new UnAuthException("Не удалось зарегистрировать пользователя с таким именем! Попробуйте другое имя пользователя");
        }
        return ResponseEntity.ok(user);
    }
    @PostMapping(value = "/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid UserDTO request,
                                              BindingResult bindingResult) {
        hasErrors(bindingResult);
        AuthResponse user = authService.login(request);
        if (user.equals(new AuthResponse())) {
            throw new UnAuthException("Неверный логин или пароль!");
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

}
