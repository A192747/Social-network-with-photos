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
    @Query("MATCH (u:User {userId: $userId}), (f:User {userId: $followerId}) " +
            "CREATE (u)-[:FOLLOWS]->(f)")
    void addFollower(@Param("userId") int userId, @Param("followerId") int followerId);

    @Query("MATCH (u:User {userId: $userId})<-[:FOLLOWS]-(f:User) " +
            "RETURN f")
    List<User> findFollowers(@Param("userId") int userId);

    @Query("MATCH (u:User {userId: $userId}) return u")
    User findByUserId(@Param("userId") int userId);
    
//    @Query("MATCH (u:User)<-[:FOLLOWS]-(f:User) " +
//            "WITH u, COUNT(f) AS followerCount " +
//            "RETURN u " +
//            "ORDER BY followerCount DESC " +
//            "LIMIT 10")
//    List<User> getMostPopularUsers();

    @Query("MATCH (u:User) WHERE u.name CONTAINS $userName RETURN u")
    List<User> findByNameContaining(@Param("userName") String userName);

//    @Query("MATCH (u:User {userId: $userId})-[:FOLLOWS]->(f1:User)-[:FOLLOWS]->(r1:User) " +
//            "WHERE r1.userId <> $userId return r1 as User LIMIT 10 " +
//            "Union  " +
//            "MATCH (u:User {userId: $userId})-[:FOLLOWS]->(f0:User)<-[:FOLLOWS]-(r0:User) " +
//            "WHERE r0.userId <> $userId return r0 as User  LIMIT 10 ")
//    List<User> findPossibleSubscriptions(@Param("userId") int userId);

    @Query("MATCH (u:User {userId: $userId})-[:FOLLOWS]->(f:User) " +
            "RETURN f")
    List<User> findFollowing(@Param("userId") int userId);

    @Query("MATCH (u:User {userId: $userId})-[r:FOLLOWS]->(f:User {userId: $followerId}) " +
            "DELETE r")
    void unFollowUser(@Param("userId") int userId, @Param("followerId") int followerId);

    @Query("MATCH (u:User {userId: $userId}) SET u.countOfFollowers = u.countOfFollowers + 1 return u")
    void incrementCountOfFollowers(@Param("userId") int userId);

    @Query("MATCH (u:User {userId: $userId}) SET u.countOfFollowers = u.countOfFollowers - 1 return u")
    void decrementCountOfFollowers(@Param("userId") int userId);
}
