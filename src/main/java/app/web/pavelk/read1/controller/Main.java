package app.web.pavelk.read1.controller;


import app.web.pavelk.read1.model.User;
import app.web.pavelk.read1.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.*;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class Main {

    UserRepository userRepository;
    PasswordEncoder passwordEncoder;

    @GetMapping
    public ResponseEntity<String> main() {

        //время 0
        Instant instant = LocalDate.now().atStartOfDay().toInstant(ZoneOffset.UTC);
        //текущее время
        Instant instant1 = LocalDateTime.now().toInstant(ZoneOffset.UTC);
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!");
        System.out.println(instant);
        System.out.println(instant1);

        User user1 = userRepository.save(new User(10l, "user11",
                passwordEncoder.encode("1"),
                "e@e.e", instant1, false));

        return new ResponseEntity<>("main test", OK);
    }
}
