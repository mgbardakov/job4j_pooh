package ru.job4j.pooh.store;

import java.util.HashSet;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TopicStore implements MessageStore {

    private TopicStore() {
    }

    public static final class Holder {
        public static final TopicStore INSTANCE = new TopicStore();
    }

    public static TopicStore getInstance() {
        return TopicStore.Holder.INSTANCE;
    }

    private final Map<String, User> store = new ConcurrentHashMap<>();

    @Override
    public boolean addMessage(String topic, String message) {
        var rsl = false;
        for (String id : store.keySet()) {
            var user = store.get(id);
            if (user.getTopics().contains(topic)) {
                user.addMessage(message);
                rsl = true;
            }
        }
        return rsl;
    }

    @Override
    public String getMessage(String userID) {
        var rsl = "YOU'VE BEEN SUBSCRIBED";
        if (store.containsKey(userID)) {
            return store.get(userID).getMessage();
        }
        return store.get(userID).getMessage();
    }

    private static class User {

        private final String id;
        private final Set<String> topics;
        private final Queue<String> queue;

        public User(String id) {
            this.id = id;
            topics = new HashSet<>();
            queue = new ConcurrentLinkedQueue<>();
        }

        public String getId() {
            return id;
        }

        public Set<String> getTopics() {
            return topics;
        }

        public void addMessage(String message) {
            queue.offer(message);
        }

        public String getMessage() {
            return queue.poll();
        }

    }
}
