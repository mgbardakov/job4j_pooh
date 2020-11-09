package ru.job4j.pooh.store;

public interface TopicStore {
    boolean broadcastMessage(String topic, String message);
    String getMessage(String userId, String topic);
}
