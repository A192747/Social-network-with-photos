package ru.micro.services;

import lombok.AllArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import ru.micro.DTO.SnippetCreation;
import ru.micro.DTO.SnippetResponse;
import ru.micro.entities.Post;
import ru.micro.entities.Snippet;
import ru.micro.repository.PostRepository;
import ru.micro.repository.SnippetRepository;

import java.io.IOException;
import java.util.UUID;

@Service
@AllArgsConstructor
public class SnippetService {
    private final PostRepository postRepository;
    private final SnippetRepository snippetRepository;
    public void createAndSaveSnippet(SnippetCreation snippetCreation) throws IOException {
        Document document = Jsoup.connect(snippetCreation.getLink()).timeout(60000).get();
        Elements elements = document.select("link[rel=\"icon\"]");
        String faviconPath;
        String emptyIconPicturePath = "https://cdn1.iconfinder.com/data/icons/iconoir-vol-2/24/empty-page-1024.png";

        if (elements.isEmpty())
            faviconPath = emptyIconPicturePath;
        else {
            faviconPath = elements.first().attr("href");
            if (faviconPath.isEmpty())
                faviconPath = emptyIconPicturePath;
        }

        String titleText;
        elements = document.select("title");
        if (elements.isEmpty())
            titleText = "Template text";
        else {
            titleText = elements.first().text();
            if (titleText.isEmpty())
                titleText = "Template text";
        }

        String previewText = "";
        elements = document.select("p");
        for (Element element: elements) {
            if (!element.text().isEmpty()) {
                previewText = element.text();
                break;
            }
        }
        if (previewText.isEmpty()) {
            elements = document.select("div");
            for (Element element: elements) {
                if (!element.text().isEmpty()) {
                    previewText = element.text();
                    break;
                }
            }
        }
        if (previewText.isEmpty()) {
            elements = document.select("span");
            for (Element element: elements) {
                if (!element.text().isEmpty()) {
                    previewText = element.text();
                    break;
                }
            }
        }
        if (previewText.isEmpty())
            previewText = "Some interesting stuff";

        Snippet snippet = new Snippet();
        snippet.setId(snippetCreation.getPostId());
        snippet.setFavicon(faviconPath);
        snippet.setTitle(titleText);
        snippet.setTextPreview(previewText);
        snippet.setLink(snippetCreation.getLink());

        snippetRepository.save(snippet);
        Post temp = postRepository.findById(snippet.getId()).get();
        temp.setSnippetState(1);
        postRepository.save(temp);
        checkPostReadiness(snippet.getId());
    }

    private void checkPostReadiness(UUID postId) {
        Post post = postRepository.findById(postId).get();
        boolean isReady = (post.getImagesAmount() == 0 && post.getColorPreload() == null)
                || (post.getColorPreload() != null && post.getColorPreload().size() == post.getImagesAmount());
        isReady = isReady && (Math.abs(post.getSnippetState()) == 1);
        if (isReady) {
            post.setPostIsReady(true);
            postRepository.save(post);
        }
    }

    public SnippetResponse get(UUID id) {
        Snippet snippet = snippetRepository.findById(id).get();
        return new SnippetResponse(
                snippet.getFavicon(),
                snippet.getTitle(),
                snippet.getTextPreview(),
                snippet.getLink()
        );
    }
}
