package test.app.web.pavelk.read1.controller;

import app.web.pavelk.read1.Read1;
import app.web.pavelk.read1.dto.LoginRequest;
import app.web.pavelk.read1.dto.RefreshTokenRequest;
import app.web.pavelk.read1.dto.RegisterRequest;
import app.web.pavelk.read1.model.RefreshToken;
import app.web.pavelk.read1.model.User;
import app.web.pavelk.read1.model.VerificationToken;
import app.web.pavelk.read1.repository.RefreshTokenRepository;
import app.web.pavelk.read1.repository.UserRepository;
import app.web.pavelk.read1.repository.VerificationTokenRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ActiveProfiles("test")
@SpringBootTest(classes = Read1.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @BeforeEach
    private void clearBase() {
        verificationTokenRepository.deleteAll();
        refreshTokenRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void signUp1Right() throws Exception {

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail("sanolo2837@1heizi.com");
        registerRequest.setUsername("name123ddas");
        registerRequest.setPassword("as!@AS123");

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
                .andExpect(status().is(400))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));


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
                .andExpect(status().is(403))
                .andExpect(content().string("Invalid verification Token"));
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

    @Test
    public void refreshToken1Right() throws Exception {

        String string = UUID.randomUUID().toString();
        String password = "trewt43";
        String username = "asdasdas";

        User user = userRepository.save(User.builder().created(Instant.now()).email("a@pvhfha.ru")
                .username(username).password(passwordEncoder.encode(password)).enabled(true).build());

        refreshTokenRepository.save(RefreshToken.builder()
                .createdDate(Instant.now()).token(string).build());
        RefreshTokenRequest refreshTokenRequest = RefreshTokenRequest.builder()
                .refreshToken(string).username(username).build();

        mockMvc.perform(
                post("/api/auth/refresh/token")
                        .content(objectMapper.writeValueAsString(refreshTokenRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.username", is(username)))
                .andExpect(jsonPath("$.authenticationToken").exists())
                .andExpect(jsonPath("$.authenticationToken").isString())
                .andExpect(jsonPath("$.refreshToken").exists())
                .andExpect(jsonPath("$.expiresAt").exists());
    }

    @Test
    public void refreshToken2Wrong() throws Exception {

        String string = UUID.randomUUID().toString();
        String username = "asdsadsad";
        RefreshTokenRequest refreshTokenRequest = RefreshTokenRequest.builder()
                .refreshToken(string).username(username).build();

        mockMvc.perform(
                post("/api/auth/refresh/token")
                        .content(objectMapper.writeValueAsString(refreshTokenRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(403))
                .andExpect(content().string("Invalid refresh Token"));
    }


    @Test
    public void logout1Right() throws Exception {

        String string = UUID.randomUUID().toString();
        String username = "asdasdas";
        refreshTokenRepository.save(RefreshToken.builder()
                .createdDate(Instant.now()).token(string).build());
        RefreshTokenRequest refreshTokenRequest = RefreshTokenRequest.builder()
                .refreshToken(string).username(username).build();

        mockMvc.perform(
                post("/api/auth/logout")
                        .content(objectMapper.writeValueAsString(refreshTokenRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(content().string("Refresh Token Deleted Successfully!"));
        assertThat(refreshTokenRepository.findByToken(string)).isEmpty();
    }
}
