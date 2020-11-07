package ru.job4j.pooh.store;

import java.util.HashSet;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MemTopicStore implements TopicStore {

    private MemTopicStore() {
    }

    public static final class Holder {
        public static final MemTopicStore INSTANCE = new MemTopicStore();
    }

    public static MemTopicStore getInstance() {
        return MemTopicStore.Holder.INSTANCE;
    }

    private final Map<String, User> store = new ConcurrentHashMap<>();


    @Override
    public String getMessage(String userID, String topic) {
        var rsl = String.format("YOU'VE BEEN SUBSCRIBED TO %s",
                topic.toUpperCase());
        if (!store.containsKey(userID)) {
            addUser(userID);
            subscribe(userID, topic);
        } else if (store.containsKey(userID)
                && !store.get(userID).queues.containsKey(topic)) {
            subscribe(userID, topic);
        } else {
            rsl = store.get(userID).getMessage(topic);
        }
        return rsl;
    }

    @Override
    public boolean broadcastMessage(String topic, String message) {
        var rsl = false;
        for (String id : store.keySet()) {
            var user = store.get(id);
            if (user.getTopics().contains(topic)) {
                user.addMessage(message, topic);
                rsl = true;
            }
        }
        return rsl;
    }

    private void subscribe(String userID, String topic) {
        var user = store.get(userID);
        user.queues.put(topic, new ConcurrentLinkedQueue<>());
    }

    private void addUser(String userID) {
        store.put(userID, new User());
    }


    private static class User {

        private final Map<String, Queue<String>> queues;

        public User() {
            queues = new ConcurrentHashMap<>();
        }

        public Set<String> getTopics() {
            return queues.keySet();
        }

        public void addMessage(String message, String topic) {
            queues.get(topic).offer(message);
        }

        public String getMessage(String topic) {
            return queues.get(topic).poll();
        }

    }
}
