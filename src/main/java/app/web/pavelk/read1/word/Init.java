package app.web.pavelk.read1.word;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class Init {

    private final EntityManager entityManager;

    @Value("classpath:data/word2.txt")
    Resource resource;

//    @Transactional
//    public void one() throws IOException {
//        List<String> lines = Files.readAllLines(resource.getFile().toPath(), Charset.forName("windows-1251"));
//        StringBuilder stringBuilder = new StringBuilder();
//        stringBuilder.append(" insert into  words.word ( word1, translate1) values ");
//        stringBuilder.append(" ( '-', '-' ); ");
//        int i = entityManager.createNativeQuery(stringBuilder.toString()).executeUpdate();
//    }

    @Transactional
    public void two() throws IOException {
//        ClassPathResource classPathResource = new ClassPathResource("data/word2.txt");
//        List<String> lines = Files.readAllLines(Path.of(classPathResource.getURI()), Charset.forName("windows-1251"));

        List<String> lines = new ArrayList<>();
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(resource.getInputStream(), Charset.forName("windows-1251")))) {
            String string;
            while ((string = bufferedReader.readLine()) != null) {
                lines.add(string);
            }
        }

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(" insert into  words.word ( word1, translate1) values ");
        for (int i = 0; i < lines.size(); i = i + 2) {
            stringBuilder.append(" ( '" + lines.get(i) + "', '" + lines.get(i + 1) + "' ), ");
        }
        stringBuilder.append(" ( '-', '-' ); ");
        int i = entityManager.createNativeQuery(stringBuilder.toString()).executeUpdate();
    }
}
