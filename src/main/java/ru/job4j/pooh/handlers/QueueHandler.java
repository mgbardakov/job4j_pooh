package ru.job4j.pooh.handlers;

import ru.job4j.pooh.parser.Parser;
import ru.job4j.pooh.store.MemQueueStore;
import ru.job4j.pooh.store.QueueStore;

public class QueueHandler implements Handler {
    private final Parser parser;
    private final QueueStore store = MemQueueStore.getInstance();
    private static final String OK_GET_TMPL = "HTTP/1.1 200 ОК\r\n\r\n%s";
    private static final String NO_QUEUE_GET_TMPL = "HTTP/1.1 200 ОК %s QUEUE DOES"
            + " NOT EXIST\r\n";
    private static final String EMPTY_QUEUE_TMPL = "HTTP/1.1 200 ОК %s QUEUE"
            + " IS EMPTY";
    private static final String OK_POST_TMPL = "HTTP/1.1 200 ОК\r\n";

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
        var queueTitle = parser.getTitle();
        var msg = store.getMessage(queueTitle);
        var rsl = "";
        switch (msg) {
            case "NO_SUCH_KEY" :
                rsl = String.format(NO_QUEUE_GET_TMPL, parser.getTitle());
                break;
            case "QUEUE_IS_EMPTY" :
                rsl = String.format(EMPTY_QUEUE_TMPL, parser.getTitle());
                break;
            default :
                rsl = String.format(OK_GET_TMPL, msg);
        }
        return rsl;
    }

    private String doPost() {
        store.addMessage(parser.getTitle(), parser.getMessage());
        return OK_POST_TMPL;
    }
}
