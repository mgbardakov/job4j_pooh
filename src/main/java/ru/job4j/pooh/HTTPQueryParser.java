package ru.job4j.pooh;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;
import javax.json.*;

public class HTTPQueryParser implements Parser {

    private final String query;
    private static final Pattern MESSAGE_DELIMITER = Pattern.compile("\r\n\r\n");
    private static final Pattern FIRST_LINE_DELIMITER = Pattern.compile(" ?/");
    private static final String GET = "GET";
    private static final String POST = "POST";
    private static final String QUEUE = "queue";
    private static final String TOPIC = "topic";
    private final String stringQueryType;
    private final String stringJMSMode;

    public HTTPQueryParser(String query) {
        this.query = query;
        var firstLine = FIRST_LINE_DELIMITER.split(query);
        stringQueryType = firstLine[0];
        stringJMSMode = firstLine[1];
    }

    @Override
    public QueryType getQueryType() {
        var rsl = QueryType.UNKNOWN;
        if (stringQueryType.equals(GET)) {
            rsl = QueryType.GET;
        } else if (stringQueryType.equals(POST)) {
            rsl = QueryType.POST;
        }
        return rsl;
    }

    @Override
    public JMSMode getJMSMode() {
        var rsl = JMSMode.UNKNOWN;
        if (stringJMSMode.equals(QUEUE)) {
            rsl = JMSMode.QUEUE;
        } else if (stringQueryType.equals(TOPIC)) {
            rsl = JMSMode.TOPIC;
        }
        return rsl;
    }

    @Override
    public String getTopicOrQueueName() throws NoSuchKeyException {
        var message = MESSAGE_DELIMITER.split(query)[1];
        String rsl = "";
        if (stringQueryType.equals(GET)) {
            rsl = MESSAGE_DELIMITER.split(query)[2];
        } else if (stringQueryType.equals(POST)) {
            rsl = getTopicOrQueueWhenPost(message);
        }
        return rsl;
    }

    private String getTopicOrQueueWhenPost(
            String message) throws NoSuchKeyException {
        var rsl = "";
        var reader = Json.createReader(
                new ByteArrayInputStream(
                        message.getBytes(StandardCharsets.UTF_8)));
        JsonObject jsonMessage = reader.readObject();
        var key = "";
        switch (getJMSMode()) {
            case QUEUE: key = QUEUE;
                break;
            case TOPIC: key = TOPIC;
                break;
            default:
                throw new NoSuchKeyException("No needed key in JSON");
        }
        rsl = jsonMessage.getString(key);
        return rsl;
    }
}
