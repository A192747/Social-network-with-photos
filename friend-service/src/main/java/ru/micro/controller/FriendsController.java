package ru.micro.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.micro.dto.UserResponse;
import ru.micro.entity.User;
import ru.micro.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/users")
public class FriendsController {
    @Autowired
    private UserService service;
    @GetMapping("/followers")
    public List<UserResponse> getFollowers(@RequestHeader("id") int userId){
        return service.getFollowers(userId);
    }
    @GetMapping("/following")
    public List<UserResponse> getFollowing(@RequestHeader("id") int userId){
        return service.getFollowing(userId);
    }

    @GetMapping("/search")
    public List<UserResponse> searchUsersByName(@RequestParam("name") String name) {
        return service.findByNameContaining(name);
    }
    @PostMapping("/{followerId}/follow")
    public void followToFollowId(@RequestHeader("id") int userId, @PathVariable("followerId") int followId) {
        service.addFollowing(userId, followId);
    }

    @DeleteMapping("/{followerId}/follow")
    public void unFollow(@RequestHeader("id") int userId, @PathVariable("followerId") int followId) {
        service.unFollow(userId, followId);
    }
}
