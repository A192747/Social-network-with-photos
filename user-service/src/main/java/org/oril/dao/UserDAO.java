package org.oril.dao;

import lombok.AllArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.oril.entities.AuthRequest;
import org.oril.entities.NotValidException;
import org.oril.entities.Roles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.oril.entities.UserVO;


@Component
@AllArgsConstructor
public class UserDAO {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    public UserVO save(AuthRequest user) {
        UserVO userVO = new UserVO();
        userVO.setName(user.getName());
        userVO.setPassword(user.getPassword());
        userVO.setRole(Roles.USER);
        jdbcTemplate.update("insert into users (password, name, role) VALUES (?,?,?)",
                userVO.getPassword(),
                userVO.getName(),
                userVO.getRole().toString());
        return userVO;
    }
    public UserVO login(AuthRequest userVO) {
        String passwordHash = jdbcTemplate.queryForObject(
                "select password from users where name=? limit 1",
                new Object[]{userVO.getName()},
                String.class);
        if (BCrypt.checkpw(userVO.getPassword(), passwordHash)) {
            UserVO user = jdbcTemplate.queryForObject(
                    "select * from users where name=? limit 1",
                    new Object[]{userVO.getName()},
                    new UserRowMapper());
            return user;
        }

        throw new NotValidException("Не верный логин или пароль!");
    }


}
