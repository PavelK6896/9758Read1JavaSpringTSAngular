package app.web.pavelk.read1.controller;

import app.web.pavelk.read1.dto.PostRequestDto;
import app.web.pavelk.read1.dto.PostResponseDto;
import app.web.pavelk.read1.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@AllArgsConstructor
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<Void> createPost(@RequestBody PostRequestDto postRequestDto) {
        return postService.createPost(postRequestDto);
    }

    @GetMapping
    public ResponseEntity<List<PostResponseDto>> getAllPosts() {
        return postService.getAllPosts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDto> getPost(@PathVariable Long id) {
        return postService.getPost(id);
    }

    @GetMapping("by-subreddit/{id}")
    public ResponseEntity<List<PostResponseDto>> getPostsBySubreddit(@PathVariable Long id) {
        return postService.getPostsBySubreddit(id);
    }

    @GetMapping("by-user/{name}")
    public ResponseEntity<List<PostResponseDto>> getPostsByUsername(@PathVariable String name) {
        return postService.getPostsByUsername(name);
    }
}
