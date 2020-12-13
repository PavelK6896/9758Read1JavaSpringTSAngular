package test.app.web.pavelk.read1.controller;


import app.web.pavelk.read1.Read1;
import app.web.pavelk.read1.dto.VoteDto;
import app.web.pavelk.read1.model.*;
import app.web.pavelk.read1.repository.PostRepository;
import app.web.pavelk.read1.repository.SubredditRepository;
import app.web.pavelk.read1.repository.UserRepository;
import app.web.pavelk.read1.repository.VoteRepository;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = Read1.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
public class VoteControllerTest {

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

    private void clearBase() {
        voteRepository.deleteAll();
        postRepository.deleteAll();
        subredditRepository.deleteAll();
        userRepository.deleteAll();
    }

    final String username1 = "voteRight1";

    @Test
    @WithMockUser(username = username1)
    public void voteRight1() throws Exception {
        String name = "voteRight1N";
        String description = "voteRight1D";
        String password1 = "dsd$%#@sdfs";

        User user = userRepository.save(User.builder().created(Instant.now()).email("a@pvhfha.ru")
                .username(username1).password(passwordEncoder.encode(password1)).enabled(true).build());

        Subreddit subreddit = subredditRepository.save(Subreddit.builder()
                .description(description).name(name).user(user).build());

        Post post1 = postRepository.save(Post.builder().createdDate(Instant.now())
                .postName("post1").user(user).url("url1").description("d1")
                .voteCount(10).subreddit(subreddit).build());

        VoteDto voteDto = VoteDto.builder().voteType(VoteType.UP_VOTE).postId(post1.getPostId()).build();

        mockMvc.perform(
                post("/api/votes/")
                        .content(objectMapper.writeValueAsString(voteDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(202));


        clearBase();
    }

    @Test
    @WithMockUser(username = username1)
    public void voteWrong2() throws Exception {
        String name = "voteRight1N";
        String description = "voteRight1D";
        String password1 = "dsd$%#@sdfs";

        User user = userRepository.save(User.builder().created(Instant.now()).email("a@pvhfha.ru")
                .username(username1).password(passwordEncoder.encode(password1)).enabled(true).build());

        Subreddit subreddit = subredditRepository.save(Subreddit.builder()
                .description(description).name(name).user(user).build());

        Post post1 = postRepository.save(Post.builder().createdDate(Instant.now())
                .postName("post1").user(user).url("url1").description("d1")
                .voteCount(10).subreddit(subreddit).build());

        VoteDto voteDto = VoteDto.builder().voteType(VoteType.DOWN_VOTE).postId(post1.getPostId()).build();

        voteRepository.save(Vote.builder().post(post1).user(user).voteType(VoteType.DOWN_VOTE).build());

        mockMvc.perform(
                post("/api/votes/")
                        .content(objectMapper.writeValueAsString(voteDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(410));

        clearBase();
    }


}
