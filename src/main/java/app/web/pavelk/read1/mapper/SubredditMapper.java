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
// в плагин ломбок или ошибка
@Mapper(componentModel = "spring")
public interface SubredditMapper {

    //из сущьности в дто
    @Mapping(target = "numberOfPosts", expression = "java(mapPosts(subreddit.getPosts()))")
    SubredditDto mapSubredditToDto(Subreddit subreddit);

    //странные метод мастракта
    default Integer mapPosts(List<Post> numberOfPosts) {
        return numberOfPosts.size();
    }

    //из дто в сущьность
    //Наследование // Обратно
    @InheritInverseConfiguration
    @Mapping(target = "posts", ignore = true)
    Subreddit mapDtoToSubreddit(SubredditDto subredditDto);


}

