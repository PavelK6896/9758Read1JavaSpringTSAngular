package app.web.pavelk.read1.controller;


import app.web.pavelk.read1.exceptions.InvalidTokenException;
import app.web.pavelk.read1.exceptions.SpringRedditException;
import app.web.pavelk.read1.exceptions.UserAlreadyExists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.util.NestedServletException;


@Slf4j
@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(UserAlreadyExists.class)
    public ResponseEntity<String> exception1(Exception e) {
        log.error(e.getMessage() + " Такой пользователь уже существует");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(SpringRedditException.class)
    public ResponseEntity<String> exception2(Exception e) {
        log.error(e.getMessage() + " Ошибка");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> exception3(Exception e) {
        log.error(e.getMessage() + " Ошибка");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> exception4(Exception e) {
        log.error(e.getMessage() + " Неверный логин или пароль");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<String> exception5(Exception e) {
        log.error(e.getMessage() + " невалидный токен");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
    }
}
