package ru.job4j.pooh.client;

import ru.job4j.pooh.parser.Parser;
import ru.job4j.pooh.parser.SimpleParser;

public class SocketConsumerClient extends SocketClient implements ConsumerClient {

    private String userID = "unknownID";
    private static final String QUEUE_GET_TMPL = "GET queue/%s\r\n\r\n";
    private static final String TOPIC_GET_TMPL = "GET topic/%s\r\nid; %s\r\n\r\n";


    @Override
    public String getByQueue(String queueName) {
        return null;
    }

    @Override
    public String getByTopic(String topicName) {
        var response = doRequest(String.format(TOPIC_GET_TMPL, topicName, userID));
        if (userID.equals("unknownID")) {
            Parser parser = new SimpleParser(response);
            userID = parser.getUserID();
        }
        return response;
    }

    private String getMsgFromResponse(String response) {
        return null;
    }
}
