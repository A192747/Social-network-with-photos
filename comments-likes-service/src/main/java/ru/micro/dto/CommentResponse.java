package ru.micro.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.UUID;

@Data
@AllArgsConstructor
@Getter
@Setter
public class CommentResponse {
    private UUID id;
    private UUID postId;
    private Integer userId;
    private String userName;
    private String text;
    private Timestamp createdAt;
}
