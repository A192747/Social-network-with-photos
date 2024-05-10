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

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Table("recommendations")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Recommendations {
    @PrimaryKey
    @Column("id")
    private UUID id;

    @Column("user_id")
    private int userId;

    @Column("posts_ids")
    @NotNull
    @Size(min = 1)
    @NotEmpty(message = "Лист постов не может быть пустым")
    private Set<UUID> postsIds;

    @Column("recommend_taken_count")
    @NotNull
    @NotEmpty
    private int recommendTakenCount;

    //посты, которые только что подгрузили пользователи, и нам их нужно порекомендовать
    @Column("new_posts_ids")
    @NotNull
    @Size(min = 1)
    @NotEmpty(message = "Лист постов не может быть пустым")
    private Set<UUID> newPostsIds;

    @Override
    public String toString() {
        return "Recommendations{" +
                "id=" + id +
                ", userId=" + userId +
                ", postsIds=" + postsIds +
                ", recommend_taken_count=" + recommendTakenCount +
                '}';
    }
}
