package ru.micro.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.micro.dto.LikeResponse;
import ru.micro.service.CommentLikeService;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/posts")
public class LikeController {
    @Autowired
    CommentLikeService commentLikeService;

    @GetMapping("/{postId}/likes")
    public LikeResponse getLikePost(@RequestHeader("id") int userId, @PathVariable("postId") UUID postId) {
        List<Integer> likes = commentLikeService.getLikesPost(postId);
        return new LikeResponse(likes.size(), likes.contains(userId));
    }

    @PostMapping("/{postId}/likes")
    public void createLikePost(@RequestHeader("id") int userId, @PathVariable("postId") UUID postId) {
        commentLikeService.createLikePost(postId, userId);
    }

    @DeleteMapping("/{postId}/likes")
    public void removeLikePost(@RequestHeader("id") int userId, @PathVariable("postId") UUID postId) {
        commentLikeService.removeLikePost(postId, userId);
    }
}
