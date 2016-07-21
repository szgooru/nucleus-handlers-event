package org.gooru.nucleus.handlers.events.processors.repositories;

import java.util.List;

import io.vertx.core.json.JsonObject;

public interface UserRepo {

    List<String> getMultipleEmailIds(List<String> userIds);
    
    String getUsername(String userId);
    
    String[] getFirstAndLastName(String userId);

    JsonObject getUser();
}
