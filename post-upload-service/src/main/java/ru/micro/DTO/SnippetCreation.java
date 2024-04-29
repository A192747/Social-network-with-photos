package ru.micro.DTO;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SnippetCreation {
    @NotNull
    private UUID postId;

    @NotNull
    @Size(min = 1)
    @NotEmpty(message = "Ссылка на сайт для сниппета оказалась пустой!")
    private String link;
}
