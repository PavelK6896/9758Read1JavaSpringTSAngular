package app.web.pavelk.read1.controller;

import app.web.pavelk.read1.dto.SubredditDto;
import app.web.pavelk.read1.service.SubReadService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subreddit")
@AllArgsConstructor
@Slf4j
public class SubReadController {

    private final SubReadService subReadService;

    @PostMapping // сохранит сабскрайб вернет дто с ид
    public ResponseEntity<SubredditDto> createSubreddit(@RequestBody SubredditDto subredditDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(subReadService.save(subredditDto));
    }

//    @GetMapping // вернет все подписки
//    public ResponseEntity<List<SubredditDto>> getAllSubreddits() {
//        return ResponseEntity
//                .status(HttpStatus.OK)
//                .body(subReadService.getAll());
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<SubredditDto> getSubreddit(@PathVariable Long id) {
//        return ResponseEntity
//                .status(HttpStatus.OK)
//                .body(subReadService.getSubreddit(id));
//    }
}
