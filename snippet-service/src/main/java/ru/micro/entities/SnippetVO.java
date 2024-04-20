package ru.micro.entities;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SnippetVO {

    private int id;
    @NotNull
    @Size(min = 1)
    @NotEmpty(message = "Картинка должна существовать!")
    private String favicon;
    @NotNull
    @Size(min = 1)
    @NotEmpty(message = "Заголовок не может быть пустым!")
    private String title;
    @NotNull
    @Size(min = 1)
    @NotEmpty(message = "Текст не может быть пустым!")
    private String textPreview;
    @NotNull
    @Size(min = 1)
    @NotEmpty(message = "Ссылка не может быть пустой!")
    private String link;

}

