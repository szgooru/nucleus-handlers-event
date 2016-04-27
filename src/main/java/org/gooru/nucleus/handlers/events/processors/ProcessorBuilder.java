package org.gooru.nucleus.handlers.events.processors;

import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;

public final class ProcessorBuilder {

    public static Processor build(Message<Object> message) {
        return new MessageProcessor(message);
    }

    public static Processor buildEmailProcessor(Vertx vertx, JsonObject config, JsonObject resultData, JsonObject message) {
        return new EmailProcessor(vertx, config, resultData, message);
    }

    private ProcessorBuilder(Message<Object> message) {
        throw new AssertionError();
    }
}
