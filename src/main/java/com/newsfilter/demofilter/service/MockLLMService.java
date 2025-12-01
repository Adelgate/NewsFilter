package com.newsfilter.demofilter.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class MockLLMService implements LLMService {
    @Override
    public String summarize(String text) {
        log.debug("Mock summarization invoked");
        return text != null && text.length() > 200 ? text.substring(0, 200) + "..." : text;
    }

    @Override
    public List<String> extractTopics(String text) {
        log.debug("Mock topic extraction invoked");
        return List.of("general", "ai-insights");
    }
}