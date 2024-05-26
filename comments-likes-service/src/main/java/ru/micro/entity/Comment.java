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
import java.util.UUID;

@Table("comments")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    @PrimaryKey
    @Column("id")
    private UUID id;

    @Column("post_id")
    @NotNull
    private UUID postId;

    @Column("user_id")
    @NotNull
    private Integer userId;

    @Column("text")
    @NotNull
    @Size(min = 1)
    @NotEmpty(message = "Комментарий не может быть пустым!")
    private String text;

    @Column("created_at")
    @NotNull
    @NotEmpty(message = "Дата должна быть указана")
    private Timestamp createdAt;

    @Override
    public String toString() {
        return "Comments{" +
                "id=" + id +
                ", post_id=" + postId +
                ", user_id=" + userId +
                ", text=" + text +
                '}';
    }
}

