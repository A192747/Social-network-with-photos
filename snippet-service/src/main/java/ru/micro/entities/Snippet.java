package ru.micro.entities;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Table("snippets")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Snippet {
    @PrimaryKey
    private int id;
    @NotNull
    @Size(min = 1)
    @NotEmpty(message = "Картинка должна существовать!")
    private String favicon;
    @NotNull
    @Size(min = 1)
    @NotEmpty(message = "Заголовок не может быть пустым!")
    private String title;
    @Column("text_preview")
    @NotNull
    @Size(min = 1)
    @NotEmpty(message = "Текст не может быть пустым!")
    private String textPreview;
    @NotNull
    @Size(min = 1)
    @NotEmpty(message = "Ссылка не может быть пустой!")
    private String link;

}

