package ru.micro.repository;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;
import ru.micro.entity.Post;

import java.util.List;
import java.util.UUID;

@Repository
public interface PostRepository extends CassandraRepository<Post, UUID> {
    @Query("SELECT * FROM posts WHERE user_id =?0 and post_is_ready = true LIMIT ?1 ALLOW FILTERING")
    List<Post> findFirstByUserIdOrderByCreatedAtDesc(int userId, int count);
}
