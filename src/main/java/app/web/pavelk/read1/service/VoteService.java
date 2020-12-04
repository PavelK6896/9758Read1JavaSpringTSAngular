package app.web.pavelk.read1.service;


import app.web.pavelk.read1.dto.VoteDto;
import app.web.pavelk.read1.exceptions.PostNotFoundException;
import app.web.pavelk.read1.exceptions.SpringRedditException;
import app.web.pavelk.read1.model.Post;
import app.web.pavelk.read1.model.Vote;
import app.web.pavelk.read1.repository.PostRepository;
import app.web.pavelk.read1.repository.VoteRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static app.web.pavelk.read1.model.VoteType.UPVOTE;


@Service
@AllArgsConstructor
@Slf4j
public class VoteService {

    private final VoteRepository voteRepository;
    private final PostRepository postRepository;
    private final AuthService authService;

    @Transactional//ставим лайк
    public ResponseEntity<Void> vote(VoteDto voteDto) {
        log.info("vote");
        //находим пост
        Post post = postRepository.findById(voteDto.getPostId())
                .orElseThrow(() -> new PostNotFoundException("Post Not Found with ID - " + voteDto.getPostId()));

        //тип +1 -1 пост и юзер
        Optional<Vote> voteByPostAndUser = voteRepository
                .findTopByPostAndUserOrderByVoteIdDesc(post, authService.getCurrentUser());

        //если уже есть голос
        if (voteByPostAndUser.isPresent() &&
                voteByPostAndUser.get().getVoteType()
                        .equals(voteDto.getVoteType())) {
            throw new SpringRedditException("You have already " //Вы уже это сделали
                    + voteDto.getVoteType() + "'d for this post");//на эту должность
        }

        //ставим голос в пост
        if (UPVOTE.equals(voteDto.getVoteType())) {
            post.setVoteCount(post.getVoteCount() + 1);
        } else {
            post.setVoteCount(post.getVoteCount() - 1);
        }

        //сохроняем
        voteRepository.save(mapToVote(voteDto, post));
        postRepository.save(post);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    //реобрпзуеться в сущьность
    private Vote mapToVote(VoteDto voteDto, Post post) {
        return Vote.builder()
                .voteType(voteDto.getVoteType())
                .post(post)
                .user(authService.getCurrentUser())
                .build();
    }
}
