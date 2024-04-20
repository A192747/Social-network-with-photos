package ru.micro.dao;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.micro.entities.SnippetVO;

@Component
@AllArgsConstructor
public class SnippetDAO {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    public int save(SnippetVO snippetVO) {
        jdbcTemplate.update("insert into snippets (favicon, title, text_preview, link) VALUES (?,?,?,?)",
                snippetVO.getFavicon(),
                snippetVO.getTitle(),
                snippetVO.getTextPreview(),
                snippetVO.getLink());
        return snippetVO.getId();
    }

    public SnippetVO get(int id) {
        return jdbcTemplate.queryForObject(
                "SELECT * FROM snippets WHERE id = ? LIMIT 1",
                new Object[]{id},
                new SnippetRowMapper());
    }
}