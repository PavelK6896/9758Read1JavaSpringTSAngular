package app.web.pavelk.read1.service;

import app.web.pavelk.read1.dto.*;
import app.web.pavelk.read1.exceptions.InvalidTokenException;
import app.web.pavelk.read1.exceptions.SpringRedditException;
import app.web.pavelk.read1.exceptions.UserAlreadyExists;
import app.web.pavelk.read1.model.User;
import app.web.pavelk.read1.model.VerificationToken;
import app.web.pavelk.read1.repository.UserRepository;
import app.web.pavelk.read1.repository.VerificationTokenRepository;
import app.web.pavelk.read1.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.http.HttpStatus.FOUND;
import static org.springframework.http.HttpStatus.OK;


@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final MailService mailService;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;
    private final UserDetailsService userDetailsServiceImpl;

    @Value("${host}")
    private String host;

    @Transactional
    public ResponseEntity<String> signUp(RegisterRequest registerRequest) {
        log.info("signUp");
        User setUser;
        Optional<User> byUsername = userRepository.findByUsername(registerRequest.getUsername());
        if (byUsername.isPresent()) {
            if (byUsername.get().isEnabled()) {
                throw new UserAlreadyExists("Such a user already exists");
            } else {
                setUser = byUsername.get();
            }
        } else {
            setUser = new User();
        }

        setUser.setUsername(registerRequest.getUsername());
        setUser.setEmail(registerRequest.getEmail());
        setUser.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        setUser.setCreated(Instant.now());
        setUser.setEnabled(false);

        userRepository.save(setUser);
        String token = generateVerificationToken(setUser);
        mailService.sendMail(new NotificationEmail("Please Activate your Account",
                setUser.getEmail(), "Thank you for signing up to Spring Reddit, " +
                "please click on the below url to activate your account : " +
                host + "/api/read1/api/auth/accountVerification/" + token));

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

    public ResponseEntity<Void> verifyAccount(String token) {
        log.info("verifyAccount");
        fetchUserAndEnable(verificationTokenRepository.findByToken(token)
                .orElseThrow(() -> new InvalidTokenException("Invalid verification Token")));
        return ResponseEntity.status(FOUND).header("Location", host + "/read1").build();
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

        org.springframework.security.core.userdetails.User principal =
                (org.springframework.security.core.userdetails.User) authenticate.getPrincipal();

        AuthenticationResponse authenticationResponse = jwtProvider.generateToken(principal);
        authenticationResponse.setRefreshToken(refreshTokenService.generateRefreshToken().getToken());

        return ResponseEntity.status(OK).body(authenticationResponse);
    }

    @Transactional(readOnly = true)
    public User getCurrentUser() {

        org.springframework.security.core.userdetails.User principal
                = (org.springframework.security.core.userdetails.User) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();

        return userRepository.findByUsername(principal.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User name not found " + principal.getUsername()));

    }

    public boolean isLoggedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated();
    }

    public ResponseEntity<AuthenticationResponse> refreshToken(RefreshTokenRequest refreshTokenRequest) {
        log.info("refreshTokens -------------------------- ******************************** ");
        refreshTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken());

        org.springframework.security.core.userdetails.User principal =
                (org.springframework.security.core.userdetails.User)
                        userDetailsServiceImpl.loadUserByUsername(refreshTokenRequest.getUsername());

        AuthenticationResponse authenticationResponse = jwtProvider.generateToken(principal);
        authenticationResponse.setRefreshToken(refreshTokenService.generateRefreshToken().getToken());

        return ResponseEntity.status(OK).body(authenticationResponse);
    }
}
