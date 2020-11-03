package ru.job4j.pooh;

import java.util.function.Predicate;

public class TopicExchanger<K, V> {

    private final MessageContainer<K, V> container;

    public TopicExchanger(MessageContainer<K, V> container) {
        this.container = container;
    }

    public boolean sendMessage(V message, Predicate<K> predicate) {
        var rsl = false;
        var queues = container.getQueues();
        for (K key : queues.keySet()) {
            if (predicate.test(key)) {
                queues.get(key).offer(message);
                rsl = true;
            }
        }
        return rsl;
    }
}
