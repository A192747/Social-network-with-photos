package ru.micro.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.micro.dto.PostResponse;
import ru.micro.entity.Post;
import ru.micro.service.RecommendPostService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/posts")
public class RecommendationsController {
    @Autowired
    RecommendPostService recommendPostService;
    @GetMapping("/recommendations")
    public List<PostResponse> getRecommendations(@RequestHeader("id") int userId){
        return convertToResponseList(recommendPostService.getRecommendations(userId));
    }
    @PutMapping("/{postId}")
    public void updateRecomListForId(@RequestHeader("id") int userId,
                                     @PathVariable("postId") UUID postId) {
        recommendPostService.updateRecommendation(userId, postId);
    }
    private PostResponse convertToResponse(Post post) {
        return new PostResponse(
                post.getId(),
                post.getUserId(),
                recommendPostService.getAuthorName(post.getUserId()),
                post.getText(),
                post.getCreatedAt(),
                post.getColorPreload(),
                post.getImagesAmount(),
                post.getLikesCounter(),
                post.getSnippetState()
        );
    }

    private List<PostResponse> convertToResponseList(List<Post> list) {
        List<PostResponse> postResponses = new ArrayList<>();
        for (Post post: list) {
            try {
                postResponses.add(convertToResponse(post));
            } catch (Exception ex) {
                System.out.println(new Date(System.currentTimeMillis()) +
                        " Не удалось добавить пост в Response. PostId=" +
                        post.getId());
            }
        }
        return postResponses;
    }
}
