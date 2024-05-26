package ru.micro.entity;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Table("posts")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Post {
    @PrimaryKey
    @Column("id")
    private UUID id;

    @Column("user_id")
    @NotNull
    private Integer userId;

    @Column("text")
    @NotNull
    @Size(min = 1)
    @NotEmpty(message = "Текст не может быть пустым!")
    private String text;

    @Column("created_at")
    @NotNull
    @NotEmpty(message = "Дата должна быть указана")
    private Timestamp createdAt;

    @Column("color_preload")
    @NotNull
    @Size(min = 1)
    @NotEmpty(message = "Цвет не может быть пустым!")
    private List<String> colorPreload;

    @Column("images_amount")
    @NotNull
    private Integer imagesAmount;

    @Column("snippet_state")
    @NotNull
    private Integer snippetState;

    @Column("post_is_ready")
    @NotNull
    private Boolean postIsReady;
}

