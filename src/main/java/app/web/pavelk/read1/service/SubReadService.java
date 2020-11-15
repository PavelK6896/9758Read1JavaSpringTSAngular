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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubReadService {

    private final SubredditRepository subredditRepository;

//    private final SubredditMapper subredditMapper;

    @Transactional // сохранение подписки и добасление ид
    public SubredditDto save(SubredditDto subredditDto) {
        Subreddit save = subredditRepository.save(Subreddit
                .builder().name(subredditDto.getName()).description(subredditDto.getDescription()).build());
//        Subreddit save = subredditRepository.save(subredditMapper.mapDtoToSubreddit(subredditDto));
        subredditDto.setId(save.getId());
        return subredditDto;
    }

//    @Transactional(readOnly = true) // возрощает все посты прогоняя через мапер
//    public List<SubredditDto> getAll() {
//        return subredditRepository.findAll()
//                .stream()
//                .map(subredditMapper::mapSubredditToDto)//заменяет прямой билдер
//                .collect(toList());
//    }

//    public SubredditDto getSubreddit(Long id) {
//        Subreddit subreddit = subredditRepository.findById(id)
//                .orElseThrow(() -> new SpringRedditException("No subreddit found with ID - " + id));
//        return subredditMapper.mapSubredditToDto(subreddit);
//    }
}
