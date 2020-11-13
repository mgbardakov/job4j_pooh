package ru.job4j.pooh.client;

public interface ConsumerClient {
    String getByQueue(String queueName);
    String getByTopic(String topicName);
}
