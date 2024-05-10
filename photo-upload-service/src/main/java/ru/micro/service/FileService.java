package ru.micro.service;

import io.minio.*;
import io.minio.errors.*;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.micro.dto.PhotoUploadResponse;
import ru.micro.entities.Post;
import ru.micro.exceptions.NotValidException;
import ru.micro.repository.PostRepository;
import ru.micro.util.ImageAverageColor;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@Service
public class FileService {

    @Autowired
    private MinioClient minioClient;

    @Autowired
    private PostRepository postRepository;
    @Value("${minio.bucket-name}")
    private String bucketName;

    @Transactional
    public PhotoUploadResponse save(UUID postId, int photoId, MultipartFile file) throws IllegalArgumentException, ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        InputStream inputStream = new ByteArrayInputStream(file.getBytes());
        InputStream inputStreamCopy = new ByteArrayInputStream(file.getBytes());
        if (photoId < 0)
            throw new IllegalArgumentException("Значение photoId не должны быть отрицательными!");
        String fileName = "post-" + postId + "-photo-" + photoId + ".jpg";
        Post post = postRepository.findById(postId).orElseThrow(() -> new NotValidException("Такой пост не существует!"));
        List<String> colors = post.getColorPreload();
        if (colors == null) {
            colors = new ArrayList<>();
            post.setColorPreload(colors);
        }
        if (colors.size() == post.getImagesAmount())
            throw new NotValidException("Все фото уже подгружены!");
        String hex = ImageAverageColor.getAverageColorHex(inputStreamCopy);
        post.getColorPreload().add(hex);
        postRepository.save(post);
        minioClient.putObject(PutObjectArgs.builder().bucket(bucketName).object(fileName)
                .stream(inputStream, -1, 10485760).build());
        System.out.println(inputStream);
        checkPostReadiness(postId);

        if (inputStream != null) {
            inputStream.close();
        }
        if (inputStreamCopy != null) {
            inputStreamCopy.close();
        }

        return new PhotoUploadResponse(fileName, new Date(System.currentTimeMillis()));
    }

    private void checkPostReadiness(UUID postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new NotValidException("Такой пост не существует!"));
        boolean isReady = (post.getImagesAmount() == 0 && post.getColorPreload() == null)
                || (post.getColorPreload() != null && post.getColorPreload().size() == post.getImagesAmount());
        isReady = isReady && (Math.abs(post.getSnippetState()) == 1);
        if (isReady) {
            post.setPostIsReady(true);
            postRepository.save(post);
        }
    }


    public ByteArrayResource getPhoto(UUID postId, int photoId) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        if (photoId < 0)
            throw new IllegalArgumentException("Значение photoId не должны быть отрицательными!");
        String photoName = "post-" + postId + "-photo-" + photoId + ".jpg";
        InputStream stream = minioClient.getObject(GetObjectArgs.builder().bucket(bucketName).object(photoName).build());
        byte[] photoData = IOUtils.toByteArray(stream);
        return new ByteArrayResource(photoData);
    }


}
