package ru.job4j.pooh;

import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class UserConnection extends Thread {

    private Socket socket;

    private final Set<String> complexKey = new HashSet<>();

    public Set<String> getComplexKey() {
        return complexKey;
    }

    @Override
    public void run() {

    }
}
