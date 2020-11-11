package ru.job4j.pooh;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class StringSocket implements AutoCloseable {
    private final Socket socket;

    public StringSocket(Socket socket) {
        this.socket = socket;
    }

    public void sendString(String query) {
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

    public String acceptString() {
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

    @Override
    public void close() throws Exception {
        socket.close();
    }
}
