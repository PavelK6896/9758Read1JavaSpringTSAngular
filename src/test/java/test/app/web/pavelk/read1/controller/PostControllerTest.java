package test.app.web.pavelk.read1.controller;

import app.web.pavelk.read1.Read1;
import app.web.pavelk.read1.dto.PostRequest;
import app.web.pavelk.read1.model.Post;
import app.web.pavelk.read1.model.Subreddit;
import app.web.pavelk.read1.model.User;
import app.web.pavelk.read1.repository.CommentRepository;
import app.web.pavelk.read1.repository.PostRepository;
import app.web.pavelk.read1.repository.SubredditRepository;
import app.web.pavelk.read1.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = Read1.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
public class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private SubredditRepository subredditRepository;

    final String username1 = "createPost1Right";

    @Test
    @WithMockUser(username = username1)
    public void createPost1Right() throws Exception {

        Long postId = 101l;
        String subredditName = "nameSub1";
        String password1 = "dsd$%#@sdfs";

        User user = userRepository.save(User.builder().created(Instant.now()).email("a@pvhfha.ru")
                .username(username1).password(passwordEncoder.encode(password1)).enabled(true).build());

        Subreddit subreddit = subredditRepository.save(Subreddit.builder()
                .description("d1").name(subredditName).user(user).build());

        PostRequest postRequest = PostRequest.builder().description("op")
                .postId(postId).postName("name1").subredditName(subredditName).url("url").build();

        mockMvc.perform(
                post("/api/posts/")
                        .content(objectMapper.writeValueAsString(postRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(201));

        assertThat(postRepository.findById(postId)).isEmpty();
    }


    @Test
    public void createPost2Wrong() throws Exception {

        Long postId = 102l;
        String subredditName = "nameSub2";

        PostRequest postRequest = PostRequest.builder().description("op")
                .postId(postId).postName("name1").subredditName(subredditName).url("url").build();

        mockMvc.perform(
                post("/api/posts/")
                        .content(objectMapper.writeValueAsString(postRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(404))
                .andExpect(content().string("The division is not found " + subredditName));
    }

    @Test
    public void createPost3Wrong() throws Exception {

        Long postId = 103l;
        String subredditName = "nameSub2";

        Subreddit subreddit = subredditRepository.save(Subreddit.builder()
                .description("d1").name(subredditName).user(null).build());

        PostRequest postRequest = PostRequest.builder().description("op")
                .postId(postId).postName("name1").subredditName(subredditName).url("url").build();

        mockMvc.perform(
                post("/api/posts/")
                        .content(objectMapper.writeValueAsString(postRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(404));

        subredditRepository.delete(subreddit);

    }

    final String username2 = "getAllPosts1Right";

    @Test
    @WithMockUser(username = username2)
    public void getAllPosts1Right() throws Exception {

        commentRepository.deleteAll();
        postRepository.deleteAll();
        subredditRepository.deleteAll();

        String password1 = "dsd$%#@sdfs";
        Long postId1 = 104l;
        Long postId2 = 105l;

        User user = userRepository.save(User.builder().created(Instant.now()).email("a@pvhfha.ru")
                .username(username2).password(passwordEncoder.encode(password1)).enabled(true).build());

        Subreddit subreddit = subredditRepository.save(Subreddit.builder()
                .description("d1").name("name1").user(user).build());

        Post post1 = postRepository.save(Post.builder().createdDate(Instant.now())
                .postId(postId1).postName("post1").user(user).url("url1").description("d1")
                .voteCount(10).subreddit(subreddit).build());

        Post post2 = postRepository.save(Post.builder().createdDate(Instant.now())
                .postId(postId2).postName("post2").user(user).description("d2")
                .voteCount(20).subreddit(subreddit).build());

        mockMvc.perform(
                get("/api/posts/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].postName", is("post1")))
                .andExpect(jsonPath("$[1].postName", is("post2")))
                .andExpect(jsonPath("$[0].subredditName", is("name1")))
                .andExpect(jsonPath("$[1].subredditName", is("name1")))
                .andExpect(jsonPath("$[0].id", is(post1.getPostId().intValue())))
                .andExpect(jsonPath("$[1].id", is(post2.getPostId().intValue())));
    }

    @Test
    public void getAllPosts2Wrong() throws Exception {

        postRepository.save(Post.builder().createdDate(Instant.now())
                .postId(1l).postName("post2").user(null).description("d2").voteCount(20).subreddit(null).build());

        mockMvc.perform(
                get("/api/posts/"))
                .andDo(print())
                .andExpect(status().is(404));
    }

    final String username3 = "getPost1Right";

    @Test
    @WithMockUser(username = username3)
    public void getPost1Right() throws Exception {

        String password1 = "dsd$%#@sdfs";
        Long postId = 1l;

        User user = userRepository.save(User.builder().created(Instant.now()).email("a@pvhfha.ru")
                .username(username3).password(passwordEncoder.encode(password1)).enabled(true).build());

        Subreddit subreddit = subredditRepository.save(Subreddit.builder()
                .description("d1").name("subreddit1").user(user).build());

        Post post = postRepository.save(Post.builder().createdDate(Instant.now())
                .postId(postId).postName("post").user(user).description("description1").voteCount(20).subreddit(subreddit).build());

        mockMvc.perform(
                get("/api/posts/" + post.getPostId().intValue()))
                .andDo(print())
                .andExpect(jsonPath("$.postName", is("post")))
                .andExpect(jsonPath("$.description", is("description1")))
                .andExpect(jsonPath("$.id", is(post.getPostId().intValue())))
                .andExpect(jsonPath("$.userName", is(username3)))
                .andExpect(jsonPath("$.subredditName", is("subreddit1")));

    }

    @Test
    public void getPost2Wrong() throws Exception {
        Long postId = 1000l;
        mockMvc.perform(
                get("/api/posts/" + postId))
                .andDo(print())
                .andExpect(status().is(404))
                .andExpect(content().string("Post not found id " + postId));
    }


    final String username5 = "getPostsBySubreddit1Right";

    @Test
    @WithMockUser(username = username5)
    public void getPostsBySubreddit1Right() throws Exception {

        String subredditName = "getPostsBySubreddit1Right";
        String password1 = "dsd$%#@sdfs";
        Long postId = 1l;
        Long subredditId = 1l;

        User user = userRepository.save(User.builder().created(Instant.now()).email("a@pvhfha.ru")
                .username(username5).password(passwordEncoder.encode(password1)).enabled(true).build());

        Subreddit subreddit = subredditRepository.save(Subreddit.builder().id(subredditId)
                .description("d1").name(subredditName).user(user).build());

        Post post = postRepository.save(Post.builder().createdDate(Instant.now())
                .postId(postId).postName("post").user(user).description("description1").voteCount(20).subreddit(subreddit).build());

        mockMvc.perform(
                get("/api/posts/by-subreddit/" + subreddit.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].postName", is("post")));
    }


    @Test
    public void getPostsBySubreddit2Wrong() throws Exception {

        int subredditId = 151555;
        mockMvc.perform(
                get("/api/posts/by-subreddit/" + subredditId))
                .andDo(print())
                .andExpect(status().is(404))
                .andExpect(content().string("Subreddit not id " + subredditId));

    }


    final String username6 = "getPostsByUsername1Right";

    @Test
    @WithMockUser(username = username6)
    public void getPostsByUsername1Right() throws Exception {

        String subredditName = "getPostsByUsername1RightS";
        String password1 = "asdasdasd";
        Long postId = 1l;
        Long subredditId = 1l;

        User user = userRepository.save(User.builder().created(Instant.now()).email("a@pvhfha.ru")
                .username(username6).password(passwordEncoder.encode(password1)).enabled(true).build());

        Subreddit subreddit = subredditRepository.save(Subreddit.builder().id(subredditId)
                .description("d1").name(subredditName).user(user).build());

        Post post = postRepository.save(Post.builder().createdDate(Instant.now())
                .postId(postId).postName("getPostsByUsername1RightP").user(user).description("getPostsByUsername1RightD").voteCount(20).subreddit(subreddit).build());

        mockMvc.perform(
                get("/api/posts/by-user/" + username6))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].postName", is("getPostsByUsername1RightP")))
                .andExpect(jsonPath("$[0].description", is("getPostsByUsername1RightD")));
    }


    @Test
    public void getPostsByUsername2Wrong() throws Exception {
        String username7 = "getPostsByUsername2Wrong";
        mockMvc.perform(
                get("/api/posts/by-user/" + username7))
                .andDo(print())
                .andExpect(status().is(404))
                .andExpect(content().string("Username Not Found " + username7));

    }


}
