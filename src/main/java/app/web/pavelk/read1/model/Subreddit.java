package app.web.pavelk.read1.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.Instant;
import java.util.List;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "subreddit", schema = "client")
public class Subreddit { // сабскрайб
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    @NotBlank(message = "Community name is required")
    private String name;
    @NotBlank(message = "Description is required")
    private String description;

//   * Поле, которому принадлежит отношение. Требуется, если
//   только * связь не является однонаправленной.
//  mappedBy = "dd"
    @OneToMany(fetch = LAZY)
    private List<Post> posts;
    // будет ли создона дополнительная таблица для связи
    // и таблица создаеться без схемы в паблике

    private Instant createdDate;

    @ManyToOne(fetch = LAZY)
    private User user;

}
