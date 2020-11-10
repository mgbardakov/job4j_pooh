package ru.job4j.pooh.store;


public interface QueueStore {
    boolean addMessage(String queueName, String message);
    String getMessage(String queueName);
}
