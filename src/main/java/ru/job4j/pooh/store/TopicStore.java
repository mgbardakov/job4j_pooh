package ru.job4j.pooh.store;

import ru.job4j.pooh.exceptions.NoSubscribersOnTopic;

public interface TopicStore {
    boolean broadcastMessage(String topic, String message);
    String getMessage(String userId, String topic);
}
