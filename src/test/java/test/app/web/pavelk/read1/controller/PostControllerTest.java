package test.app.web.pavelk.read1.controller;

import app.web.pavelk.read1.Read1;
import app.web.pavelk.read1.dto.PostRequestDto;
import app.web.pavelk.read1.model.Post;
import app.web.pavelk.read1.model.Subreddit;
import app.web.pavelk.read1.model.User;
import app.web.pavelk.read1.repository.CommentRepository;
import app.web.pavelk.read1.repository.PostRepository;
import app.web.pavelk.read1.repository.SubredditRepository;
import app.web.pavelk.read1.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.time.LocalDateTime;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@SpringBootTest(classes = Read1.class)
@AutoConfigureMockMvc(addFilters = false)
class PostControllerTest {

    final String username1 = "createPost1Right";
    final String username2 = "getAllPosts1Right";
    final String username3 = "getPost1Right";
    final String username5 = "getPostsBySubreddit1Right";
    final String username6 = "getPostsByUsername1Right";
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private SubredditRepository subredditRepository;

    @BeforeEach
    private void clearBase() {
        commentRepository.deleteAll();
        postRepository.deleteAll();
        subredditRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = username1)
    void createPost1Right() throws Exception {

        String subredditName = "nameSub1";
        String password1 = "dsd$%#@sdfs";

        User user = userRepository.save(User.builder().created(Instant.now()).email("a@pvhfha.ru")
                .username(username1).password(passwordEncoder.encode(password1)).enabled(true).build());

        Subreddit subreddit = subredditRepository.save(Subreddit.builder()
                .description("d1").name(subredditName).user(user).build());

        PostRequestDto postRequestDto = PostRequestDto.builder().description("op")
                .postName("name1").subReadName(subredditName).build();

        mockMvc.perform(
                post("/api/posts")
                        .content(objectMapper.writeValueAsString(postRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(201));
    }

    @Test
    void createPost2Wrong() throws Exception {

        String subredditName = "nameSub2";

        PostRequestDto postRequestDto = PostRequestDto.builder().description("op")
                .postName("name1").subReadName(subredditName).build();

        mockMvc.perform(
                post("/api/posts")
                        .content(objectMapper.writeValueAsString(postRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(404))
                .andExpect(content().string("The sub is not found " + subredditName));
    }

    @Test
    void createPost3Wrong() throws Exception {

        String subredditName = "nameSub2";

        Subreddit subreddit = subredditRepository.save(Subreddit.builder()
                .description("d1").name(subredditName).user(null).build());

        PostRequestDto postRequestDto = PostRequestDto.builder().description("op")
                .postName("name1").subReadName(subredditName).build();

        mockMvc.perform(
                post("/api/posts")
                        .content(objectMapper.writeValueAsString(postRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(404));

        subredditRepository.delete(subreddit);
    }

    @Test
    @WithMockUser(username = username2)
    void getAllPosts1Right() throws Exception {

        String password1 = "dsd$%#@sdfs";
        Long postId1 = 104l;
        Long postId2 = 105l;

        User user = userRepository.save(User.builder().created(Instant.now()).email("a@pvhfha.ru")
                .username(username2).password(passwordEncoder.encode(password1)).enabled(true).build());

        Subreddit subreddit = subredditRepository.save(Subreddit.builder()
                .description("d1").name("name1").user(user).build());

        Post post1 = postRepository.save(Post.builder().createdDate(LocalDateTime.now())
                .postId(postId1).postName("post1").user(user).description("d1")
                .voteCount(10).subreddit(subreddit).build());

        Post post2 = postRepository.save(Post.builder().createdDate(LocalDateTime.now())
                .postId(postId2).postName("post2").user(user).description("d2")
                .voteCount(20).subreddit(subreddit).build());

        mockMvc.perform(
                get("/api/posts"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].postName", is("post1")))
                .andExpect(jsonPath("$[1].postName", is("post2")))
                .andExpect(jsonPath("$[0].subReadName", is("name1")))
                .andExpect(jsonPath("$[1].subReadName", is("name1")))
                .andExpect(jsonPath("$[0].id", is(post1.getPostId().intValue())))
                .andExpect(jsonPath("$[1].id", is(post2.getPostId().intValue())));
    }

    @Test
    void getAllPosts2Wrong() throws Exception {

        postRepository.save(Post.builder().createdDate(LocalDateTime.now())
                .postId(1l).postName("post2").user(null).description("d2").voteCount(20).subreddit(null).build());

        mockMvc.perform(
                get("/api/posts"))
                .andDo(print())
                .andExpect(status().is(404));
    }

    @Test
    @WithMockUser(username = username3)
    void getPost1Right() throws Exception {

        String password1 = "dsd$%#@sdfs";
        Long postId = 1l;

        User user = userRepository.save(User.builder().created(Instant.now()).email("a@pvhfha.ru")
                .username(username3).password(passwordEncoder.encode(password1)).enabled(true).build());

        Subreddit subreddit = subredditRepository.save(Subreddit.builder()
                .description("d1").name("subReadName1").user(user).build());

        Post post = postRepository.save(Post.builder().createdDate(LocalDateTime.now())
                .postId(postId).postName("post").user(user).description("description1").voteCount(20).subreddit(subreddit).build());

        mockMvc.perform(
                get("/api/posts/" + post.getPostId().intValue()))
                .andDo(print())
                .andExpect(jsonPath("$.postName", is("post")))
                .andExpect(jsonPath("$.description", is("description1")))
                .andExpect(jsonPath("$.id", is(post.getPostId().intValue())))
                .andExpect(jsonPath("$.userName", is(username3)))
                .andExpect(jsonPath("$.subReadName", is("subReadName1")));

    }

    @Test
    void getPost2Wrong() throws Exception {
        Long postId = 1000l;
        mockMvc.perform(
                get("/api/posts/" + postId))
                .andDo(print())
                .andExpect(status().is(404))
                .andExpect(content().string("Post not found id " + postId));
    }

    @Test
    @WithMockUser(username = username5)
    void getPostsBySubreddit1Right() throws Exception {

        String subredditName = "getPostsBySubreddit1Right";
        String password1 = "dsd$%#@sdfs";
        Long postId = 1l;
        Long subredditId = 1l;

        User user = userRepository.save(User.builder().created(Instant.now()).email("a@pvhfha.ru")
                .username(username5).password(passwordEncoder.encode(password1)).enabled(true).build());

        Subreddit subreddit = subredditRepository.save(Subreddit.builder().id(subredditId)
                .description("d1").name(subredditName).user(user).build());

        Post post = postRepository.save(Post.builder().createdDate(LocalDateTime.now())
                .postId(postId).postName("post").user(user).description("description1").voteCount(20).subreddit(subreddit).build());

        mockMvc.perform(
                get("/api/posts/by-subreddit/" + subreddit.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].postName", is("post")));
    }

    @Test
    void getPostsBySubreddit2Wrong() throws Exception {

        int subredditId = 151555;
        mockMvc.perform(
                get("/api/posts/by-subreddit/" + subredditId))
                .andDo(print())
                .andExpect(status().is(404))
                .andExpect(content().string("Subreddit not id " + subredditId));

    }

    @Test
    @WithMockUser(username = username6)
    void getPostsByUsername1Right() throws Exception {

        String subredditName = "getPostsByUsername1RightS";
        String password1 = "asdasdasd";
        Long postId = 1l;
        Long subredditId = 1l;

        User user = userRepository.save(User.builder().created(Instant.now()).email("a@pvhfha.ru")
                .username(username6).password(passwordEncoder.encode(password1)).enabled(true).build());

        Subreddit subreddit = subredditRepository.save(Subreddit.builder().id(subredditId)
                .description("d1").name(subredditName).user(user).build());

        Post post = postRepository.save(Post.builder().createdDate(LocalDateTime.now())
                .postId(postId).postName("getPostsByUsername1RightP").user(user).description("getPostsByUsername1RightD").voteCount(20).subreddit(subreddit).build());

        mockMvc.perform(
                get("/api/posts/by-user/" + username6))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].postName", is("getPostsByUsername1RightP")))
                .andExpect(jsonPath("$[0].description", is("getPostsByUsername1RightD")));
    }

    @Test
    void getPostsByUsername2Wrong() throws Exception {
        String username7 = "getPostsByUsername2Wrong";
        mockMvc.perform(
                get("/api/posts/by-user/" + username7))
                .andDo(print())
                .andExpect(status().is(404))
                .andExpect(content().string("Username Not Found " + username7));
    }
}
