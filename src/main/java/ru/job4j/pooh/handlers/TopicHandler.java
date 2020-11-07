package ru.job4j.pooh.handlers;

import ru.job4j.pooh.parser.Parser;
import ru.job4j.pooh.store.MemTopicStore;
import ru.job4j.pooh.store.TopicStore;

public class TopicHandler implements Handler {
    private final Parser parser;
    private final TopicStore store = MemTopicStore.getInstance();
    private static final String OK_GET_TMPL = "%s";
    private static final String OK_POST_TMPL = "%s";
    private static final String ERROR_POST_TMPL = "%s";

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
        var message = store.getMessage(parser.getUserID(), parser.getTitle());
        return String.format(OK_GET_TMPL, message);
    }


    private String doPost() {
        var rsl = "";
        if (store.broadcastMessage(parser.getTitle(), parser.getMessage())) {
            rsl = String.format(OK_POST_TMPL, parser.getTitle());
        } else {
            rsl = String.format(ERROR_POST_TMPL, parser.getTitle());
        }
        return rsl;
    }
}
