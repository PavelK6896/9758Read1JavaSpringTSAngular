package test.app.web.pavelk.read1.controller;


import app.web.pavelk.read1.Read1;
import app.web.pavelk.read1.dto.VoteDto;
import app.web.pavelk.read1.model.Post;
import app.web.pavelk.read1.model.Subreddit;
import app.web.pavelk.read1.model.User;
import app.web.pavelk.read1.model.VoteType;
import app.web.pavelk.read1.repository.PostRepository;
import app.web.pavelk.read1.repository.SubredditRepository;
import app.web.pavelk.read1.repository.UserRepository;
import app.web.pavelk.read1.repository.VoteRepository;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test-g")
@SpringBootTest(classes = Read1.class)
@AutoConfigureMockMvc(addFilters = false)
class VoteControllerTest {

    final String username1 = "voteRight1";
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
    private SubredditRepository subredditRepository;
    @Autowired
    private VoteRepository voteRepository;

    @BeforeEach
    private void clearBase() {
        voteRepository.deleteAll();
        postRepository.deleteAll();
        subredditRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = username1)
    void voteRight1() throws Exception {
        String name = "voteRight1N";
        String description = "voteRight1D";
        String password1 = "dsd$%#@sdfs";

        User user = userRepository.save(User.builder().created(Instant.now()).email("a@pvhfha.ru")
                .username(username1).password(passwordEncoder.encode(password1)).enabled(true).build());

        Subreddit subreddit = subredditRepository.save(Subreddit.builder()
                .description(description).name(name).user(user).build());

        Post post1 = postRepository.save(Post.builder().createdDate(LocalDateTime.now())
                .postName("post1").user(user).description("d1")
                .voteCount(10).subreddit(subreddit).build());

        VoteDto voteDto = VoteDto.builder().voteType(VoteType.UP_VOTE).postId(post1.getPostId()).build();

        mockMvc.perform(
                post("/api/votes")
                        .content(objectMapper.writeValueAsString(voteDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(202));

    }

    @Test
    @WithMockUser(username = username1)
    void voteWrong2() throws Exception {
        String password1 = "dsd$%#@sdfs";

        User user = userRepository.save(User.builder().created(Instant.now()).email("a@pvhfha.ru")
                .username(username1).password(passwordEncoder.encode(password1)).enabled(true).build());

        VoteDto voteDto = VoteDto.builder().voteType(VoteType.DOWN_VOTE).postId(150l).build();

        mockMvc.perform(
                post("/api/votes")
                        .content(objectMapper.writeValueAsString(voteDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(404));
    }
}
