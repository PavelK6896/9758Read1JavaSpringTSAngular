package app.web.pavelk.read1.service;


import app.web.pavelk.read1.dto.CommentsDto;
import app.web.pavelk.read1.exceptions.PostNotFoundException;
import app.web.pavelk.read1.mapper.CommentMapper;
import app.web.pavelk.read1.model.Comment;
import app.web.pavelk.read1.model.NotificationEmail;
import app.web.pavelk.read1.model.Post;
import app.web.pavelk.read1.model.User;
import app.web.pavelk.read1.repository.CommentRepository;
import app.web.pavelk.read1.repository.PostRepository;
import app.web.pavelk.read1.repository.UserRepository;
import app.web.pavelk.read1.service.mail.MailContentBuilder;
import app.web.pavelk.read1.service.mail.MailService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@Service
@AllArgsConstructor
@Slf4j
public class CommentService {
    private static final String POST_URL = "";
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final CommentMapper commentMapper;
    private final CommentRepository commentRepository;
    private final MailContentBuilder mailContentBuilder;
    private final MailService mailService;

    //сохранить комент
    public ResponseEntity<Void> save(CommentsDto commentsDto) {
        log.info("createComment");
        Post post = postRepository.findById(commentsDto.getPostId())
                .orElseThrow(() -> new PostNotFoundException(commentsDto.getPostId().toString()));
        Comment comment = commentMapper.map(commentsDto, post, authService.getCurrentUser());
        commentRepository.save(comment);

        String message = mailContentBuilder.build(authService.getCurrentUser() + " posted a comment on your post." + POST_URL);
        sendCommentNotification(message, post.getUser());
        return ResponseEntity.status(CREATED).build();
    }

    //отправить письмо о получении коментария
    private void sendCommentNotification(String message, User user) {
        mailService.sendMail(new NotificationEmail(user.getUsername() + " Commented on your post", user.getEmail(), message));
    }

    //полусить все коментарии для поста
    public ResponseEntity<List<CommentsDto>> getAllCommentsForPost(Long postId) {
        log.info("getAllCommentsForPost");
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId.toString()));
        return ResponseEntity.status(OK).body(
                commentRepository.findByPost(post).stream()
                        .map(commentMapper::mapToDto).collect(toList()));
    }

    //получить все коментарии юзера
    public ResponseEntity<List<CommentsDto>> getAllCommentsForUser(String userName) {
        log.info("getAllCommentsForUser");
        User user = userRepository.findByUsername(userName)
                .orElseThrow(() -> new UsernameNotFoundException(userName));
        System.out.println("--getAllCommentsForUser");
        return ResponseEntity.status(OK)
                .body(commentRepository.findAllByUser(user)
                        .stream()
                        .map(commentMapper::mapToDto)
                        .collect(toList()));
    }
}
