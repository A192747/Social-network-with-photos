package ru.micro.controller;


import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.micro.DTO.PostIdResponse;
import ru.micro.DTO.PostUploadObject;

import ru.micro.services.PostUploadService;
import ru.micro.exceptions.NotValidException;

import java.util.List;

@RestController
@RequestMapping("/posts")
@AllArgsConstructor
public class PostUploadController {
    private final PostUploadService postService;
    @PostMapping()
    public ResponseEntity<PostIdResponse> generate(@RequestHeader("id") int id,
                                                   @RequestBody @Valid PostUploadObject post,
                                                   BindingResult bindingResult){
        hasErrors(bindingResult);
        return ResponseEntity.ok(new PostIdResponse(postService.createAndSavePost(post, id)));
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
