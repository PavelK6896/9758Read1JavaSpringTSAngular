package app.web.pavelk.read1.service;


import app.web.pavelk.read1.dto.VoteDto;
import app.web.pavelk.read1.exceptions.PostNotFoundException;
import app.web.pavelk.read1.exceptions.SpringRedditException;
import app.web.pavelk.read1.exceptions.VoteException;
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

import static app.web.pavelk.read1.model.VoteType.UP_VOTE;


@Service
@AllArgsConstructor
@Slf4j
public class VoteService {

    private final VoteRepository voteRepository;
    private final PostRepository postRepository;
    private final AuthService authService;

    @Transactional
    public ResponseEntity<Void> vote(VoteDto voteDto) {
        log.info("vote");

        Post post = postRepository.findById(voteDto.getPostId())
                .orElseThrow(() -> new PostNotFoundException("Post Not Found with ID - " + voteDto.getPostId()));

        Optional<Vote> voteByPostAndUser = voteRepository
                .findTopByPostAndUserOrderByVoteIdDesc(post, authService.getCurrentUser());

        if (voteByPostAndUser.isPresent() && voteByPostAndUser.get().getVoteType().equals(voteDto.getVoteType())) {
            throw new VoteException("You have already " + voteDto.getVoteType() + "'d for this post");
        }

        if (UP_VOTE.equals(voteDto.getVoteType())) {
            post.setVoteCount(post.getVoteCount() + 1);
        } else {
            post.setVoteCount(post.getVoteCount() - 1);
        }

        voteRepository.save(mapToVote(voteDto, post));
        postRepository.save(post);

        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    private Vote mapToVote(VoteDto voteDto, Post post) {
        return Vote.builder()
                .voteType(voteDto.getVoteType())
                .post(post)
                .user(authService.getCurrentUser())
                .build();
    }
}
