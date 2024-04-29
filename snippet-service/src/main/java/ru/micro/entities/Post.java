package ru.micro.entities;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.Set;
import java.util.UUID;

@Table("posts")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Post {
    @PrimaryKey
    private UUID id;

    @Column("user_id")
    @NotNull
    private String userId;

    @NotNull
    @Size(min = 1)
    @NotEmpty(message = "Текст не может быть пустым!")
    private String text;

    @Column("color_preload")
    @NotNull
    @NotEmpty(message = "Цвет не может быть пустым!")
    private Set<String> colorPreload;

    @Column("images_amount")
    @NotNull
    private Integer imagesAmount;

    @Column("likes_counter")
    @NotNull
    private Integer likesCounter;

    @Column("snippet_state")
    @NotNull
    private Integer snippetState;

    @Column("post_is_ready")
    @NotNull
    private Boolean postIsReady;
}

