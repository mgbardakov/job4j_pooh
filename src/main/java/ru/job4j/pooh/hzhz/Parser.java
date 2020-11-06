package ru.job4j.pooh.hzhz;

public interface Parser {
    QueryType getQueryType();
    JMSMode getJMSMode();
    String getTopicOrQueueName() throws NoSuchKeyException;
}
