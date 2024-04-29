package ru.micro.repository;

import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.cassandra.repository.CassandraRepository;
import reactor.core.publisher.Flux;
import ru.micro.entities.Post;

import java.util.UUID;

public interface PostRepository extends CassandraRepository<Post, UUID> {
}
