package com.newsfilter.demofilter.mapper;

import com.newsfilter.demofilter.domain.mongo.NewsDocument;
import com.newsfilter.demofilter.dto.NewsRequest;
import com.newsfilter.demofilter.dto.NewsResponse;
import org.bson.types.ObjectId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

import com.newsfilter.demofilter.domain.jpa.Source;
import com.newsfilter.demofilter.repository.jpa.SourceRepository;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class NewsMapper {

    @Autowired
    protected SourceRepository sourceRepository;

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fetchedAt", ignore = true)
    @Mapping(target = "content", source = "text")
    @Mapping(target = "imageUrl", source = "mediaUrl")
    @Mapping(target = "publishedAt", source = "postedAt")
    @Mapping(target = "url", source = "link")
    @Mapping(target = "sourceId", expression = "java(resolveSourceId(request.source()))")
    public abstract NewsDocument toDocument(NewsRequest request);

    @Mapping(target = "id", expression = "java(mapId(newsDocument.getId()))")
    @Mapping(target = "text", source = "content")
    @Mapping(target = "mediaUrl", source = "imageUrl")
    @Mapping(target = "postedAt", source = "publishedAt")
    @Mapping(target = "link", source = "url")
    @Mapping(target = "source", expression = "java(resolveSourceName(newsDocument.getSourceId()))")
    @Mapping(target = "createdAt", source = "fetchedAt")
    public abstract NewsResponse toResponse(NewsDocument newsDocument);

    public abstract List<NewsResponse> toResponseList(List<NewsDocument> newsDocuments);

    protected String mapId(ObjectId id) {
        return id != null ? id.toHexString() : null;
    }

    protected Long resolveSourceId(String outputSourceName) {
        if (outputSourceName == null) {
            return null;
        }
        return sourceRepository.findByName(outputSourceName)
                .map(Source::getId)
                .orElse(null);
    }

    protected String resolveSourceName(Long sourceId) {
        if (sourceId == null) {
            return null;
        }
        return sourceRepository.findById(sourceId)
                .map(Source::getName)
                .orElse(null);
    }
}