package com.newsfilter.demofilter.mapper;

import com.newsfilter.demofilter.domain.mongo.NewsDocument;
import com.newsfilter.demofilter.dto.NewsRequest;
import com.newsfilter.demofilter.dto.NewsResponse;
import org.bson.types.ObjectId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface NewsMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fetchedAt", ignore = true)
    NewsDocument toDocument(NewsRequest request);

    @Mapping(target = "id", expression = "java(mapId(newsDocument.getId()))")
    NewsResponse toResponse(NewsDocument newsDocument);

    List<NewsResponse> toResponseList(List<NewsDocument> newsDocuments);

    default String mapId(ObjectId id) {
        return id != null ? id.toHexString() : null;
    }
}