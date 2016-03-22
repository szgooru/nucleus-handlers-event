package org.gooru.nucleus.handlers.events.processors.repositories;

import io.vertx.core.json.JsonObject;

/**
 * Created by subbu on 06-Jan-2016.
 */
public interface ContentRepo {
  JsonObject getResource();
  JsonObject getDeletedResource();
  
  JsonObject getQuestion();
  JsonObject getDeletedQuestion();
  
}