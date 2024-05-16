package ru.micro.repository;

import org.springframework.data.cassandra.repository.CassandraRepository;
import ru.micro.entities.Snippet;

import java.util.UUID;

public interface SnippetRepository extends CassandraRepository<Snippet, UUID> {
}
