package app.web.pavelk.read1.controller;


import app.web.pavelk.read1.exceptions.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


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

    @ExceptionHandler(PostNotFoundException.class)
    public ResponseEntity<String> exception6(Exception e) {
        log.error(e.getMessage() + " пост не найден");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<String> exception7(Exception e) {
        log.error(e.getMessage() + " пользователь не найден");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(SubredditNotFoundException.class)
    public ResponseEntity<String> exception8(Exception e) {
        log.error(e.getMessage() + " Подразделение не найдено");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<String> exception9(Exception e) {
        log.error(e.getMessage() + " данные не найдены");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(VoteException.class)
    public ResponseEntity<String> exception10(Exception e) {
        log.error(e.getMessage() + " уже проголосовали");
        return ResponseEntity.status(HttpStatus.GONE).body(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> exception11(Exception e) {
        log.error(e.getMessage() + " ошибка параметры");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}
