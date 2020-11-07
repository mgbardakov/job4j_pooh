package ru.job4j.pooh;

import java.util.concurrent.atomic.AtomicInteger;

public class Server extends Thread {
    public static final AtomicInteger idCounter = new AtomicInteger();
    public static String getNewID(){
        return String.valueOf(idCounter.incrementAndGet());
    }
}
