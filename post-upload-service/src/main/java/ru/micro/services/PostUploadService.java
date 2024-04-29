package ru.micro.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.micro.DAO.PostDAO;
import ru.micro.DTO.PostGetResponse;
import ru.micro.DTO.PostUploadObject;
import ru.micro.DTO.SnippetCreation;
import ru.micro.entities.Post;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@AllArgsConstructor
public class PostUploadService {
    private final RestTemplate restTemplate;
    private final PostDAO postDAO;
    public void createAndSavePost(PostUploadObject object, String id) {
        Post post = new Post();
        post.setId(UUID.randomUUID());
        post.setUserId(id);
        post.setText(object.getText());
        post.setImagesAmount(object.getImagesAmount());
        post.setColorPreload(new HashSet<>());
        post.setPostIsReady(false);
        post.setLikesCounter(0);

        Pattern pattern = Pattern.compile("(http|ftp|https)://([\\w_-]+(?:\\.[\\w_-]+)+)([\\w.,@?^=%&:/~+#-]*[\\w@?^=%&/~+#-])");
        Matcher matcher = pattern.matcher(object.getText());

        boolean hasLink = matcher.find();

        if (hasLink)
            post.setSnippetState(0);
        else
            post.setSnippetState(-1);

        UUID postID = postDAO.save(post);

        if (hasLink) {
            SnippetCreation request = new SnippetCreation(postID, object.getText().substring(matcher.start(), matcher.end()));
            restTemplate.postForObject("http://snippet-service/snippets", request, Object.class);
        }

        checkPostReadiness(postID);
    }

    private void checkPostReadiness(UUID postId) {
        Post post = postDAO.get(postId);
        boolean isReady = post.getColorPreload() == null || post.getColorPreload().size() == post.getImagesAmount();
        isReady = isReady && (post.getSnippetState() == 1 || post.getSnippetState() == -1);
        if (isReady) {
            post.setPostIsReady(true);
            postDAO.save(post);
        }
    }

    public PostGetResponse get(UUID id) {
        Post post = postDAO.get(id);
        return new PostGetResponse(
                post.getText(),
                post.getColorPreload()
        );
    }
}
