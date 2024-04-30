package org.oril.dao;

import lombok.AllArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.oril.exceptions.NotValidException;
import org.oril.models.Roles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.oril.models.User;


@Component
@AllArgsConstructor
public class UserDAO {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    public User save(User user) {
        user.setRole(Roles.USER);
        jdbcTemplate.update("insert into users (password, name, role) VALUES (?,?,?)",
                user.getPassword(),
                user.getName(),
                user.getRole().toString());
        user.setId(jdbcTemplate.queryForObject(
                "select id from users where name=? limit 1",
                new Object[]{user.getName()},
                Integer.class));
        return user;
    }
    public User login(User user) {
        String passwordHash = jdbcTemplate.queryForObject(
                "select password from users where name=? limit 1",
                new Object[]{user.getName()},
                String.class);
        if (BCrypt.checkpw(user.getPassword(), passwordHash)) {
            User fullUser = jdbcTemplate.queryForObject(
                    "select * from users where name=? limit 1",
                    new Object[]{user.getName()},
                    new UserRowMapper());
            return fullUser;
        }
        throw new NotValidException("Неверный логин или пароль!");
    }


}
