package ru.micro;

import io.minio.MinioClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.multipart.MultipartFile;
import ru.micro.DAO.PostDAO;
import ru.micro.exceptions.NotValidException;
import ru.micro.service.FileService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;

import static org.junit.Assert.*;

@SpringBootTest
class PhotoUploadServiceTests {

    @Autowired
    private FileService fileService;

    @MockBean
    private MinioClient minioClient;

    @MockBean
    private PostDAO postDAO;

    @BeforeEach
    public void setUp() {
        // Настройка моков перед каждым тестом
    }

    @Test
    public void testGetPhotoWithNegativePhotoId() {
        // Arrange
        UUID postId = UUID.randomUUID();
        int photoId = -1;
        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> fileService.getPhoto(postId, photoId));
    }
    @Test
    public void testSavePhotoWithMaxPhotosReached() throws Exception {
        // Arrange
        UUID postId = UUID.randomUUID();
        int photoId = 0;
        MultipartFile file = Mockito.mock(MultipartFile.class);
        Mockito.when(file.getBytes()).thenReturn(new byte[]{1, 2, 3});
        Post post = new Post();
        post.setImagesAmount(1);
        post.setColorPreload(new ArrayList<>(Collections.nCopies(post.getImagesAmount(), "#000000")));
        Mockito.when(postDAO.get(postId)).thenReturn(post);

        // Act and Assert
        assertThrows(NotValidException.class, () -> fileService.save(postId, photoId, file));
    }

}
