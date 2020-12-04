package app.web.pavelk.read1.controller;

import app.web.pavelk.read1.dto.AuthenticationResponse;
import app.web.pavelk.read1.dto.LoginRequest;
import app.web.pavelk.read1.dto.RefreshTokenRequest;
import app.web.pavelk.read1.dto.RegisterRequest;
import app.web.pavelk.read1.service.AuthService;
import app.web.pavelk.read1.service.RefreshTokenService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.OK;

//добавляет 4 заголовка
//  Access-Control-Allow-Origin: *
//  Vary: Origin
//  Vary: Access-Control-Request-Method
//  Vary: Access-Control-Request-Headers
//@CrossOrigin

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;

    //регистрация
    @PostMapping("/signUp")
    public ResponseEntity<String> signUp(@RequestBody RegisterRequest registerRequest) {
        return authService.signUp(registerRequest);
    }

    //подтверждение регистрации
    @GetMapping("accountVerification/{token}")
    public ResponseEntity<String> verifyAccount(@PathVariable String token) {
        return authService.verifyAccount(token);
    }

    //для входа
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody LoginRequest loginRequest) {
        return authService.signIn(loginRequest);
    }

    //обновление токена
    // * Ограничения, определенные для объекта и его свойств, проверяются при проверке
    //This behavior is applied recursively.
    // токена имя и время жизни обычьного токена
    @PostMapping("/refresh/token")
    public ResponseEntity<AuthenticationResponse> refreshTokens(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        return authService.refreshToken(refreshTokenRequest);
    }

    //для выхода и удаления рефрешь токена
    //нужо передовать рефрешь токен для удаления
    //Маркер Обновления Успешно Удален
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        return refreshTokenService.deleteRefreshToken(refreshTokenRequest.getRefreshToken());
    }

}
