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

import java.util.Set;
import java.util.UUID;

@Table("friendpost")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FriendPost {
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

    @Column("friend_post_taken_count")
    @NotNull
    @NotEmpty
    private int friendlyPostTakenCount;

    @Column("new_posts_ids")
    @NotNull
    @Size(min = 1)
    @NotEmpty(message = "Лист постов не может быть пустым")
    private Set<UUID> newPostsIds;

    @Override
    public String toString() {
        return "FriendPost{" +
                "id=" + id +
                ", userId=" + userId +
                ", postsIds=" + postsIds +
                ", friendly_post_taken_count=" + friendlyPostTakenCount +
                '}';
    }
}
