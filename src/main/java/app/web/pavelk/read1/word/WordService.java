package app.web.pavelk.read1.word;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class WordService {

    private final WordRepo wordRepo;
    private final Init init;

    @Value("classpath:data/word2.txt")
    Resource resource;

//    @PostConstruct
    private void initWords() throws IOException {
        long l = System.nanoTime();
        List<String> lines = Files.readAllLines(resource.getFile().toPath(), Charset.forName("windows-1251"));
        ArrayList<WordTable> wordTables = new ArrayList<>();
        for (int i = 0; i < lines.size(); i = i + 2) {
            wordTables.add(WordTable.builder().word1(lines.get(i)).translate1(lines.get(i + 1)).build());
        }
        List<WordTable> wordTables1 = wordRepo.saveAll(wordTables);
        log.info(String.valueOf(System.nanoTime() - l)); // 6_701_592_700
    }

    @PostConstruct
    public void initWords2() throws IOException {
        long l = System.nanoTime();
        init.two();
        log.info(String.valueOf(System.nanoTime() - l));// 1_340_135_200
    }

    public ResponseEntity<Page<WordTable>> getWords(Pageable pageable) {
        return ResponseEntity.ok(wordRepo.findAll(pageable));
    }

}
