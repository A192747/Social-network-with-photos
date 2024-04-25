package ru.micro.controller;

import io.minio.errors.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.micro.dto.PhotoUploadResponse;
import ru.micro.service.FileService;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/posts")
public class FileController {

    @Autowired
    private FileService fileService;

    @PostMapping("/{postId}/photos/{photoId}")
    public ResponseEntity<PhotoUploadResponse> upload(@PathVariable("postId") int postId,
                                                      @PathVariable("photoId") int photoId,
                                                      @RequestParam("photo") MultipartFile file) throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        InputStream in = new ByteArrayInputStream(file.getBytes());
        return ResponseEntity.ok(fileService.save(postId, photoId, in));
    }

    @GetMapping("/{postId}/photos/{photoId}")
    public ResponseEntity<ByteArrayResource> getPhoto(@PathVariable("postId") int postId,
                                                      @PathVariable("photoId") int photoId) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        ByteArrayResource photo = fileService.getPhoto(postId, photoId);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(photo);
    }

}
