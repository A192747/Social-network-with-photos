package org.oril.service;

import org.modelmapper.ModelMapper;
import org.oril.dao.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.oril.dto.UserDTO;
import org.oril.models.User;
import org.oril.repository.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserDAO userDAO;
    @Autowired
    public ModelMapper modelMapper;

    public User save(UserDTO userDTO) {
        User user = userDAO.save(convertToUser(userDTO));
        userRepository.save(convertToUser(user));
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
        System.out.println(user);
        usr.setUserId(user.getId());
        usr.setName(user.getName());
        usr.setCountOfFollowers(0);
        return usr;
    }

}
