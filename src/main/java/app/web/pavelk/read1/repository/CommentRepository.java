package app.web.pavelk.read1.repository;


import app.web.pavelk.read1.model.Comment;
import app.web.pavelk.read1.model.Post;
import app.web.pavelk.read1.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPost(Post post);

    List<Comment> findAllByUser(User user);
}
