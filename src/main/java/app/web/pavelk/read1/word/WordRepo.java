package app.web.pavelk.read1.word;

import org.springframework.data.jpa.repository.JpaRepository;

public interface WordRepo extends JpaRepository<WordTable, Long> {
}
