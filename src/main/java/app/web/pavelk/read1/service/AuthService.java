package app.web.pavelk.read1.service;

import app.web.pavelk.read1.dto.AuthenticationResponse;
import app.web.pavelk.read1.dto.LoginRequest;
import app.web.pavelk.read1.dto.RegisterRequest;
import app.web.pavelk.read1.exceptions.SpringRedditException;
import app.web.pavelk.read1.model.NotificationEmail;
import app.web.pavelk.read1.model.User;
import app.web.pavelk.read1.model.VerificationToken;
import app.web.pavelk.read1.repository.UserRepository;
import app.web.pavelk.read1.repository.VerificationTokenRepository;
import app.web.pavelk.read1.security.JwtProvider;
import app.web.pavelk.read1.service.mail.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final MailService mailService;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;

    @Transactional
    public void signUp(RegisterRequest registerRequest) {
        userRepository.findByUsername(registerRequest.getUsername())
                .ifPresent(user -> {
                    throw new SpringRedditException("Такой пользователь уже существует");
                });

        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setCreated(Instant.now());//time
        user.setEnabled(false);//active

        userRepository.save(user);

        String token = generateVerificationToken(user);//register token

        mailService.sendMail(new NotificationEmail("Please Activate your Account",
                user.getEmail(), "Thank you for signing up to Spring Reddit, " +
                "please click on the below url to activate your account : " +
                "http://localhost:8080/api/auth/accountVerification/" + token));
    }

    //токен для регистрации
    private String generateVerificationToken(User user) {
        String token = UUID.randomUUID().toString();//для сылки

        VerificationToken verificationToken = new VerificationToken();//для базы
        verificationToken.setToken(token);
        verificationToken.setUser(user);

        verificationTokenRepository.save(verificationToken);
        return token;
    }

    public void verifyAccount(String token) {
        Optional<VerificationToken> verificationToken = verificationTokenRepository.findByToken(token);
        //проверка есть ли токен в базе
        fetchUserAndEnable(verificationToken.orElseThrow(() -> new SpringRedditException("Invalid Token")));//Недопустимый Токен
    }

    @Transactional
    public void fetchUserAndEnable(VerificationToken verificationToken) {
        //из токена регистрации
        String username = verificationToken.getUser().getUsername();
        //найти юзера
        User user = userRepository.findByUsername(username)
                //        Пользователь не найден с именем
                .orElseThrow(() -> new SpringRedditException("User not found with name - " + username));
        //установить значение активилован и сохранить
        user.setEnabled(true);
        userRepository.save(user);
    }


    public AuthenticationResponse signIn(LoginRequest loginRequest) {

        //создает авторизацию для спринга
        Authentication authenticate = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                        loginRequest.getPassword()));
        //установить авторизацию в спринг
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        String token = jwtProvider.generateToken(authenticate);

        //ответ в дто todo Обернуть в ентити
        return AuthenticationResponse.builder()
                .authenticationToken(token)
                .refreshToken(refreshTokenService.generateRefreshToken().getToken())
                //todo время отдельно или в токене
                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
                .username(loginRequest.getUsername())
                .build();

    }
}
