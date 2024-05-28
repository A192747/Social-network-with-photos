package ru.micro.repository.primary;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;
import ru.micro.entity.Like;

import java.util.List;
import java.util.UUID;

@Repository
public interface LikeRepository extends CassandraRepository<Like, UUID> {
    @Query("SELECT users_id FROM likes WHERE post_id =?0")
    List<Integer> findUsersLikePost(UUID postId);

    @Query("SELECT COUNT(*) <> 0 FROM likes WHERE post_id =?0")
    Boolean findFieldPost(UUID postId);

    @Query("INSERT INTO likes (post_id, users_id) VALUES (?0, [?1]])")
    void createFieldPost(UUID postId, Integer userId);

    @Query("UPDATE likes SET users_id = users_id + [?1] WHERE post_id =?0")
    void createLikePost(UUID postId, Integer userId);

    @Query("DELETE users_id[?1] FROM likes WHERE post_id =?0")
    void removeLikePost(UUID postId, Integer userId);
}
