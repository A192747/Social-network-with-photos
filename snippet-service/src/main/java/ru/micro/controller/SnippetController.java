package ru.micro.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.micro.DTO.SnippetCreation;
import ru.micro.DTO.SnippetResponse;
import ru.micro.services.SnippetService;
import ru.micro.util.ErrorResponse;
import ru.micro.util.NotValidException;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/snippets")
@AllArgsConstructor
public class SnippetController {
    private final SnippetService snippetService;
    @GetMapping("/{id}")
    public ResponseEntity<SnippetResponse> getSnippet(@PathVariable("id") int id) {
        return ResponseEntity.ok(snippetService.get(id));
    }

    @PostMapping()
    public void generate(@RequestBody @Valid SnippetCreation snippet,
                         BindingResult bindingResult){
        hasErrors(bindingResult);
        try {
            snippetService.createAndSaveSnippet(snippet);
        }
        catch (IOException e) {
            throw new NotValidException(snippet.getLink());
        }
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
