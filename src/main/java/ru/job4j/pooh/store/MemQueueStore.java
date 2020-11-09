package ru.job4j.pooh.store;

import ru.job4j.pooh.exceptions.NoSuchKeyException;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MemQueueStore implements QueueStore {

    private MemQueueStore() {
    }

    public static final class Holder {
        public static final MemQueueStore INSTANCE = new MemQueueStore();
    }

    public static MemQueueStore getInstance() {
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
        var rsl = "";
        if (!store.containsKey(key)) {
            rsl = "NO_SUCH_KEY";
        } else {
            rsl = store.get(key).poll();
        }
        return rsl;
    }
}
