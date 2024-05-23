package ru.micro.repository;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;
import ru.micro.entity.FriendPost;

import java.util.UUID;

@Repository
public interface FriendPostRepository extends CassandraRepository<FriendPost, UUID> {
    @Query("SELECT * FROM friendpost WHERE user_id =?0 ALLOW FILTERING")
    FriendPost findByUserId(int userId);
}
