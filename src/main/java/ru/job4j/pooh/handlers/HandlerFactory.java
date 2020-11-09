package ru.job4j.pooh.handlers;

import ru.job4j.pooh.Server;
import ru.job4j.pooh.parser.Parser;
import ru.job4j.pooh.parser.SimpleParser;

public class HandlerFactory {
    public static Handler getProperHandler(String query) throws UnsupportedOperationException {
        Handler rslHandler;
        Parser parser = new SimpleParser(query);
        Parser fixedParser = parser;
        var fixedQuery = query;
        if (parser.getUserID().equals("unknownID")) {
            fixedQuery = query.replace("unknownID", Server.getNewID());
            fixedParser = new SimpleParser(fixedQuery);
        }
        if (fixedParser.getJMSMode().equals("queue")) {
            rslHandler = new QueueHandler(fixedParser);
        } else if (fixedParser.getJMSMode().equals("topic")) {
            rslHandler = new TopicHandler(fixedParser);
        } else {
            throw new UnsupportedOperationException();
        }
        return rslHandler;
    }
}
