package ru.micro.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.micro.entity.Post;
import ru.micro.entity.Recommendations;
import ru.micro.entity.User;
import ru.micro.repository.PostRepository;
import ru.micro.repository.RecommendationsRepository;
import ru.micro.repository.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecommendPostService {
    @Autowired
    PostRepository postRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RecommendationsRepository recommendationsRepository;

    private Set<UUID> update(int userId, Recommendations rec) {
        Set<Post> posts;
        if(rec.getPostsIds() == null)
            rec.setRecommendTakenCount(1);
        posts = updateForUser(userId, rec);

        //При окончании ленты рекомендаций обнуляем
        //счетчик и начинаем показывать посты, которые уже показывали
        if (posts == null || posts.isEmpty()) {
            rec.setPostsIds(null);
            return update(userId, rec);
        }

        return posts.stream().map(Post::getId).collect(Collectors.toSet());
    }
    @Transactional(transactionManager = "transactionManager")
    public List<Post> getRecommendations(int userId) {
        Recommendations recommendations = recommendationsRepository.findByUserId(userId);
        Set<Post> posts;
        if(recommendations == null) {
            recommendations = new Recommendations();
            recommendations.setId(UUID.randomUUID());
            recommendations.setRecommendTakenCount(1);
            recommendations.setUserId(userId);
            recommendations.setPostsIds(update(userId, recommendations));
        }
        if (recommendations.getRecommendTakenCount() > 1) {
            recommendations.setPostsIds(update(userId, recommendations));
        }
        //добавляем отложенные посты
        Set<UUID> recPostsIds = recommendations.getNewPostsIds();
        if(recPostsIds == null)
            recPostsIds = new HashSet<>();
        recPostsIds.addAll(recommendations.getPostsIds());
        posts = getPostsArray(recPostsIds);
        recommendations.setNewPostsIds(null);

        recommendations.setRecommendTakenCount(recommendations.getRecommendTakenCount() + 1);
        recommendationsRepository.save(recommendations);
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
                //skip
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

        // Ограничиваем количество возвращаемых постов до 100
        Set<Post> limitedPosts = readyPosts.stream()
                .limit(100)
                .collect(Collectors.toSet());

        return limitedPosts;
    }



    private List<UUID> deletePostFromUserId(int userId, Set<UUID> postsIds) {
        Set<Post> posts = getPostsArray(postsIds);
        for (Post post: posts) {
            if (post != null && post.getUserId() == userId)
                posts.remove(post);
        }
        return posts.stream().map(Post::getId).toList();
    }

    public void updateRecommendation(int userId, UUID postId) {
        List<Integer> usersIds = userRepository.findPossibleFollowers(userId);
        List<UUID> postList = null;
        for(Integer user: usersIds) {
            Recommendations rec = recommendationsRepository.findByUserId(user);
            if(rec != null) {
                if(rec.getNewPostsIds() == null) {
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
                rec = new Recommendations();
                rec.setId(UUID.randomUUID());
                rec.setUserId(user);
                rec.setNewPostsIds(new HashSet<>(Collections.singleton(postId)));
            }
            recommendationsRepository.save(rec);
        }
    }

    //обновляем для всех пользователей рекомендации автоматически раз в 24 часа
    @Scheduled(fixedRate = 86400000)
    public void updateRecommendationsForAllUsers() {
        Iterable<Integer> users = userRepository.findAllUsers();
        for (Integer userId: users) {
            Recommendations rec = recommendationsRepository.findByUserId(userId);
            if(rec == null) {
                rec = new Recommendations();
                rec.setId(UUID.randomUUID());
                rec.setUserId(userId);
                rec.setNewPostsIds(null);
            }
            rec.setRecommendTakenCount(1);
            Set<UUID> postIds = updateForUser(userId, rec)
                    .stream()
                    .filter(Objects::nonNull)
                    .map(Post::getId)
                    .collect(Collectors.toSet());
            rec.setPostsIds(postIds);
            recommendationsRepository.save(rec);
        }
        System.out.println(new Date(System.currentTimeMillis()) + " Рекомендации обновлены для всех пользователей");
    }


    private Set<Post> updateForUser(int userId, Recommendations rec) {
        Set<Post> posts = new HashSet<>();
        Set<Integer> recommendUsersIds = new HashSet<>(userRepository.findPossibleSubscriptions(userId));
        Set<Integer> mostPopularUsersIds = new HashSet<>(userRepository.getMostPopularUsers(100 - recommendUsersIds.size()));
        List<Integer> recommendUsersList = new ArrayList<>(recommendUsersIds);
        Collections.shuffle(recommendUsersList);
        Set<Integer> recommendUsersIdsList = new HashSet<>(recommendUsersList);
        recommendUsersIdsList.addAll(mostPopularUsersIds);
        if(recommendUsersIdsList.contains(Integer.valueOf(userId)))
            recommendUsersIdsList.remove(Integer.valueOf(userId));
        List<Post> post = null;
        Integer countOfRequests = rec.getRecommendTakenCount();
        Set<UUID> recommendedPosts = rec.getNewPostsIds();
        if(recommendedPosts != null) {
            posts.addAll(getPostsArray(recommendedPosts));
            rec.setNewPostsIds(null);
        }
        for (Integer recommendUserId: recommendUsersIdsList) {
            post = postRepository.findFirstByUserIdOrderByCreatedAtDesc(recommendUserId, countOfRequests);
            if (post != null && !post.isEmpty()) {
                //Чтобы с обновлением ленты появлялись более старые посты
                //чтобы не добавлять уже показанные посты
                if(countOfRequests <= post.size()) {
                    for (int i = post.size() - 1; i >= 0; i--) {
                        Post element = post.get(i);
                        if (element != null) {
                            posts.add(element);
                            break;
                        }
                    }
                }
            }
            if(posts.size() == 100)
                break;
        }
        return posts;
    }

}
