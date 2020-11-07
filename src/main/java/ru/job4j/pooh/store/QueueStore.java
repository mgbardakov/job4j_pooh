package ru.job4j.pooh.store;

import ru.job4j.pooh.exceptions.NoSuchKeyException;

public interface QueueStore {
    boolean addMessage(String queueName, String message);
    String getMessage(String queueName) throws NoSuchKeyException;
}
