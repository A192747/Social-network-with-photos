package ru.micro.services;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import ru.micro.DTO.PostUploadObject;
import ru.micro.DTO.SnippetCreation;
import ru.micro.entities.Post;
import ru.micro.repository.PostRepository;

import java.sql.Timestamp;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@AllArgsConstructor
public class PostUploadService {
    @Autowired
    private final RestTemplate restTemplate;
    @Autowired
    private final PostRepository postRepository;
    @Transactional(transactionManager = "transactionManager")
    public UUID createAndSavePost(PostUploadObject object, int id) {
        Post post = new Post();
        post.setId(UUID.randomUUID());
        post.setUserId(id);
        post.setText(object.getText());
        post.setImagesAmount(object.getImagesAmount());
        post.setColorPreload(null);
        post.setPostIsReady(false);
        post.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        post.setLikesCounter(0);

        Pattern pattern = Pattern.compile("(http|ftp|https)://([\\w_-]+(?:\\.[\\w_-]+)+)([\\w.,@?^=%&:/~+#-]*[\\w@?^=%&/~+#-])");
        Matcher matcher = pattern.matcher(object.getText());

        boolean hasLink = matcher.find();

        if (hasLink)
            post.setSnippetState(0);
        else
            post.setSnippetState(-1);

        UUID postID = postRepository.save(post).getId();

        if (hasLink) {
            SnippetCreation request = new SnippetCreation(postID, object.getText().substring(matcher.start(), matcher.end()));
            restTemplate.postForObject("http://snippet-service/snippets", request, Object.class);
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("id", String.valueOf(id));
            HttpEntity<Object> entity = new HttpEntity<>(null, headers); // Передаем null, так как тело запроса не требуется
            restTemplate.put("http://recommendation-service/posts/" + postID, entity);
        } catch (Exception ex) {
            System.out.println(new Date(System.currentTimeMillis()) +
                    " Не удалось обновить рекомендации пользователей при добавлении поста " + postID + "\n" +
                    ex.getMessage());
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("id", String.valueOf(id));
            HttpEntity<Object> entity = new HttpEntity<>(null, headers); // Передаем null, так как тело запроса не требуется
            restTemplate.put("http://friend-post-service/posts/" + postID, entity);
        } catch (Exception ex) {
            System.out.println(new Date(System.currentTimeMillis()) +
                    " Не удалось обновить ленту друзей пользователей при добавлении поста " + postID + "\n" +
                    ex.getMessage());
        }

        checkPostReadiness(postID);
        return postID;
    }

    private void checkPostReadiness(UUID postId) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new NoSuchElementException("Такой пост не существует!")
        );
        boolean isReady = (post.getImagesAmount() == 0 && post.getColorPreload() == null)
                || (post.getColorPreload() != null && post.getColorPreload().size() == post.getImagesAmount());
        isReady = isReady && (Math.abs(post.getSnippetState()) == 1);
        if (isReady) {
            post.setPostIsReady(true);
            postRepository.save(post);
        }
    }

}
