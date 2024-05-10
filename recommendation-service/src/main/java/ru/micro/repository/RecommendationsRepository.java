package ru.micro.repository;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;
import ru.micro.entity.Post;
import ru.micro.entity.Recommendations;

import java.util.UUID;

@Repository
public interface RecommendationsRepository extends CassandraRepository<Recommendations, UUID> {
    @Query("SELECT * FROM recommendations WHERE user_id =?0 ALLOW FILTERING")
    Recommendations findByUserId(int userId);
}
