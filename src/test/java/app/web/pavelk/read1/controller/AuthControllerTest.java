package app.web.pavelk.read1.controller;

import app.web.pavelk.read1.dto.RegisterRequest;
import app.web.pavelk.read1.model.User;
import app.web.pavelk.read1.model.VerificationToken;
import app.web.pavelk.read1.repository.UserRepository;
import app.web.pavelk.read1.repository.VerificationTokenRepository;
import app.web.pavelk.read1.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AuthService authService;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;
    @Autowired
    private UserRepository userRepository;


    @Test
    public void signUp() throws Exception {

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail("sanolo2837@1heizi.com");
        registerRequest.setUsername("name123");
        registerRequest.setPassword("name123");

        mockMvc.perform(
                post("/api/auth/signUp")
                        .content(objectMapper.writeValueAsString(registerRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("User Registration Successful"));
    }

    @Test
    public void signUp2() throws Exception {

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail("sanolo2837@1heizi.com");

        mockMvc.perform(
                post("/api/auth/signUp")
                        .content(objectMapper.writeValueAsString(registerRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("rawPassword cannot be null"));
    }

    @Test
    public void accountVerification1() throws Exception {

        User user = userRepository.save(User.builder().created(Instant.now()).email("a@a.ru")
                .username("a").password("1").id(1l).build());

        verificationTokenRepository.save(VerificationToken.builder().id(1l)
                .token("4412ced7-1faf-49b4-a05a-d1cee3c526af").user(user).build());

        mockMvc.perform(
                get("/api/auth/accountVerification/4412ced7-1faf-49b4-a05a-d1cee3c526af"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("Account Activated Successfully"));
    }

    @Test
    public void accountVerification2() throws Exception {
        mockMvc.perform(
                get("/api/auth/accountVerification/ljljljljlkjlk"))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Invalid Token"));
    }


}
