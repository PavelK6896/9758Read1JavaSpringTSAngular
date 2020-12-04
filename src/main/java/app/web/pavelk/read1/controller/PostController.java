package app.web.pavelk.read1.controller;

import app.web.pavelk.read1.dto.PostRequest;
import app.web.pavelk.read1.dto.PostResponse;
import app.web.pavelk.read1.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/posts/")
@AllArgsConstructor
public class PostController {

    private final PostService postService;

    //создать
    @PostMapping
    public ResponseEntity<Void> createPost(@RequestBody PostRequest postRequest) {
        return postService.save(postRequest);
    }

    //все посты
    @GetMapping
    public ResponseEntity<List<PostResponse>> getAllPosts() {
        return postService.getAllPosts();
    }

    //один пост
    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPost(@PathVariable Long id) {
        return postService.getPost(id);
    }

    //посты по подписке
    @GetMapping("by-subreddit/{id}")
    public ResponseEntity<List<PostResponse>> getPostsBySubreddit(Long id) {
        return postService.getPostsBySubreddit(id);
    }

    //посты по имени
    @GetMapping("by-user/{name}")
    public ResponseEntity<List<PostResponse>> getPostsByUsername(String username) {
        return postService.getPostsByUsername(username);
    }
}
