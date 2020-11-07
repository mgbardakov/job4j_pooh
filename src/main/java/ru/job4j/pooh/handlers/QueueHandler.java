package ru.job4j.pooh.handlers;

import ru.job4j.pooh.exceptions.NoSuchKeyException;
import ru.job4j.pooh.parser.Parser;
import ru.job4j.pooh.store.MemQueueStore;
import ru.job4j.pooh.store.QueueStore;

public class QueueHandler implements Handler {
    private final Parser parser;
    private final QueueStore store = MemQueueStore.getInstance();
    private static final String OK_GET_TMPL = "%s";
    private static final String ERROR_GET_TMPL = "%s";
    private static final String OK_POST_TMPL = "%s";

    public QueueHandler(Parser parser) {
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
        try {
            var queueTitle = parser.getTitle();
            var message = store.getMessage(queueTitle);
            return String.format(OK_GET_TMPL, message);
        } catch (NoSuchKeyException e) {
            return ERROR_GET_TMPL;
        }
    }

    private String doPost() {
        store.addMessage(parser.getTitle(), parser.getMessage());
        return OK_POST_TMPL;
    }
}
