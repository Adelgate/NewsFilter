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

    // Basic mapping - service will handle additional fields
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "sourceId", ignore = true)
    @Mapping(target = "title", ignore = true) // Will be extracted from text
    @Mapping(target = "content", source = "text")
    @Mapping(target = "summary", ignore = true) // Can be generated later
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "imageUrl", source = "mediaUrl")
    @Mapping(target = "publishedAt", source = "postedAt")
    @Mapping(target = "url", source = "link")
    @Mapping(target = "fetchedAt", ignore = true)
    @Mapping(target = "rawPayload", ignore = true)
    @Mapping(target = "metadata", ignore = true)
    NewsDocument toDocument(NewsRequest request);

    @Mapping(target = "id", expression = "java(mapId(newsDocument.getId()))")
    @Mapping(target = "source", expression = "java(mapSourceId(newsDocument.getSourceId()))")
    @Mapping(target = "text", source = "content")
    @Mapping(target = "mediaUrl", source = "imageUrl")
    @Mapping(target = "postedAt", source = "publishedAt")
    @Mapping(target = "link", source = "url")
    @Mapping(target = "createdAt", source = "fetchedAt")
    NewsResponse toResponse(NewsDocument newsDocument);

    List<NewsResponse> toResponseList(List<NewsDocument> newsDocuments);

    default String mapId(ObjectId id) {
        return id != null ? id.toHexString() : null;
    }

    default String mapSourceId(Long sourceId) {
        return sourceId != null ? "source-" + sourceId : "unknown";
    }
}