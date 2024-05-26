package ru.micro.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.micro.entity.Post;
import ru.micro.entity.FriendPost;
import ru.micro.repository.PostRepository;
import ru.micro.repository.FriendPostRepository;
import ru.micro.repository.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FriendlyPostService {
    @Autowired
    PostRepository postRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    FriendPostRepository friendPostRepository;

    private Set<UUID> update(int userId, FriendPost rec) {
        Set<Post> posts;

        if (rec.getPostsIds() == null) {
            rec.setFriendlyPostTakenCount(1);
        }
        posts = updateForUser(userId, rec);

        if (posts.isEmpty()) {
            rec.setPostsIds(null);
            return update(userId, rec);
        }

        return posts.stream().map(Post::getId).collect(Collectors.toSet());
    }
    @Transactional(transactionManager = "transactionManager")
    public List<Post> getFriendPost(int userId) {
        FriendPost friendPost = friendPostRepository.findByUserId(userId);
        Set<Post> posts;

        if (friendPost == null) {
            friendPost = new FriendPost();
            friendPost.setId(UUID.randomUUID());
            friendPost.setUserId(userId);
            friendPost.setFriendlyPostTakenCount(1);
            friendPost.setPostsIds(update(userId, friendPost));
        }

        if (friendPost.getFriendlyPostTakenCount() > 1) {
            friendPost.setPostsIds(update(userId, friendPost));
        }

        Set<UUID> recPostsIds = friendPost.getNewPostsIds();
        if (recPostsIds == null) {
            recPostsIds = new HashSet<>();
        }
        if (friendPost.getPostsIds() != null) {
            recPostsIds.addAll(friendPost.getPostsIds());
        }
        posts = getPostsArray(recPostsIds);
        friendPost.setNewPostsIds(null);

        friendPost.setFriendlyPostTakenCount(friendPost.getFriendlyPostTakenCount() + 1);
        friendPostRepository.save(friendPost);

        return posts.stream().filter(Objects::nonNull).toList();
    }

    public String getAuthorName(int userId) {
        return userRepository.findByUserId(userId).getName();
    }

    protected Set<Post> getPostsArray(Set<UUID> listIds) {
        Set<Post> posts = new HashSet<>();

        for (UUID id : listIds) {
            try {
                posts.add(postRepository.findById(id).get());
            } catch (Exception ex) {
                System.out.println("post not found: " +
                        new Date(System.currentTimeMillis()) + " " +
                        ex.getMessage());
            }
        }

        Map<Integer, Long> userIdCounts = posts.stream()
                .collect(Collectors.groupingBy(Post::getUserId, Collectors.counting()));
        List<Post> postsTemp = new ArrayList<>(posts.stream().filter(Objects::nonNull).toList());

        for (int i = posts.size() - 1; i >= 0; i--) {
            Post post = postsTemp.get(i);
            Integer userId = post.getUserId();
            if (userIdCounts.get(userId) > 1) {
                userIdCounts.put(userId, userIdCounts.get(userId) - 1);
                postsTemp.remove(post);
            }
        }

        Set<Post> readyPosts = postsTemp.stream()
                .filter(Post::getPostIsReady)
                .collect(Collectors.toSet());

        return readyPosts.stream()
                .limit(100)
                .collect(Collectors.toSet());
    }

    private List<UUID> deletePostFromUserId(int userId, Set<UUID> postsIds) {
        Set<Post> posts = getPostsArray(postsIds);
        posts.removeIf(post -> post != null && post.getUserId() == userId);
        return posts.stream().map(Post::getId).toList();
    }

    public void updateFriendPost(int userId, UUID postId) {
        List<Integer> usersIds = userRepository.findFollowers(userId);
        List<UUID> postList;

        for (Integer user: usersIds) {
            FriendPost rec = friendPostRepository.findByUserId(user);
            if (rec != null) {
                if (rec.getNewPostsIds() == null) {
                    postList = new LinkedList<>();
                } else {
                    postList = new LinkedList<>(deletePostFromUserId(userId, rec.getNewPostsIds()));
                    if (postList.size() == 100) {
                        postList.removeLast();
                    }
                }
                postList.addFirst(postId);
                rec.setNewPostsIds(new HashSet<>(postList));
            } else {
                rec = new FriendPost();
                rec.setId(UUID.randomUUID());
                rec.setUserId(user);
                rec.setNewPostsIds(new HashSet<>(Collections.singleton(postId)));
            }
            friendPostRepository.save(rec);
        }
    }

    @Scheduled(fixedRate = 86400000)
    public void updateFriendPostForAllUsers() {
        Iterable<Integer> users = userRepository.findAllUsers();
        for (Integer userId: users) {
            FriendPost rec = friendPostRepository.findByUserId(userId);
            if (rec == null) {
                rec = new FriendPost();
                rec.setId(UUID.randomUUID());
                rec.setUserId(userId);
                rec.setNewPostsIds(null);
            }
            rec.setFriendlyPostTakenCount(1);
            Set<UUID> postIds = updateForUser(userId, rec)
                    .stream()
                    .filter(Objects::nonNull)
                    .map(Post::getId)
                    .collect(Collectors.toSet());
            rec.setPostsIds(postIds);
            friendPostRepository.save(rec);
        }
        System.out.println(new Date(System.currentTimeMillis()) + " Посты друзей обновлены для всех пользователей");
    }


    private Set<Post> updateForUser(int userId, FriendPost rec) {
        Set<Post> posts = new HashSet<>();
        Set<Integer> friendsUsersIds = new HashSet<>(userRepository.findFollowing(userId));
        List<Integer> friendsUsersList = new ArrayList<>(friendsUsersIds);
        Collections.shuffle(friendsUsersList);
        Set<Integer> friendsUsersIdsList = new HashSet<>(friendsUsersList);
        List<Post> post;
        int countOfRequests = rec.getFriendlyPostTakenCount();
        Set<UUID> friendsPosts = rec.getNewPostsIds();

        if (friendsPosts != null) {
            posts.addAll(getPostsArray(friendsPosts));
            rec.setNewPostsIds(null);
        }

        for (Integer friendUserId: friendsUsersIdsList) {
            post = postRepository.findFirstByUserIdOrderByCreatedAtDesc(friendUserId, countOfRequests);
            if (post != null && !post.isEmpty() && countOfRequests <= post.size()) {
                for (int i = post.size() - 1; i >= 0; i--) {
                    Post element = post.get(i);
                    if (element != null) {
                        posts.add(element);
                        break;
                    }
                }
            }
            if (posts.size() == 100) {
                break;
            }
        }

        return posts;
    }
}
