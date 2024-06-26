package ru.micro.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@Getter
@Setter
public class LikeResponse {
    private Integer countOfLikes;
    private Boolean liked;
}
