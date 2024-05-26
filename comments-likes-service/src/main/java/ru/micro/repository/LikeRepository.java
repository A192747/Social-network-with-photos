package ru.micro.repository;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;
import ru.micro.entity.Like;

import java.util.List;
import java.util.UUID;

@Repository
public interface LikeRepository extends CassandraRepository<Like, UUID> {
    @Query("SELECT users_id FROM likes WHERE owner_id =?0 and post_id =?1")
    List<Integer> findUsersLikePost(int ownerId, int postId);
}
