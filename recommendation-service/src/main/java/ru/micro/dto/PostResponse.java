package ru.micro.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@Getter
@Setter
public class PostResponse {
    private UUID id;
    private Integer userId;
    private String userName;
    private String text;
    private Timestamp date;
    private List<String> colorPreload;
    private Integer imagesAmount;
    private Integer likesCounter;
    private Integer snippetState;
}
