package ru.micro.DAO;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.micro.entities.Post;
import ru.micro.repository.PostRepository;
import ru.micro.exceptions.NotValidException;

import java.util.UUID;

@Component
@AllArgsConstructor
public class PostDAO {
    @Autowired
    PostRepository postRepository;
    public UUID save(Post post) {
        postRepository.insert(post);
        return post.getId();
    }

    public Post get(UUID id) {
        return postRepository.findById(id).orElseThrow(
                () -> new NotValidException("Такой пост не существует!")
        );
    }
}