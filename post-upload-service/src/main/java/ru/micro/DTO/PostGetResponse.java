package ru.micro.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostGetResponse {
    @NotNull
    private String text;

    @NotNull
    private Set<String> colorPreload;
}
