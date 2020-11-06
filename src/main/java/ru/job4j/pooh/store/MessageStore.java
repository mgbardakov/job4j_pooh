package ru.job4j.pooh.store;

public interface MessageStore {
    boolean addMessage(String postingKey, String message);
    String getMessage(String gettingKey);
}
