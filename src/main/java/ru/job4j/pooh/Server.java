package ru.job4j.pooh;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.atomic.AtomicInteger;

public class Server extends Thread {
    public static final AtomicInteger ID_COUNTER = new AtomicInteger();
    public static String getNewID() {
        return String.valueOf(ID_COUNTER.incrementAndGet());
    }

    @Override
    public void run() {
        while(!isInterrupted()) {
            try(ServerSocket serverSocket = new ServerSocket(1234)) {
                var socket = serverSocket.accept();
                new Connection(socket).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
