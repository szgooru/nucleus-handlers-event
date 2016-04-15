package org.gooru.nucleus.handlers.events.processors;

import io.vertx.core.json.JsonObject;

/**
 * Created by Sachin on 19-Mar-2016.
 */
public class ProcessorContext {

    private final String eventName;
    private final JsonObject eventBody;

    public ProcessorContext(String eventName, JsonObject eventBody) {
        if (eventName == null || eventBody == null) {
            throw new IllegalStateException("Processor Context creation failed because of invalid values");
        }
        this.eventName = eventName;
        this.eventBody = eventBody.copy();
    }

    public String eventName() {
        return eventName;
    }

    public JsonObject eventBody() {
        return eventBody;
    }
}
