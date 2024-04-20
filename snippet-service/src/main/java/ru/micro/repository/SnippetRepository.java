package ru.micro.repository;

import org.springframework.data.cassandra.repository.CassandraRepository;
import ru.micro.entities.Snippet;

public interface SnippetRepository extends CassandraRepository<Snippet, Integer> {
}
