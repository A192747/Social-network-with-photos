package ru.micro.services;

import lombok.AllArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.micro.DAO.PostDAO;
import ru.micro.DAO.SnippetDAO;
import ru.micro.DTO.SnippetCreation;
import ru.micro.DTO.SnippetResponse;
import ru.micro.entities.Post;
import ru.micro.entities.Snippet;

import java.io.IOException;
import java.util.UUID;

@Service
@AllArgsConstructor
public class SnippetService {
    private final PostDAO postDAO;
    private final SnippetDAO snippetDAO;
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

        snippetDAO.save(snippet);
        Post temp = postDAO.get(snippet.getId());
        temp.setSnippetState(1);
        postDAO.save(temp);
        checkPostReadiness(snippet.getId());
    }

    private void checkPostReadiness(UUID postId) {
        Post post = postDAO.get(postId);
        boolean isReady = (post.getImagesAmount() == 0 && post.getColorPreload() == null)
                || (post.getColorPreload() != null && post.getColorPreload().size() == post.getImagesAmount());
        isReady = isReady && (Math.abs(post.getSnippetState()) == 1);
        if (isReady) {
            post.setPostIsReady(true);
            postDAO.save(post);
        }
    }

    public SnippetResponse get(int id) {
        Snippet snippet = snippetDAO.get(id);
        return new SnippetResponse(
                snippet.getFavicon(),
                snippet.getTitle(),
                snippet.getTextPreview(),
                snippet.getLink()
        );
    }
}
