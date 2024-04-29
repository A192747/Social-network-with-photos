package ru.micro.DAO;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.micro.entities.Snippet;
import ru.micro.repository.SnippetRepository;
import ru.micro.util.NotValidException;

import java.util.UUID;

@Component
@AllArgsConstructor
public class SnippetDAO {
    @Autowired
    SnippetRepository snippetRepository;
    public UUID save(Snippet snippet) {
        snippetRepository.insert(snippet);
        return snippet.getId();
    }

    public Snippet get(int id) {
        return snippetRepository.findById(id).orElseThrow(
                () -> new NotValidException("Такой сниппет не существует!")
        );
    }
}