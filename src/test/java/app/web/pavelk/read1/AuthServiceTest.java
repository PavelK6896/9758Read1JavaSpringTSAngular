package app.web.pavelk.read1;

import app.web.pavelk.read1.dto.RegisterRequest;
import app.web.pavelk.read1.exceptions.UserAlreadyExists;
import app.web.pavelk.read1.model.User;
import app.web.pavelk.read1.repository.UserRepository;
import app.web.pavelk.read1.repository.VerificationTokenRepository;
import app.web.pavelk.read1.security.JwtProvider;
import app.web.pavelk.read1.service.AuthService;
import app.web.pavelk.read1.service.MailService;
import app.web.pavelk.read1.service.RefreshTokenService;
import app.web.pavelk.read1.service.UserDetailsServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.OK;

class AuthServiceTest {

    PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
    UserRepository userRepository = Mockito.mock(UserRepository.class);
    VerificationTokenRepository verificationTokenRepository = Mockito.mock(VerificationTokenRepository.class);
    MailService mailService = Mockito.mock(MailService.class);
    AuthenticationManager authenticationManager = Mockito.mock(AuthenticationManager.class);
    JwtProvider jwtProvider = Mockito.mock(JwtProvider.class);
    RefreshTokenService refreshTokenService = Mockito.mock(RefreshTokenService.class);
    UserDetailsServiceImpl userDetailsService = Mockito.mock(UserDetailsServiceImpl.class);

    @Test
    void userIsEnabledTrue() {
        AuthService authService = new AuthService(passwordEncoder, userRepository, verificationTokenRepository,
                mailService, authenticationManager, jwtProvider, refreshTokenService, userDetailsService);
        User build1 = User.builder().password("d").enabled(true).username("ww").build();
        when(userRepository.findByUsername(Mockito.any()))
                .thenReturn(Optional.of(build1));

        RegisterRequest build = RegisterRequest.builder().email("ww").password("pp").username("uu").build();
        Assertions.assertThrows(UserAlreadyExists.class, () -> {
            authService.signUp(build);
        });
    }

    @Test
    void userIsEnabledFalse() {
        AuthService authService = new AuthService(passwordEncoder, userRepository, verificationTokenRepository,
                mailService, authenticationManager, jwtProvider, refreshTokenService, userDetailsService);

        assertNotNull(authService);
        User build1 = User.builder().password("d").enabled(false).username("ww").build();
        when(userRepository.findByUsername(Mockito.any()))
                .thenReturn(Optional.of(build1));

        RegisterRequest build = RegisterRequest.builder().email("ww").password("pp").username("uu").build();
        ResponseEntity<String> stringResponseEntity = authService.signUp(build);
        assertEquals(OK, stringResponseEntity.getStatusCode());
    }
}
