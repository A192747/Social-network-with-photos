package ru.micro.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.micro.dto.FriendPostResponse;
import ru.micro.entity.Post;
import ru.micro.service.FriendlyPostService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/posts")
public class FriendPostController {
    @Autowired
    FriendlyPostService friendlyPostService;

    @GetMapping("/friends")
    public List<FriendPostResponse> getFriendPost(@RequestHeader("id") int userId) {
        return convertToResponseList(friendlyPostService.getFriendPost(userId));
    }

    @PutMapping("/{postId}")
    public void updateFriendPostListForId(@RequestHeader("id") int userId, @PathVariable("postId") UUID postId) {
        friendlyPostService.updateFriendPost(userId, postId);
    }

    private FriendPostResponse convertToResponse(Post post) {
        return new FriendPostResponse(
                post.getId(),
                post.getUserId(),
                friendlyPostService.getAuthorName(post.getUserId()),
                post.getText(),
                post.getCreatedAt(),
                post.getColorPreload(),
                post.getImagesAmount(),
                post.getLikesCounter(),
                post.getSnippetState()
        );
    }

    private List<FriendPostResponse> convertToResponseList(List<Post> list) {
        List<FriendPostResponse> postResponses = new ArrayList<>();
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
