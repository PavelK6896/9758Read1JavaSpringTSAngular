package app.web.pavelk.read1.mapper;


import app.web.pavelk.read1.dto.SubredditDto;
import app.web.pavelk.read1.model.Post;
import app.web.pavelk.read1.model.Subreddit;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

// нужен плагин в мавене
// и он всеравно неработает ошибки и т д
// проще дто сбилдить наверно вот
@Mapper(componentModel = "spring")
public interface SubredditMapper {

    // подменяем значение для numberOfPosts из метода
//    @Mapping( target = "numberOfPosts", expression = "0", ignore = true)
    SubredditDto mapSubredditToDto(Subreddit subreddit);

    default Integer mapPosts(Subreddit subreddits) {
        return subreddits.getPosts().size();
    }

    //из одного в другое
    @InheritInverseConfiguration
//    @Mapping(target = "posts", ignore = true)
    Subreddit mapDtoToSubreddit(SubredditDto subredditDto);
}

