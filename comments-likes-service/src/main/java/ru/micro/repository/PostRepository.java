package ru.micro.repository;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;
import ru.micro.entity.Post;

import java.util.UUID;

@Repository
public interface PostRepository extends CassandraRepository<Post, UUID> {
    @Query("SELECT COUNT(*) <> 0 FROM posts WHERE id =?0")
    Boolean findPost(UUID postId);

    @Query("SELECT user_id FROM posts WHERE id =?0")
    Integer findOwnerPost(UUID postId);
}
