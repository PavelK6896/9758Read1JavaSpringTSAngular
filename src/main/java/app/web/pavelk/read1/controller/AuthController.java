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

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/signup")//регистрация пренимаем дто
    public ResponseEntity<String> signUp(@RequestBody RegisterRequest registerRequest) {
        //валидация?
        //проверка на существование?
      log.info("signup");

        authService.signUp(registerRequest);
        return new ResponseEntity<>("User Registration Successful",
                OK);
    }

    //подтверждение регистрации
    @GetMapping("accountVerification/{token}")
    public ResponseEntity<String> verifyAccount(@PathVariable String token) {
        authService.verifyAccount(token);
        return new ResponseEntity<>("Account Activated Successfully", OK); //Учетная Запись Успешно Активирована
    }

    @PostMapping("/signin")//для входа
    public AuthenticationResponse login(@RequestBody LoginRequest loginRequest) {
        log.info("signin");
        return authService.signIn(loginRequest);
    }


    //обновление токена
    @PostMapping("/refresh/token")
    // * Ограничения, определенные для объекта и его свойств, проверяются при проверке
    //This behavior is applied recursively.
    public AuthenticationResponse refreshTokens(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        return authService.refreshToken(refreshTokenRequest);
        //2 токена имя и время жизни обычьного токена
    }



    //для выхода и удаления рефрешь токена
    //нужо передовать рефрешь токен для удаления
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        refreshTokenService.deleteRefreshToken(refreshTokenRequest.getRefreshToken());
        return ResponseEntity.status(OK).body("Refresh Token Deleted Successfully!!");
        //Маркер Обновления Успешно Удален
    }








}
