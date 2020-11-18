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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
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
    public void save(CommentsDto commentsDto) {
        Post post = postRepository.findById(commentsDto.getPostId())
                .orElseThrow(() -> new PostNotFoundException(commentsDto.getPostId().toString()));
        Comment comment = commentMapper.map(commentsDto, post, authService.getCurrentUser());
        commentRepository.save(comment);

        String message = mailContentBuilder.build(authService.getCurrentUser() + " posted a comment on your post." + POST_URL);
        sendCommentNotification(message, post.getUser());
    }

    //отправить письмо о получении коментария
    private void sendCommentNotification(String message, User user) {
        mailService.sendMail(new NotificationEmail(user.getUsername() + " Commented on your post", user.getEmail(), message));
    }

    //полусить все коментарии для поста
    public List<CommentsDto> getAllCommentsForPost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId.toString()));
        return commentRepository.findByPost(post)
                .stream()
                .map(commentMapper::mapToDto).collect(toList());
    }

    //получить все коментарии юзера
    public List<CommentsDto> getAllCommentsForUser(String userName) {
        User user = userRepository.findByUsername(userName)
                .orElseThrow(() -> new UsernameNotFoundException(userName));
        System.out.println("--getAllCommentsForUser");
        return commentRepository.findAllByUser(user)
                .stream()
                .map(commentMapper::mapToDto)
                .collect(toList());
    }
}
