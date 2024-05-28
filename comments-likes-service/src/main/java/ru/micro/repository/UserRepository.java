package ru.micro.repository;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.micro.entity.User;

import java.util.UUID;

@Repository
public interface UserRepository extends Neo4jRepository<User, UUID> {
    @Query("MATCH (u:User {userId: $userId}) return u")
    User findByUserId(@Param("userId") int userId);
}
