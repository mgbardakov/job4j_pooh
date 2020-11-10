package ru.job4j.pooh;

import ru.job4j.pooh.parser.SimpleParser;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class Client extends Thread {
    private String id;

    public String doRequest(String query) {
        var rsl = "";
        try (Socket socket = new Socket(InetAddress.getLocalHost(), 1234)) {
            sendRequest(socket, query);
            rsl = getResponse(socket);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rsl;
    }

    private void sendRequest(Socket socket, String query) {
        try (OutputStream out = socket.getOutputStream()) {
            query.chars().forEach(x -> {
                try {
                    out.write(x);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getResponse(Socket socket) {
        var rsl = "";
        try (var in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            var rslBuilder = new StringBuilder();
            int read;
            while ((read = in.read()) != -1) {
                rslBuilder.append((char) read);
            }
            rsl = rslBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rsl;
    }
}
