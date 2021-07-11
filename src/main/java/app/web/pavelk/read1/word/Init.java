package app.web.pavelk.read1.word;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.List;

@Component
@RequiredArgsConstructor
public class Init {

    private final EntityManager entityManager;

    @Value("classpath:data/word2.txt")
    Resource resource;

    @Transactional
    public void one() throws IOException {
        List<String> lines = Files.readAllLines(resource.getFile().toPath(), Charset.forName("windows-1251"));
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(" insert into  words.word ( word1, translate1) values ");
        stringBuilder.append(" ( '-', '-' ); ");
        int i = entityManager.createNativeQuery(stringBuilder.toString()).executeUpdate();
    }

    @Transactional
    public void two() throws IOException {
        List<String> lines = Files.readAllLines(resource.getFile().toPath(), Charset.forName("windows-1251"));
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(" insert into  words.word ( word1, translate1) values ");
        for (int i = 0; i < lines.size(); i = i + 2) {
            stringBuilder.append(" ( '" + lines.get(i) + "', '" + lines.get(i + 1) + "' ), ");
        }
        stringBuilder.append(" ( '-', '-' ); ");
        int i = entityManager.createNativeQuery(stringBuilder.toString()).executeUpdate();
    }
}
