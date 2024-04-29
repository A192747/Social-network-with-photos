package ru.micro.controller;

//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.micro.DTO.PostGetResponse;
import ru.micro.DTO.PostUploadHeaders;
import ru.micro.DTO.PostUploadObject;
import ru.micro.model.Dto;
import ru.micro.services.PostUploadService;
import ru.micro.util.NotValidException;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/posts")
@AllArgsConstructor
public class PostUploadController {
    private final PostUploadService postService;
    @PostMapping()
    public void generate(@RequestHeader("id") String id,
                         @RequestBody @Valid PostUploadObject post,
                         BindingResult bindingResult){
        hasErrors(bindingResult);

        postService.createAndSavePost(post, id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostGetResponse> getSnippet(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(postService.get(id));
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
