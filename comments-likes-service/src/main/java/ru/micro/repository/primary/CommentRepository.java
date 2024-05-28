package ru.micro.repository.primary;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;
import ru.micro.entity.Comment;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Repository
public interface CommentRepository extends CassandraRepository<Comment, UUID> {
    @Query("SELECT * FROM comments WHERE post_id =?0")
    List<Comment> findCommentsPost(UUID postId);

    @Query("INSERT INTO comments (id, post_id, user_id, text, created_at) VALUES (?0, ?1, ?2, ?3, ?4)")
    void createCommentPost(UUID commentId, UUID postId, Integer userId,
                           String text, Timestamp date);

    @Query("DELETE FROM comments WHERE id =?0")
    void removeCommentPost(UUID commentId);

    @Query("SELECT user_id FROM comments WHERE id =?0")
    int findOwnerComment(UUID commentId);
}
