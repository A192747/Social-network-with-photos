package ru.micro;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import ru.micro.dto.UserResponse;
import ru.micro.entity.User;
import ru.micro.exceptions.NotValidException;
import ru.micro.repository.UserRepository;
import ru.micro.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class FriendServiceTests {

    private User initUser(Integer userId, String name, int count) {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setUserId(userId);
        user.setName(name);
        user.setCountOfFollowers(count);
        System.out.println(user);
        return user;
    }
    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetFollowers() {
        // Mock data
        List<User> followers = new ArrayList<>();
        followers.add(initUser(1, "Follower1", 10));
        followers.add(initUser(2, "Follower2", 20));

        when(userRepository.findFollowers(1)).thenReturn(followers);

        List<UserResponse> userResponses = userService.getFollowers(1);

        assertEquals(2, userResponses.size());
        assertEquals("Follower1", userResponses.get(0).getName());
        assertEquals("Follower2", userResponses.get(1).getName());
    }

    @Test
    void testGetFollowing() {
        // Mock data
        List<User> following = new ArrayList<>();
        following.add(initUser(3, "Following1", 15));
        following.add(initUser(4, "Following2", 25));

        when(userRepository.findFollowing(1)).thenReturn(following);

        List<UserResponse> userResponses = userService.getFollowing(1);

        assertEquals(2, userResponses.size());
        assertEquals("Following1", userResponses.get(0).getName());
        assertEquals("Following2", userResponses.get(1).getName());
    }

    @Test
    void testUnFollow() {
        // Mock data
        List<User> followers = new ArrayList<>();
        followers.add(initUser(1, "Follower1", 10));
        followers.add(initUser(2, "Follower2", 20));

        when(userRepository.findFollowers(2)).thenReturn(followers);

        userService.unFollow(2, 1);

    }

    @Test
    void testAddFollowing() {
        // Mock data
        List<User> followers = new ArrayList<>();
        followers.add(initUser(1, "Follower1", 10));
        followers.add(initUser(2, "Follower2", 20));

        when(userRepository.findFollowers(2)).thenReturn(followers);

        assertThrows(NotValidException.class, () -> userService.addFollowing(2, 2));

        assertThrows(NotValidException.class, () -> userService.addFollowing(1, 1));

        userService.addFollowing(2, 1);

    }

    @Test
    void testFindByNameContaining() {
        // Mock data
        List<User> users = new ArrayList<>();
        users.add(initUser(1, "User1", 5));
        users.add(initUser(2, "User2", 10));

        when(userRepository.findByNameContaining("User")).thenReturn(users);

        List<UserResponse> userResponses = userService.findByNameContaining("User");

        assertEquals(2, userResponses.size());
        assertEquals("User1", userResponses.get(0).getName());
        assertEquals("User2", userResponses.get(1).getName());
    }
}
