package ru.micro.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@AllArgsConstructor
@Getter
@Setter
public class UserResponse {
    private Integer userId;
    private String name;
    private Integer countOfFollowers;
}
