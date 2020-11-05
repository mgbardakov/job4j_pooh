package ru.job4j.pooh;

public class HTTPQueryHandler implements QueryHandler {

    private static final String EMPTY_QUEUE_RESPONSE = "";
    private static final String OK_RESPONSE_TEMPLATE = "";
    private static final String NO_SUBSCRIBERS_RESPONSE = "";
    private final Parser parser;

    public HTTPQueryHandler(String query) {
        parser = new HTTPQueryParser(query);
    }

    @Override
    public String getResponse() throws NoSuchKeyException {
        var queryType = parser.getQueryType();
        var jmsMode = parser.getJMSMode();
        var queueOrTopicTitle = parser.getTopicOrQueueName();
        var rsl = "";
        if (queryType == QueryType.GET
            && jmsMode == JMSMode.QUEUE) {
            rsl = getResponse(queueOrTopicTitle,
                    MessageContainer.getQueueContainer());
        } else if (queryType == QueryType.GET
                && jmsMode == JMSMode.TOPIC) {

        }
        return null;
    }

    private final TopicExchanger<UserConnection, String> exchanger
            = new TopicExchanger<>(MessageContainer.getTopicContainer());

    private <K> String getResponse(K key, MessageContainer<K, String> container) {
        if (container.getQueues().isEmpty()) {
            return EMPTY_QUEUE_RESPONSE;
        }
        return OK_RESPONSE_TEMPLATE
                + container.getMessage(key);
    }

    private String postQueue(String key, String message) {
        MessageContainer.getQueueContainer().addMessage(key, message);
        return OK_RESPONSE_TEMPLATE;
    }

    private String postTopic(String topic, String message) {
        if (exchanger.sendMessage(message, x -> x.getComplexKey().contains(topic))) {
            return OK_RESPONSE_TEMPLATE;
        } else {
            return NO_SUBSCRIBERS_RESPONSE;
        }
    }

}
