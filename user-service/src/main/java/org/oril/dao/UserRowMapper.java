package org.oril.dao;

import org.oril.entities.Roles;
import org.oril.entities.UserVO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRowMapper implements RowMapper<UserVO> {
    @Override
    public UserVO mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        UserVO userVo = new UserVO();
        userVo.setId(resultSet.getInt("id"));
        userVo.setName(resultSet.getString("name"));
        userVo.setPassword(resultSet.getString("password"));
        userVo.setRole(Roles.valueOf(resultSet.getString("role")));
        return userVo;
    }
}
