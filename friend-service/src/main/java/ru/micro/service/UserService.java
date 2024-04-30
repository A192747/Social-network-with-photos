package ru.micro.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.micro.dto.UserResponse;
import ru.micro.entity.User;
import ru.micro.exceptions.NotValidException;
import ru.micro.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    public List<UserResponse> getFollowers(int userId) {
        return convertToResponseList(userRepository.findFollowers(userId));
    }

    public List<UserResponse> getFollowing(int userId) {
        return convertToResponseList(userRepository.findFollowing(userId));
    }


    public void unFollow(int userId, int followingId) {
        List<User> following = userRepository.findFollowers(followingId);
        List<Integer> listIds = following.stream()
                .map(User::getUserId)
                .toList();
        if(listIds.contains(userId)) {
            userRepository.unFollowUser(userId, followingId);
            userRepository.decrementCountOfFollowers(followingId);
        }
    }

    public void addFollowing(int userId, int followingId) {
        if (userId == followingId)
            throw new NotValidException("Пользователь не может быть подписан сам на себя");
        List<Integer> listIds = userRepository.findFollowers(followingId).stream()
                .map(User::getUserId)
                .toList();
        if (listIds.contains(userId))
            throw new NotValidException("Пользователь не может быть подписан на одного и того же человека несколько раз");
        userRepository.addFollower(userId, followingId);
        userRepository.incrementCountOfFollowers(followingId);
    }

    public List<UserResponse> findByNameContaining(String name) {
        return convertToResponseList(userRepository.findByNameContaining(name));
    }

    private UserResponse convertToResponse(User user) {
        UserResponse usr = new UserResponse(user.getUserId(), user.getName(), user.getCountOfFollowers());
        return usr;
    }
    private List<UserResponse> convertToResponseList(List<User> list) {
        return list.stream().map(e -> convertToResponse(e)).toList();
    }
}
