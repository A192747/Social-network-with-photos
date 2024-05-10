package org.oril.service;

import org.modelmapper.ModelMapper;
import org.oril.dao.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.oril.dto.UserDTO;
import org.oril.models.User;
import org.oril.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Date;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserDAO userDAO;
    @Autowired
    public ModelMapper modelMapper;
    @Autowired
    private RestTemplate restTemplate;

    @Transactional(transactionManager = "transactionManager")
    public User save(UserDTO userDTO) {
        User user = userDAO.save(convertToUser(userDTO));
        userRepository.save(convertToUser(user));
        //для только что зарегистрированного пользователя создадим рекомендации:
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("id", String.valueOf(user.getId()));
            HttpEntity<Object> entity = new HttpEntity<>(null, headers); // Передаем null, так как тело запроса не требуется
            restTemplate.exchange(
                    "http://recommendation-service/posts/recommendations",
                    HttpMethod.GET,
                    entity,
                    String.class
            );
        } catch (Exception ex) {
            System.out.println("Не удалось создать рекомендации для пользователя " +
                    new Date(System.currentTimeMillis()) + " " +
                    ex.getMessage());
            //skip
        }
        return user;
    }

    public User login(UserDTO userDTO) {
        return userDAO.login(convertToUser(userDTO));
    }

    private User convertToUser(UserDTO user) {
        return modelMapper.map(user, User.class);
    }
    private org.oril.entity.User convertToUser(User user) {
        org.oril.entity.User usr = new org.oril.entity.User();
        usr.setUserId(user.getId());
        usr.setName(user.getName());
        usr.setCountOfFollowers(0);
        return usr;
    }

}
