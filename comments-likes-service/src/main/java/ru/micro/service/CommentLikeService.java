package ru.micro.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.micro.entity.Comment;
import ru.micro.exceptions.NotValidException;
import ru.micro.repository.PostRepository;
import ru.micro.repository.CommentRepository;
import ru.micro.repository.LikeRepository;
import ru.micro.repository.UserRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class CommentLikeService {
    @Autowired
    PostRepository postRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    LikeRepository likeRepository;

    @Transactional(transactionManager = "transactionManager")
    public List<Comment> getCommentsPost(UUID postId) {
        if (postRepository.findPost(postId)) {
            return commentRepository.findCommentsPost(postId);
        } else {
            throw new NotValidException("Пост не существует!");
        }
    }

    @Transactional(transactionManager = "transactionManager")
    public void createCommentPost(UUID postId, Integer userId, String text) {
        if (postRepository.findPost(postId)) {
            if (!text.isEmpty()) {
                commentRepository.createCommentPost(UUID.randomUUID(), postId, userId,
                        text, Timestamp.valueOf(LocalDateTime.now()));
            } else {
                throw new NotValidException("Должен быть введён текст!");
            }
        } else {
            throw new NotValidException("Пост не существует!");
        }
    }

    @Transactional(transactionManager = "transactionManager")
    public void removeCommentPost(int userId, UUID postId, UUID commentId) {
        if (postRepository.findPost(postId)) {
            if (postRepository.findOwnerPost(postId) == userId ||
                    commentRepository.findOwnerComment(commentId) == userId) {
                commentRepository.removeCommentPost(commentId);
            } else {
                throw new NotValidException("Вы не можете удалить этот комментарий!");
            }
        } else {
            throw new NotValidException("Пост не существует!");
        }
    }

    public String getAuthorName(int userId) {
        return userRepository.findByUserId(userId).getName();
    }
}
