package app.web.pavelk.read1.service;

import app.web.pavelk.read1.dto.AuthenticationResponse;
import app.web.pavelk.read1.dto.LoginRequest;
import app.web.pavelk.read1.dto.RefreshTokenRequest;
import app.web.pavelk.read1.dto.RegisterRequest;
import app.web.pavelk.read1.exceptions.SpringRedditException;
import app.web.pavelk.read1.exceptions.UserAlreadyExists;
import app.web.pavelk.read1.model.NotificationEmail;
import app.web.pavelk.read1.model.User;
import app.web.pavelk.read1.model.VerificationToken;
import app.web.pavelk.read1.repository.UserRepository;
import app.web.pavelk.read1.repository.VerificationTokenRepository;
import app.web.pavelk.read1.security.JwtProvider;
import app.web.pavelk.read1.service.mail.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.http.HttpStatus.OK;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final MailService mailService;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;

    @Transactional
    public ResponseEntity<String> signUp(RegisterRequest registerRequest) {
        log.info("signUp");
        userRepository.findByUsername(registerRequest.getUsername())
                .ifPresent(user -> {
                    throw new UserAlreadyExists("Such a user already exists");
                });

        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setCreated(Instant.now());
        user.setEnabled(false);

        userRepository.save(user);

        String token = generateVerificationToken(user);

        mailService.sendMail(new NotificationEmail("Please Activate your Account",
                user.getEmail(), "Thank you for signing up to Spring Reddit, " +
                "please click on the below url to activate your account : " +
                "http://localhost:8080/api/auth/accountVerification/" + token));

        return ResponseEntity.status(OK).body("User Registration Successful");
    }

    private String generateVerificationToken(User user) {
        String token = UUID.randomUUID().toString();

        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);

        verificationTokenRepository.save(verificationToken);
        return token;
    }

    public ResponseEntity<String> verifyAccount(String token) {
        log.info("verifyAccount");
        Optional<VerificationToken> verificationToken = verificationTokenRepository.findByToken(token);
        fetchUserAndEnable(verificationToken.orElseThrow(() -> new SpringRedditException("Invalid Token")));
        return ResponseEntity.status(OK).body("Account Activated Successfully");
    }

    @Transactional
    public void fetchUserAndEnable(VerificationToken verificationToken) {
        String username = verificationToken.getUser().getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new SpringRedditException("User not found with name - " + username));
        user.setEnabled(true);
        userRepository.save(user);
    }


    public ResponseEntity<AuthenticationResponse> signIn(LoginRequest loginRequest) {
        log.info("signIn");
        Authentication authenticate = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                        loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        String token = jwtProvider.generateToken(authenticate);

        return ResponseEntity.status(OK).body(
                AuthenticationResponse.builder()
                        .authenticationToken(token)
                        .refreshToken(refreshTokenService.generateRefreshToken().getToken())
                        .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
                        .username(loginRequest.getUsername()).build());
    }

    @Transactional(readOnly = true)
    public User getCurrentUser() {

        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();

        return userRepository.findByUsername(principal.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User name not found - " + principal.getUsername()));
    }

    public boolean isLoggedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated();
    }



    public ResponseEntity<AuthenticationResponse> refreshToken(RefreshTokenRequest refreshTokenRequest) {
        log.info("refreshTokens");
        refreshTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken());

        String token = jwtProvider.generateTokenWithUserName(refreshTokenRequest.getUsername());

        return ResponseEntity.status(OK).body(AuthenticationResponse.builder()
                .authenticationToken(token)
                .refreshToken(refreshTokenRequest.getRefreshToken())
                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
                .username(refreshTokenRequest.getUsername())
                .build());
    }
}
