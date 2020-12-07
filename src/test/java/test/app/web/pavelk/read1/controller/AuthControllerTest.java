package test.app.web.pavelk.read1.controller;

import app.web.pavelk.read1.Read1;
import app.web.pavelk.read1.dto.LoginRequest;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest(classes = Read1.class)
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

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Test
    public void signUp1Right() throws Exception {

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
    public void signUp2Wrong() throws Exception {

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
    public void accountVerification1Right() throws Exception {

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
    public void accountVerification2Wrong() throws Exception {
        mockMvc.perform(
                get("/api/auth/accountVerification/ljljljljlkjlk"))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Invalid Token"));
    }

    @Test
    public void login1AllRight() throws Exception {
        String password = "dsd$%#@$sfSDF";
        String username = "aasfdasf423";

        User user = userRepository.save(User.builder().created(Instant.now()).email("a@pvhfha.ru")
                .username(username).password(passwordEncoder.encode(password)).enabled(true).build());
        LoginRequest loginRequest = LoginRequest.builder().username(username).password(password).build();
        boolean matches = passwordEncoder.matches(password, user.getPassword());
        System.out.println("login1AllRight " + matches);

        mockMvc.perform(
                post("/api/auth/login")
                        .content(objectMapper.writeValueAsString(loginRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is(username)))
                .andExpect(jsonPath("$.authenticationToken").exists())
                .andExpect(jsonPath("$.authenticationToken").isString())
                .andExpect(jsonPath("$.refreshToken").exists())
                .andExpect(jsonPath("$.expiresAt").exists());
    }

    @Test
    public void login2WrongPassword() throws Exception {
        String password = "dsd$%#@wqerwerew";
        String username = "safsdfs";

        User user = userRepository.save(User.builder().created(Instant.now()).email("a@pvhfha.ru")
                .username(username).password(passwordEncoder.encode("password")).enabled(true).build());
        LoginRequest loginRequest = LoginRequest.builder().username(username).password(password).build();
        boolean matches = passwordEncoder.matches(password, user.getPassword());
        System.out.println("login2WrongPassword " + matches);

        mockMvc.perform(
                post("/api/auth/login")
                        .content(objectMapper.writeValueAsString(loginRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(403))
                .andExpect(content().string("Bad credentials"));
    }

    @Test
    public void login3WrongPassword() throws Exception {
        String password = "trewt43";
        String username = "dsfsd";

        LoginRequest loginRequest = LoginRequest.builder().username(username).password(password).build();

        mockMvc.perform(
                post("/api/auth/login")
                        .content(objectMapper.writeValueAsString(loginRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(403))
                .andExpect(content().string("Bad credentials"));
    }

}
