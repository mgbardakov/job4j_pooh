package ru.job4j.pooh.handlers;

import ru.job4j.pooh.parser.Parser;
import ru.job4j.pooh.store.MemTopicStore;
import ru.job4j.pooh.store.TopicStore;

public class TopicHandler implements Handler {
    private final Parser parser;
    private final TopicStore store = MemTopicStore.getInstance();
    private static final String OK_GET_TMPL = "HTTP/1.1 200 ОК\r\nUserID: %s\r\n"
            + "\r\n%s";
    private static final String SUB_GET_TMPL = "HTTP/1.1 200 ОК YOU'VE BEEN"
            + " SUBSCRIBED ON %s TOPIC\r\nUserID: %s\r\n";
    private static final String NO_MSG_GET_TMPL = "HTTP/1.1 200 ОК \r\nUserID: %s\r\n";
    private static final String OK_POST_TMPL = "HTTP/1.1 200 ОК\r\nUserID: %s\r\n";
    private static final String ERROR_POST_TMPL = "HTTP/1.1 200 ОК NO SUBSCRIBERS "
            + "ON %s TOPIC\r\nUserID: %s\r\n";

    public TopicHandler(Parser parser) {
        this.parser = parser;
    }

    @Override
    public String getResponse() {
        var rsl = "";
        var method = parser.getHTTPMethod();
        if (method.equals("GET")) {
            rsl = doGet();
        } else if (method.equals("POST")) {
            rsl = doPost();
        }
        return rsl;
    }

    private String doGet() {
        var message = store.getMessage(parser.getUserID(),
                parser.getTitle());
        var rsl = "";
        switch (message) {
            case "NO_SUCH_KEY" :
                rsl = String.format(SUB_GET_TMPL, parser.getTitle(),
                        parser.getUserID());
                break;
            case "NO_MESSAGES" :
                rsl = String.format(NO_MSG_GET_TMPL, parser.getUserID());
                break;
            default :
                rsl = String.format(OK_GET_TMPL, parser.getUserID(),
                        message);
        }
        return rsl;
    }

    private String doPost() {
        var rsl = "";
        if (store.broadcastMessage(parser.getTitle(), parser.getMessage())) {
            rsl = String.format(OK_POST_TMPL, parser.getUserID());
        } else {
            rsl = String.format(ERROR_POST_TMPL, parser.getTitle(), parser.getUserID());
        }
       return rsl;
    }
}
