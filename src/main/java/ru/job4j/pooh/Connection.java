package ru.job4j.pooh;

import java.io.*;
import java.net.Socket;

public class Connection extends Thread {

    private Socket socket;

    public Connection(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        super.run();
    }

    private String readQuery() {
        var queryBuilder = new StringBuilder();
        try(var in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            int read;
            while ((read = in.read()) != -1) {
                queryBuilder.append((char) read);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return queryBuilder.toString();
    }

    private void sendResponse(String response) {
        try(var out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {
            response.chars().forEach(x -> {
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


}
