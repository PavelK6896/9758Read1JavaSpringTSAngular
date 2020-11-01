package app.web.pavelk.read1.controller;


import app.web.pavelk.read1.model.User;
import app.web.pavelk.read1.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/")
@AllArgsConstructor
public class Main {

    UserRepository userRepository;
    PasswordEncoder passwordEncoder;

    @GetMapping
    @Transactional
    public ResponseEntity<String> main() {

        //время 0
        Instant instant = LocalDate.now().atStartOfDay().toInstant(ZoneOffset.UTC);
        //текущее время
        Instant instant1 = LocalDateTime.now().toInstant(ZoneOffset.UTC);
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!");
        System.out.println(instant);
        System.out.println(instant1);

        if (!userRepository.findByUsername("user11").isPresent()) {
            User user1 = userRepository.save(new User(10l, "user11",
                    passwordEncoder.encode("1"),
                    "e@e.e", instant1, false));
        }

        return new ResponseEntity<>("main test", OK);
    }
}
