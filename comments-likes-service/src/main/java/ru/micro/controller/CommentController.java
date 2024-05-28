package ru.micro.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.micro.dto.CommentResponse;
import ru.micro.entity.Comment;
import ru.micro.service.CommentLikeService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/posts")
public class CommentController {
    @Autowired
    CommentLikeService commentLikeService;

    @GetMapping("/{postId}/comments")
    public List<CommentResponse> getCommentsPost(@PathVariable("postId") UUID postId) {
        return convertToResponseList(commentLikeService.getCommentsPost(postId));
    }

    @PostMapping("/{postId}/comments")
    public void createCommentPost(@RequestHeader("id") int userId, @PathVariable("postId") UUID postId,
                                @RequestBody @Valid String text) {
        commentLikeService.createCommentPost(postId, userId, text);
    }

    @DeleteMapping("/{postId}/comments")
    public void removeCommentPost(@RequestHeader("id") int userId, @PathVariable("postId") UUID postId,
                                  @RequestBody @Valid UUID commentId) {
        commentLikeService.removeCommentPost(userId, postId, commentId);
    }

    private CommentResponse convertToResponse(Comment comment) {
        return new CommentResponse(
                comment.getId(),
                comment.getPostId(),
                comment.getUserId(),
                commentLikeService.getAuthorName(comment.getUserId()),
                comment.getText(),
                comment.getCreatedAt()
        );
    }

    private List<CommentResponse> convertToResponseList(List<Comment> list) {
        List<CommentResponse> commentResponses = new ArrayList<>();
        for (Comment comment: list) {
            try {
                commentResponses.add(convertToResponse(comment));
            } catch (Exception ex) {
                System.out.println(new Date(System.currentTimeMillis()) +
                        " Не удалось добавить комментарий в Response. CommentId=" +
                        comment.getId());
            }
        }
        return commentResponses;
    }
}
