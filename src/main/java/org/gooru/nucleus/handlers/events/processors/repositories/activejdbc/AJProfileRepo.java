package org.gooru.nucleus.handlers.events.processors.repositories.activejdbc;

import org.gooru.nucleus.handlers.events.processors.ProcessorContext;
import org.gooru.nucleus.handlers.events.processors.repositories.ProfileRepo;

import io.vertx.core.json.JsonObject;

public class AJProfileRepo implements ProfileRepo {

    private final ProcessorContext context;

    public AJProfileRepo(ProcessorContext context) {
        this.context = context;
    }

    @Override
    public JsonObject followUnfollowProfileEvent() {
        return context.eventBody();
    }

}
