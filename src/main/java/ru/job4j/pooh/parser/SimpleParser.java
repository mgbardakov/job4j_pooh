package ru.job4j.pooh.parser;


import javax.json.Json;
import javax.json.JsonObject;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class SimpleParser implements Parser {

    private static final Pattern LINE_DELIMITER = Pattern.compile("\r\n");
    private static final Pattern FIRST_LINE_DELIMITER = Pattern.compile(" ?/");
    private static final Pattern HTTP_HEADER_DELIMITER = Pattern.compile(": ");
    private String query;

    public SimpleParser(String query) {
        this.query = query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    @Override
    public String getHTTPMethod() {
        return FIRST_LINE_DELIMITER.split(query)[0];
    }

    @Override
    public String getJMSMode() {
        return FIRST_LINE_DELIMITER.split(query)[1];
    }
    @Override
    public String getTitle() {
        var rsl = "unknown method";
        var method = getHTTPMethod();
        if (method.equals("GET")) {
            rsl = FIRST_LINE_DELIMITER.split(query)[2];
        } else if (method.equals("POST")) {
            var lines = LINE_DELIMITER.split(query);
            var message = lines[lines.length - 1];
            rsl = getTitleFromMessage(message);
        }
        return rsl;
    }
    @Override
    public String getUserID() {
        return getHeaders().get("UserID");
    }

    private String getTitleFromMessage(String message) {
        var rsl = "";
        var reader = Json.createReader(
                new ByteArrayInputStream(
                        message.getBytes(StandardCharsets.UTF_8)));
        JsonObject jsonMessage = reader.readObject();
        var key = getJMSMode();
        rsl = jsonMessage.getString(key);
        return rsl;
    }

    private Map<String, String> getHeaders() {
        Map<String, String> rslMap = new HashMap<>();
        var lines = LINE_DELIMITER.split(query);
        for (int i = 1; i < lines.length - 3; i++) {
            var array = HTTP_HEADER_DELIMITER.split(lines[i]);
            rslMap.put(array[0], array[1]);
        }
        return rslMap;
    }

    @Override
    public String getMessage() {
        var lines = LINE_DELIMITER.split(query);
        return lines[lines.length - 1];
    }


}
