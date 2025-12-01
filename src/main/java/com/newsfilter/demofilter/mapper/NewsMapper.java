package com.newsfilter.demofilter.mapper;

import com.newsfilter.dto.NewsRequest;
import com.newsfilter.dto.NewsResponse;
import com.newsfilter.entity.News;
import org.bson.types.ObjectId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface NewsMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "score", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    News toEntity(NewsRequest request);

    @Mapping(target = "id", expression = "java(mapId(news.getId()))")
    NewsResponse toResponse(News news);

    List<NewsResponse> toResponseList(List<News> news);

    default String mapId(ObjectId id) {
        return id != null ? id.toHexString() : null;
    }
}