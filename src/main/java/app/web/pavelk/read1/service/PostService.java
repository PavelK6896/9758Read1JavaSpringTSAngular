package app.web.pavelk.read1.service;


import app.web.pavelk.read1.dto.PostRequest;
import app.web.pavelk.read1.dto.PostResponse;
import app.web.pavelk.read1.exceptions.PostNotFoundException;
import app.web.pavelk.read1.exceptions.SubredditNotFoundException;
import app.web.pavelk.read1.mapper.PostMapper;
import app.web.pavelk.read1.model.Post;
import app.web.pavelk.read1.model.Subreddit;
import app.web.pavelk.read1.model.User;
import app.web.pavelk.read1.repository.PostRepository;
import app.web.pavelk.read1.repository.SubredditRepository;
import app.web.pavelk.read1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final SubredditRepository subredditRepository;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final PostMapper postMapper;

    public ResponseEntity<Void> createPost(PostRequest postRequest) {
        log.info("createPost");
        Subreddit subreddit = subredditRepository.findByName(postRequest.getSubredditName())
                .orElseThrow(() -> new SubredditNotFoundException("The division is not found " + postRequest.getSubredditName()));

        postRepository.save(postMapper.map(postRequest, subreddit, authService.getCurrentUser()));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Transactional(readOnly = true)
    public ResponseEntity<PostResponse> getPost(Long id) {
        log.info("getPost");
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException("Post not found id " + id.toString()));
        return ResponseEntity.status(HttpStatus.OK).body(postMapper.mapToDto(post));
    }

    @Transactional(readOnly = true)
    public ResponseEntity<List<PostResponse>> getAllPosts() {
        return ResponseEntity.status(HttpStatus.OK).body(
                postRepository.findAll()
                        .stream()
                        .map(postMapper::mapToDto)
                        .collect(toList()));
    }

    @Transactional(readOnly = true)
    public ResponseEntity<List<PostResponse>> getPostsBySubreddit(Long subredditId) {
        log.info("getPostsBySubreddit");
        Subreddit subreddit = subredditRepository.findById(subredditId)
                .orElseThrow(() -> new SubredditNotFoundException("Subreddit not id " + subredditId.toString()));
        List<Post> posts = postRepository.findAllBySubreddit(subreddit);
        return ResponseEntity.status(HttpStatus.OK).body(posts.stream().map(postMapper::mapToDto).collect(toList()));
    }

    @Transactional(readOnly = true)
    public ResponseEntity<List<PostResponse>> getPostsByUsername(String username) {
        log.info("getPostsBySubreddit");
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username Not Found " + username));
        return ResponseEntity.status(HttpStatus.OK).body(postRepository.findByUser(user)
                .stream()
                .map(postMapper::mapToDto)
                .collect(toList()));
    }
}
