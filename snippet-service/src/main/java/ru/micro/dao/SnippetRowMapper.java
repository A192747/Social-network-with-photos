package ru.micro.dao;

import ru.micro.entities.SnippetVO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SnippetRowMapper implements RowMapper<SnippetVO> {
    @Override
    public SnippetVO mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        SnippetVO snippetVO = new SnippetVO();
        snippetVO.setId             (resultSet.getInt("id"));
        snippetVO.setFavicon        (resultSet.getString("favicon"));
        snippetVO.setTitle          (resultSet.getString("title"));
        snippetVO.setTextPreview    (resultSet.getString("text_preview"));
        snippetVO.setLink           (resultSet.getString("link"));
        return snippetVO;
    }
}
