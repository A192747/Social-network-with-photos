package org.oril.entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.*;

import java.util.List;
import java.util.UUID;

@Node("User")
@Setter
@Getter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(generatorClass = GeneratedValue.UUIDGenerator.class)
    private UUID id;
    @Property("userId")
    private Integer userId;
    @Property("name")
    private String name;
    @Property("countOfFollowers")
    private Integer countOfFollowers;

    /// Отношение FOLLOWS для подписок
    @Relationship(type = "FOLLOWS", direction = Relationship.Direction.OUTGOING)
    private List<User> following;

    @Relationship(type = "FOLLOWS", direction = Relationship.Direction.INCOMING)
    private List<User> followers;
}
