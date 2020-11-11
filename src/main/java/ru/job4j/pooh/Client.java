package ru.job4j.pooh;

import java.net.InetAddress;
import java.net.Socket;

public class Client {
    private String id;

    public String doRequest(String query) {
        var rsl = "";
        try (StringSocket stringSocket = new StringSocket
                (new Socket(InetAddress.getLocalHost(), 1234))) {
            stringSocket.sendString(query);
            rsl = stringSocket.acceptString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rsl;
    }
}
