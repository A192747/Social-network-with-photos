package ru.micro.services;

import lombok.AllArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.micro.dao.SnippetDAO;
import ru.micro.entities.SnippetCreation;
import ru.micro.entities.SnippetResponse;
import ru.micro.entities.SnippetVO;

import java.io.IOException;

@Service
@AllArgsConstructor
public class SnippetService {
    //Add relation to posts database when it's ready. Should provide id of snippet to the post
    private final RestTemplate restTemplate;
    private final SnippetDAO snippetDAO;
    public void createAndSaveSnippet(SnippetCreation snippet) throws IOException {
        Document document = Jsoup.connect(String.valueOf(snippet.getLink())).timeout(60).get();
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

        String previewText;
        elements = document.select("p");
        if (elements.isEmpty())
            previewText = "Some interesting stuff";
        else {
            previewText = elements.first().text();
            if (previewText.isEmpty())
                previewText = "Some interesting stuff";
        }

        SnippetVO snippetVO = new SnippetVO();
        snippetVO.setFavicon(faviconPath);
        snippetVO.setTitle(titleText);
        snippetVO.setTextPreview(previewText);
        snippetVO.setLink(snippet.getLink());

        snippetDAO.save(snippetVO);
    }

    public SnippetResponse get(int id) {
        SnippetVO snippetVO = snippetDAO.get(id);
        return new SnippetResponse(
                snippetVO.getFavicon(),
                snippetVO.getTitle(),
                snippetVO.getTextPreview(),
                snippetVO.getLink()
        );
    }
}
