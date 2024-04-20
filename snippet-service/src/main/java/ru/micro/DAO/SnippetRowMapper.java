package ru.micro.DAO;

import ru.micro.entities.Snippet;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SnippetRowMapper implements RowMapper<Snippet> {
    @Override
    public Snippet mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Snippet snippet = new Snippet();
        snippet.setId             (resultSet.getInt("id"));
        snippet.setFavicon        (resultSet.getString("favicon"));
        snippet.setTitle          (resultSet.getString("title"));
        snippet.setTextPreview    (resultSet.getString("text_preview"));
        snippet.setLink           (resultSet.getString("link"));
        return snippet;
    }
}
