package org.oril.dao;

import org.oril.models.Roles;
import org.oril.models.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRowMapper implements RowMapper<User> {
    @Override
<<<<<<< HEAD
    public UserVO mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        UserVO userVO = new UserVO();
        userVO.setId(resultSet.getInt("id"));
        userVO.setName(resultSet.getString("name"));
        userVO.setPassword(resultSet.getString("password"));
        userVO.setRole(Roles.valueOf(resultSet.getString("role")));
        return userVO;
=======
    public User mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        User user = new User();
        user.setId(resultSet.getInt("id"));
        user.setName(resultSet.getString("name"));
        user.setPassword(resultSet.getString("password"));
        user.setRole(Roles.valueOf(resultSet.getString("role")));
        return user;
>>>>>>> d9ac06fb3bd24589862d651040e8f7669dc2f7a5
    }
}
