package app.web.pavelk.read1.word;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import static javax.persistence.GenerationType.IDENTITY;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "word", schema = "words")
public class WordTable {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String word1;

    private String translate1;

}
