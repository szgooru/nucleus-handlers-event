package org.gooru.nucleus.handlers.events.processors;

import io.vertx.core.json.JsonObject;

public interface Processor {
        JsonObject process();
}
