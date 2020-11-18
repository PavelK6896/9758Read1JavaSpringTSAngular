package app.web.pavelk.read1.controller;


import app.web.pavelk.read1.dto.CommentsDto;
import app.web.pavelk.read1.service.CommentService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/comments/")
@AllArgsConstructor
@Slf4j
public class CommentsController {
    private final CommentService commentService;

    @PostMapping//создатьт комент
    public ResponseEntity<Void> createComment(@RequestBody CommentsDto commentsDto) {
        commentService.save(commentsDto);
        return new ResponseEntity<>(CREATED);
    }


    @GetMapping("/by-post/{postId}")//получить все коменты для поста
    public ResponseEntity<List<CommentsDto>> getAllCommentsForPost(@PathVariable Long postId) {
        return ResponseEntity.status(OK)
                .body(commentService.getAllCommentsForPost(postId));
    }

    @GetMapping("/by-user/{userName}")//все коменты по имени
    public ResponseEntity<List<CommentsDto>> getAllCommentsForUser(@PathVariable String userName){

        log.info("userName " + userName);
        return ResponseEntity.status(OK)
                .body(commentService.getAllCommentsForUser(userName));
    }

}
