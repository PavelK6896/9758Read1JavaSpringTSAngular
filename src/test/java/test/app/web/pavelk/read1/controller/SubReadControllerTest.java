package test.app.web.pavelk.read1.controller;


import app.web.pavelk.read1.Read1;
import app.web.pavelk.read1.dto.SubredditDto;
import app.web.pavelk.read1.model.Subreddit;
import app.web.pavelk.read1.repository.SubredditRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test-g")
@SpringBootTest(classes = Read1.class)
@AutoConfigureMockMvc(addFilters = false)
class SubReadControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SubredditRepository subredditRepository;

    @BeforeEach
    private void clearBase() {
        subredditRepository.deleteAll();
    }

    @Test
    void createSubreddit1Right() throws Exception {
        Long id = 1l;
        String name = "createSubreddit1RightN";
        String description = "createSubreddit1RightD";
        SubredditDto subredditDto = SubredditDto.builder().id(id).name(name).description(description).numberOfPosts(1).build();
        mockMvc.perform(
                post("/api/subreddit")
                        .content(objectMapper.writeValueAsString(subredditDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(201));
    }

    @Test
    void createSubreddit2Wrong() throws Exception {
        Long id = 1l;
        String description = "createSubreddit2WrongD";
        SubredditDto subredditDto = SubredditDto.builder().id(id).name(null).description(description).numberOfPosts(1).build();

        mockMvc.perform(
                post("/api/subreddit")
                        .content(objectMapper.writeValueAsString(subredditDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(404))
                .andExpect(content().string("name is marked non-null but is null"));

    }


    @Test
    void getAllSubreddits1Right() throws Exception {

        String name1 = "getAllSubreddits1Right1";
        String name2 = "getAllSubreddits1Right2";
        String description = "getAllSubreddits1RightD";

        Subreddit subreddit1 = subredditRepository.save(Subreddit.builder().name(name1).description(description).build());
        Subreddit subreddit2 = subredditRepository.save(Subreddit.builder().name(name2).description(description).build());

        mockMvc.perform(
                get("/api/subreddit"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is(name1)))
                .andExpect(jsonPath("$[1].name", is(name2)))
                .andExpect(jsonPath("$[0].description", is(description)))
                .andExpect(jsonPath("$[1].description", is(description)))
                .andExpect(jsonPath("$[0].id", is(subreddit1.getId().intValue())))
                .andExpect(jsonPath("$[1].id", is(subreddit2.getId().intValue())));
    }

    @Test
    void getAllSubreddits2Wrong() throws Exception {
        mockMvc.perform(
                get("/api/subreddit"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void getSubredditRight1() throws Exception {

        String name1 = "getSubreddit1Right";
        String description = "getSubreddit1RightD";
        Subreddit subreddit1 = subredditRepository.save(Subreddit.builder().name(name1).description(description).build());
        mockMvc.perform(
                get("/api/subreddit/" + subreddit1.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(name1)))
                .andExpect(jsonPath("$.description", is(description)))
                .andExpect(jsonPath("$.id", is(subreddit1.getId().intValue())));
    }

    @Test
    void getSubredditWrong2() throws Exception {
        Long id = 2626l;
        mockMvc.perform(
                get("/api/subreddit/" + id))
                .andDo(print())
                .andExpect(status().is(404))
                .andExpect(content().string("No subreddit found with ID - " + id.intValue()));
    }

}
