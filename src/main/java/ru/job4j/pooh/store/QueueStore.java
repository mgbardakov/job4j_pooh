package ru.job4j.pooh.store;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class QueueStore implements MessageStore {

    private QueueStore() {
    }

    public static final class Holder {
        public static final QueueStore INSTANCE = new QueueStore();
    }

    public static QueueStore getInstance() {
        return Holder.INSTANCE;
    }

    private final Map<String, Queue<String>> store = new ConcurrentHashMap<>();

    @Override
    public boolean addMessage(String queueName, String message) {
        if (store.containsKey(queueName)) {
            store.get(queueName).offer(message);
        } else {
            var queue = new ConcurrentLinkedQueue<String>();
            queue.offer(message);
            store.put(queueName, queue);
        }
        return true;
    }

    @Override
    public String getMessage(String key) {
        return store.get(key).poll();
    }
}
