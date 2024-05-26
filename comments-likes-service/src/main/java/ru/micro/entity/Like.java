package ru.micro.entity;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.List;
import java.util.UUID;

@Table("likes")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Like {
    @Column("post_id")
    @NotNull
    private UUID postId;

    @Column("users_id")
    @NotNull
    private List<Integer> usersId;

    @Override
    public String toString() {
        return "Likes{" +
                "post_id=" + postId +
                ", users_id=" + usersId +
                '}';
    }
}

