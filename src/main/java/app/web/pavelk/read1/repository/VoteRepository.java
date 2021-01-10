package app.web.pavelk.read1.repository;


import app.web.pavelk.read1.model.Post;
import app.web.pavelk.read1.model.User;
import app.web.pavelk.read1.model.Vote;
import app.web.pavelk.read1.model.VoteType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {

    @Query("select v.voteType from Vote v where v.post = :post and v.user = :user")
    Optional<VoteType> getTypeByUser(Post post, User user);

    @Query("select v from Vote v where v.post.postId = :postId and v.user = :user")
    Optional<Vote> getTypeByUserPostId(Long postId, User user);

    @Query("select sum(case when v.voteType = 0 then 1 else 0 end) - sum(case when v.voteType = 1 then 1 else 0 end)  " +
            "from Vote v where v.post = :post")
    Integer getCount(Post post);

}
