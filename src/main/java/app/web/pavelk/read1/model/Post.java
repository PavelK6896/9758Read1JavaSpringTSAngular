package app.web.pavelk.read1.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.Instant;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "post", schema = "client")
public class Post { // пост для блога
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long postId;
    @NotBlank(message = "Post Name cannot be empty or Null")
    private String postName;
    @Nullable
    private String url;
    @Nullable
    @Lob
    private String description;
    private Integer voteCount = 0;

    @ManyToOne(fetch = LAZY)
//    упоминаемый referencedColumnName = "userId" -- должен указывать на поле в user но неработает
    //<p> по умолчанию (применяется только в том случае, если используется один столбец соединения
    //*): то же имя, что и столбец первичного ключа
    //ссылочной таблицы*.
    // возможно   @JoinColumns({ через объект это указать
    @JoinColumn(name = "userId" )
    private User user; // храним id user

    private Instant createdDate;

    //referencedColumnName = "id" не работает указатьль возможно нужно мапить подругому
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "subredditId")
    private Subreddit subreddit;
}
