package org.oril.dao;

import org.oril.entities.Roles;
import org.oril.entities.UserVO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRowMapper implements RowMapper<UserVO> {
    @Override
    public UserVO mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        UserVO userVO = new UserVO();
        userVO.setId(resultSet.getInt("id"));
        userVO.setName(resultSet.getString("name"));
        userVO.setPassword(resultSet.getString("password"));
        userVO.setRole(Roles.valueOf(resultSet.getString("role")));
        return userVO;
    }
}
