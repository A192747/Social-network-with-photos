package ru.micro.service;

import io.minio.*;
import io.minio.errors.*;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import ru.micro.dto.PhotoUploadResponse;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

@Service
public class FileService {

    public FileService() {

    }

    @Autowired
    private MinioClient minioClient;

    @Value("${minio.bucket-name}")
    private String bucketName;

    public PhotoUploadResponse save(int postId, int photoId, InputStream inputStream) throws IllegalArgumentException, ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        if (postId < 0 || photoId < 0)
            throw new IllegalArgumentException("Значение postId и photoId не должны быть отрицательными!");
        String fileName = "post-" + postId + "-photo-" + photoId + ".jpg";
        minioClient.putObject(PutObjectArgs.builder().bucket(bucketName).object(fileName)
                .stream(inputStream, -1, 10485760).build());

        if (inputStream != null) {
            inputStream.close();
        }

        return new PhotoUploadResponse(fileName, new Date(System.currentTimeMillis()));
    }

    public ByteArrayResource getPhoto(int postId, int photoId) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        if (postId < 0 || photoId < 0)
            throw new IllegalArgumentException("Значение postId и photoId не должны быть отрицательными!");
        String photoName = "post-" + postId + "-photo-" + photoId + ".jpg";
        InputStream stream = minioClient.getObject(GetObjectArgs.builder().bucket(bucketName).object(photoName).build());
        byte[] photoData = IOUtils.toByteArray(stream);
        return new ByteArrayResource(photoData);
    }
}
