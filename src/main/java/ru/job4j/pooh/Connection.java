package ru.job4j.pooh;

import ru.job4j.pooh.handlers.Handler;
import ru.job4j.pooh.handlers.HandlerFactory;

public class Connection extends Thread {

    private final StringSocket stringSocket;

    public Connection(StringSocket stringSocket) {
        this.stringSocket = stringSocket;
    }

    @Override
    public void run() {
        try {
            var query = stringSocket.acceptString();
            Handler queryHandler = HandlerFactory.getProperHandler(query);
            var response = queryHandler.getResponse();
            stringSocket.sendString(response);
            stringSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
