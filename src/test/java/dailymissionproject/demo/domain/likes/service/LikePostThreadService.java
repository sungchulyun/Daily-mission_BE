package dailymissionproject.demo.domain.likes.service;

import dailymissionproject.demo.domain.like.repository.LikesRepository;
import dailymissionproject.demo.domain.like.service.LikesService;
import dailymissionproject.demo.domain.mission.repository.Mission;
import dailymissionproject.demo.domain.mission.repository.MissionRepository;
import dailymissionproject.demo.domain.missionRule.repository.MissionRule;
import dailymissionproject.demo.domain.missionRule.repository.Week;
import dailymissionproject.demo.domain.post.repository.Post;
import dailymissionproject.demo.domain.post.repository.PostRepository;
import dailymissionproject.demo.domain.user.repository.Role;
import dailymissionproject.demo.domain.user.repository.User;
import dailymissionproject.demo.domain.user.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceUnit;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
@ActiveProfiles("unit-test")
public class LikePostThreadService {

    @Autowired
    private LikesService likesService;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private LikesRepository likesRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MissionRepository missionRepository;

    @PersistenceContext
    private EntityManager em;
    @PersistenceUnit
    private EntityManagerFactory emf;

    @Test
    @DisplayName("동시에 4명이 좋아요를 누를 때")
    void likesRequestSameTime() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(200);

        CountDownLatch countDownLatch = new CountDownLatch(4);

        User user1 = User.builder()
                .username("test1")
                .email("test1@gmail.com")
                .name("tester1")
                .role(Role.USER)
                .build();

        userRepository.save(user1);

        User user2 = User.builder()
                .username("test2")
                .email("test2@gmail.com")
                .name("tester2")
                .role(Role.USER)
                .build();

        userRepository.save(user2);

        User user3 = User.builder()
                .username("test3")
                .email("test3@gmail.com")
                .name("tester3")
                .role(Role.USER)
                .build();

        userRepository.save(user3);

        User user4 = User.builder()
                .username("test4")
                .email("test4@gmail.com")
                .name("tester4")
                .role(Role.USER)
                .build();

        userRepository.save(user4);

        Mission mission_1 = Mission.builder()
                .title("title1")
                .content("content1")
                .missionRule(MissionRule.builder()
                        .week(new Week(true, true, false, false, false, false, false))
                        .build())
                .startDate(LocalDate.now().plusDays(1))
                .endDate(LocalDate.now().plusMonths(2))
                .user(user1)
                .build();

        missionRepository.save(mission_1);

        Post post_1 = Post.builder()
                .title("title1")
                .content("content1")
                .mission(mission_1)
                .user(user1)
                .build();

        postRepository.save(post_1);

        System.out.println("------------------------------------------");

        executorService.execute(() -> {
            try {
                likesService.likePost(post_1.getId(), user1.getId());
            } finally {
                countDownLatch.countDown();
            }
        });

        executorService.execute(() -> {
            try {
                likesService.likePost(post_1.getId(), user2.getId());
            } finally {
                countDownLatch.countDown();
            }
        });

        executorService.execute(() -> {
            try {
                likesService.likePost(post_1.getId(), user3.getId());
            } finally {
                countDownLatch.countDown();
            }
        });

        executorService.execute(() -> {
            try {
                likesService.likePost(post_1.getId(), user4.getId());
            } finally {
                countDownLatch.countDown();
            }
        });

        countDownLatch.await();

        Post findPost = postRepository.findById(post_1.getId()).get();
        Assertions.assertThat(findPost.getLikeCount()).isEqualTo(4);
    }

    @Test
    @DisplayName("2명은 좋아요 취소를 나머지 2명은 좋아요를")
    void likeRequestWithCancel() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(200);

        CountDownLatch countDownLatch = new CountDownLatch(4);

        User user1 = User.builder()
                .username("test1")
                .email("test1@gmail.com")
                .name("tester1")
                .role(Role.USER)
                .build();

        userRepository.save(user1);

        User user2 = User.builder()
                .username("test2")
                .email("test2@gmail.com")
                .name("tester2")
                .role(Role.USER)
                .build();

        userRepository.save(user2);

        User user3 = User.builder()
                .username("test3")
                .email("test3@gmail.com")
                .name("tester3")
                .role(Role.USER)
                .build();

        userRepository.save(user3);

        User user4 = User.builder()
                .username("test4")
                .email("test4@gmail.com")
                .name("tester4")
                .role(Role.USER)
                .build();

        userRepository.save(user4);

        Mission mission_1 = Mission.builder()
                .title("title1")
                .content("content1")
                .missionRule(MissionRule.builder()
                        .week(new Week(true, true, false, false, false, false, false))
                        .build())
                .startDate(LocalDate.now().plusDays(1))
                .endDate(LocalDate.now().plusMonths(2))
                .user(user1)
                .build();

        missionRepository.save(mission_1);

        Post post_1 = Post.builder()
                .title("title1")
                .content("content1")
                .mission(mission_1)
                .user(user1)
                .build();

        postRepository.save(post_1);

        System.out.println("------------------------------------------");

        executorService.execute(() -> {
            try {
                likesService.likePost(post_1.getId(), user2.getId());
            } finally {
                countDownLatch.countDown();
            }
        });

        executorService.execute(() -> {
            try {
                likesService.likePost(post_1.getId(), user1.getId());
            } finally {
                countDownLatch.countDown();
            }
        });

        executorService.execute(() -> {
            try {
                likesService.likePost(post_1.getId(), user3.getId());
            } finally {
                countDownLatch.countDown();
            }
        });

        executorService.execute(() -> {
            try {
                likesService.likePost(post_1.getId(), user4.getId());
            } finally {
                countDownLatch.countDown();
            }
        });

        countDownLatch.await();

        Post findPost = postRepository.findById(post_1.getId()).get();
        Assertions.assertThat(findPost.getLikeCount()).isEqualTo(4);
    }
}
