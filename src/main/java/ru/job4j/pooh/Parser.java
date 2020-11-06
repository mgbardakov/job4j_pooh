package ru.job4j.pooh;

public interface Parser {
    String getQueryType();
    String getJMSMode();
    String getQueueTitle();
    String getTopicTitle();
    String getUserID();
}
