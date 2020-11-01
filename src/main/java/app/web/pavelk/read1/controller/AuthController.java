package app.web.pavelk.read1.controller;

import app.web.pavelk.read1.dto.RegisterRequest;
import app.web.pavelk.read1.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
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
}
