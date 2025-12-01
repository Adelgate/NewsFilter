package com.newsfilter.demofilter.service;

import java.util.List;

public interface LLMService {
    String summarize(String text);

    List<String> extractTopics(String text);
}