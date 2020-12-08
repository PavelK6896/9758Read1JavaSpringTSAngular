package app.web.pavelk.read1.service;


import app.web.pavelk.read1.dto.CommentsDto;
import app.web.pavelk.read1.dto.NotificationEmail;
import app.web.pavelk.read1.exceptions.PostNotFoundException;
import app.web.pavelk.read1.mapper.CommentMapper;
import app.web.pavelk.read1.model.Post;
import app.web.pavelk.read1.model.User;
import app.web.pavelk.read1.repository.CommentRepository;
import app.web.pavelk.read1.repository.PostRepository;
import app.web.pavelk.read1.repository.UserRepository;
import app.web.pavelk.read1.service.mail.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {
    private String POST_URL = "";
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final CommentMapper commentMapper;
    private final CommentRepository commentRepository;
    private final MailService mailService;

    public ResponseEntity<Void> createComment(CommentsDto commentsDto) {
        log.info("createComment");
        Post post = postRepository.findById(commentsDto.getPostId())
                .orElseThrow(() -> new PostNotFoundException("No post " + commentsDto.getPostId().toString()));
        User currentUser = authService.getCurrentUser();
        commentRepository.save(commentMapper.map(commentsDto, post, currentUser));

        if (post.getUrl() != null)
            POST_URL = post.getUrl();

        String stringMessageMail = currentUser.getUsername() + " posted a comment on your post." + POST_URL;
        sendCommentNotification(stringMessageMail, post.getUser());
        return ResponseEntity.status(CREATED).build();
    }

    private void sendCommentNotification(String message, User user) {
        mailService.sendMail(new NotificationEmail(user.getUsername() + " Commented on your post", user.getEmail(), message));
    }

    public ResponseEntity<List<CommentsDto>> getAllCommentsForPost(Long postId) {
        log.info("getAllCommentsForPost");
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException("Not found post " + postId.toString()));
        return ResponseEntity.status(OK).body(
                commentRepository.findByPost(post).stream()
                        .map(commentMapper::mapToDto).collect(toList()));
    }

    public ResponseEntity<List<CommentsDto>> getAllCommentsForUser(String userName) {
        log.info("getAllCommentsForUser");
        User user = userRepository.findByUsername(userName)
                .orElseThrow(() -> new UsernameNotFoundException("User name not found " + userName));
        System.out.println("--getAllCommentsForUser");
        return ResponseEntity.status(OK)
                .body(commentRepository.findAllByUser(user)
                        .stream()
                        .map(commentMapper::mapToDto)
                        .collect(toList()));
    }
}
