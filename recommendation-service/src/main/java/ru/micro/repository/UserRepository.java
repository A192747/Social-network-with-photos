package ru.micro.repository;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.micro.entity.User;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserRepository extends Neo4jRepository<User, UUID> {
    @Query("MATCH (u:User) RETURN u.userId")
    Iterable<Integer> findAllUsers();

    @Query("MATCH (u:User {userId: $userId})<-[:FOLLOWS]-(f:User) " +
            "RETURN f")
    List<User> findFollowers(@Param("userId") int userId);

    @Query("MATCH (u:User {userId: $userId}) return u")
    User findByUserId(@Param("userId") int userId);
    
    @Query("MATCH (u:User)<-[:FOLLOWS]-(f:User) " +
            "WITH u, COUNT(f) AS followerCount " +
            "RETURN u.userId " +
            "ORDER BY followerCount DESC " +
            "LIMIT $count ")
    List<Integer> getMostPopularUsers(@Param("count") int count);

    @Query("MATCH (u:User {userId: $userId})-[:FOLLOWS]->(f1:User)-[:FOLLOWS]->(r1:User) " +
            "WHERE r1.userId <> u.userId AND r1.userId <> f1.userId AND f1.userId <> u.userId " +
            "RETURN r1.userId as User " +
            "LIMIT 3")
    List<Integer> findPossibleSubscriptions(@Param("userId") int userId);

    @Query("MATCH (u:User)-[:FOLLOWS]->(f1:User)-[:FOLLOWS]->(r1:User {userId: $userId}) " +
            "WHERE r1.userId <> u.userId AND r1.userId <> f1.userId AND f1.userId <> u.userId " +
            "RETURN u.userId as User " +
            "LIMIT 3")
    List<Integer> findPossibleFollowers(@Param("userId") int userId);

    @Query("MATCH (u:User {userId: $userId})-[:FOLLOWS]->(f:User) " +
            "RETURN f")
    List<User> findFollowing(@Param("userId") int userId);

}
