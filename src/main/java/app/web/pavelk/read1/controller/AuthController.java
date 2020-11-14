package app.web.pavelk.read1.controller;

import app.web.pavelk.read1.dto.AuthenticationResponse;
import app.web.pavelk.read1.dto.LoginRequest;
import app.web.pavelk.read1.dto.RegisterRequest;
import app.web.pavelk.read1.service.AuthService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signUp")//регистрация пренимаем дто
    public ResponseEntity<String> signUp(@RequestBody RegisterRequest registerRequest) {
        //валидация?
        //проверка на существование?

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

    @PostMapping("/signIn")
    public AuthenticationResponse login(@RequestBody LoginRequest loginRequest) {
        log.info("signIn");
        return authService.signIn(loginRequest);
    }





}
