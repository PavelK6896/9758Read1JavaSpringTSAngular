package test.app.web.pavelk.read1.controller;

import app.web.pavelk.read1.Read1;
import app.web.pavelk.read1.dto.CommentsDto;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = Read1.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
public class CommentsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private SubredditRepository subredditRepository;

    private final String password1 = "dsd$%#@$sfSDF";
    private final String username1 = "aasfdasf423";
    private final String username2 = "sadasdasd";

    @Test
    @WithMockUser(username = username1)
    public void createComment1Right() throws Exception {
        User user = userRepository.save(User.builder().created(Instant.now()).email("a@pvhfha.ru")
                .username(username1).password(passwordEncoder.encode(password1)).enabled(true).build());

        Subreddit subreddit = subredditRepository.save(Subreddit.builder()
                .description("d1").name("name1").user(user).build());

        Post post = postRepository.save(Post.builder()
                .postId(1l).postName("szds").user(user).description("11").voteCount(0).subreddit(subreddit).build());

        CommentsDto comment1 = CommentsDto.builder()
                .createdDate(Instant.now()).postId(post.getPostId()).text("comment1").userName(username1).build();

        mockMvc.perform(
                post("/api/comments/")
                        .content(objectMapper.writeValueAsString(comment1))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(201));
    }

    @Test
    public void createComment2WrongPostNotFoundException() throws Exception {
        CommentsDto comment1 = CommentsDto.builder()
                .createdDate(Instant.now()).postId(111l).text("comment1").userName(username1).build();

        mockMvc.perform(
                post("/api/comments/")
                        .content(objectMapper.writeValueAsString(comment1))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(404));

    }

    @Test
    @WithMockUser(username = username2)
    public void createComment3WrongUsernameNotFoundException() throws Exception {

        Subreddit subreddit = subredditRepository.save(Subreddit.builder()
                .description("d1").name("name1").user(null).build());

        Post post = postRepository.save(Post.builder()
                .postId(1l).postName("szds").user(null).description("11").voteCount(0).subreddit(subreddit).build());

        CommentsDto comment1 = CommentsDto.builder()
                .createdDate(Instant.now()).postId(post.getPostId()).text("comment1").userName(username2).build();

        mockMvc.perform(
                post("/api/comments/")
                        .content(objectMapper.writeValueAsString(comment1))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(404))
                .andExpect(content().string("User name not found " + username2));
    }


}
