package ru.job4j.pooh.store;

import ru.job4j.pooh.exceptions.NoNewMessagesException;
import ru.job4j.pooh.exceptions.NoSubscribersOnTopicExp;
import ru.job4j.pooh.exceptions.NoSuchKeyException;

import java.util.Map;
import java.util.Queue;
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

    private final Map<String, Map<String, Queue<String>>> store = new ConcurrentHashMap<>();


    @Override
    public String getMessage(String userID, String topic){
        var rsl = "";
        if (!store.containsKey(userID)) {
            addTopicQueue(userID);
            subscribe(userID, topic);
            rsl = "NO_SUCH_KEY";
        } else if (store.containsKey(userID)
                && !store.get(userID).containsKey(topic)) {
            subscribe(userID, topic);
            rsl = "NO_SUCH_KEY";
        } else if (store.get(userID).get(topic).isEmpty()) {
            rsl = "NO_MESSAGES";
        } else {
            rsl = store.get(userID).get(topic).poll();
        }
        return rsl;
    }

    @Override
    public boolean broadcastMessage(String topic, String message) {
        var rsl = false;
        for (String id : store.keySet()) {
            var map = store.get(id);
            if (map.containsKey(topic)) {
                map.get(topic).offer(message);
                rsl = true;
            }
        }
        return rsl;
    }

    private void subscribe(String userID, String topic) {
        var map = store.get(userID);
        map.put(topic, new ConcurrentLinkedQueue<>());
    }

    private void addTopicQueue(String userID) {
        store.put(userID, new ConcurrentHashMap<>());
    }

    private boolean hasTopic(String topic) {
        var rsl = false;
        for (String id : store.keySet()) {
            if (store.get(id).containsKey(topic)) {
                rsl = true;
                break;
            }
        }
        return rsl;
    }
}
