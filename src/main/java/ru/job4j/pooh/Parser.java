package ru.job4j.pooh;

public interface Parser {
    QueryType getQueryType();
    JMSMode getJMSMode();
    String getTopicOrQueueName() throws NoSuchKeyException;
}
