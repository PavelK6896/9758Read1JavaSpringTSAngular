package app.web.pavelk.read1.service;


import app.web.pavelk.read1.dto.SubredditDto;
import app.web.pavelk.read1.exceptions.SpringRedditException;
import app.web.pavelk.read1.mapper.SubredditMapper;
import app.web.pavelk.read1.model.Subreddit;
import app.web.pavelk.read1.repository.SubredditRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubReadService {

    private final SubredditRepository subredditRepository;
    private final SubredditMapper subredditMapper;

    @Transactional
    public ResponseEntity<SubredditDto> save(SubredditDto subredditDto) {
        log.info("createSubreddit");
        Subreddit save = subredditRepository.save(subredditMapper.mapDtoToSubreddit(subredditDto));
        subredditDto.setId(save.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(subredditDto);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<List<SubredditDto>> getAll() {
        log.info("getAllSubreddits");
        return ResponseEntity.status(HttpStatus.OK).body(subredditRepository.findAll()
                .stream()
                .map(subredditMapper::mapSubredditToDto)
                .collect(toList()));
    }

    public ResponseEntity<SubredditDto> getSubreddit(Long id) {
        log.info("getSubreddit");
        Subreddit subreddit = subredditRepository.findById(id)
                .orElseThrow(() -> new SpringRedditException("No subreddit found with ID - " + id));
        return ResponseEntity.status(HttpStatus.OK).body(subredditMapper.mapSubredditToDto(subreddit));
    }
}
