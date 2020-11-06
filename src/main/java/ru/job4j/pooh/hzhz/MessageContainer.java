package ru.job4j.pooh.hzhz;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MessageContainer<K, V> {

    private final Map<K, Queue<V>> queues;

    private MessageContainer() {
        queues = new ConcurrentHashMap<>();
    }

    private static final class Holder {
        private static final MessageContainer<String, String> QUEUE_INSTANCE
                = new MessageContainer<>();
        private static final MessageContainer<UserConnection, String> TOPIC_INSTANCE
                = new MessageContainer<>();
    }

    public static MessageContainer<String, String> getQueueContainer() {
        return Holder.QUEUE_INSTANCE;
    }

    public static MessageContainer<UserConnection, String> getTopicContainer() {
        return Holder.TOPIC_INSTANCE;
    }

    public void addMessage(K key, V message) {
        if (queues.containsKey(key)) {
            queues.get(key).offer(message);
        } else {
            var queue = new ConcurrentLinkedQueue<V>();
            queue.offer(message);
            queues.put(key, queue);
        }
    }

    public V getMessage(K key) {
        return queues.get(key).poll();
    }

    public Map<K, Queue<V>> getQueues() {
        return queues;
    }
}
